package com.thanhtan.groceryshop.controller;

import com.thanhtan.groceryshop.dto.request.OrderRequest;
import com.thanhtan.groceryshop.dto.response.OrderResponse;
import com.thanhtan.groceryshop.entity.Order;
import com.thanhtan.groceryshop.entity.PaymentDetail;
import com.thanhtan.groceryshop.entity.Transaction;
import com.thanhtan.groceryshop.entity.User;
import com.thanhtan.groceryshop.enums.OrderStatus;
import com.thanhtan.groceryshop.exception.AppException;
import com.thanhtan.groceryshop.exception.ErrorCode;
import com.thanhtan.groceryshop.mapper.OrderMapper;
import com.thanhtan.groceryshop.repository.OrderRepository;
import com.thanhtan.groceryshop.repository.PaymentDetailRepository;
import com.thanhtan.groceryshop.repository.TransactionRepository;
import com.thanhtan.groceryshop.repository.UserRepository;
import com.thanhtan.groceryshop.service.IEmailService;
import com.thanhtan.groceryshop.service.IOrderService;
import com.thanhtan.groceryshop.service.IVNPayService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.thanhtan.groceryshop.constant.PathConstant.API_V1_VNPAYMENT;


@RestController
@RequestMapping(API_V1_VNPAYMENT)
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {

    IVNPayService vnPayService;

    IOrderService orderService;

    OrderMapper orderMapper;

    UserRepository userRepository;

    OrderRepository orderRepository;

    IEmailService emailService;

    PaymentDetailRepository paymentDetailRepository;
    TransactionRepository transactionRepository;

    @PostMapping("/neworder")
    @PreAuthorize("hasRole('USER')")
    public String createPayemnt(@RequestBody @Valid OrderRequest orderRequest, HttpSession session) throws UnsupportedEncodingException, MessagingException {
        OrderResponse savedOrder = orderService.createOrder(orderRequest);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String baseUrl = "http://localhost:8080";
        Map<String, String> result = vnPayService.createOrder(savedOrder.getTotalPay(), baseUrl);
        String vnpayUrl = result.get("paymentUrl");
        Transaction transaction = new Transaction();
        transaction.setTransactionId(result.get("vnp_OrderInfo"));
        transaction.setUsername(username);
        transaction.setOrderId(savedOrder.getId());
        transactionRepository.save(transaction);

        return vnpayUrl;

    }

    @GetMapping("/vnpay-payment")
    @Transactional
    public RedirectView handlePaymentReturn(HttpServletRequest request) throws MessagingException {
        int paymentStatus = vnPayService.orderReturn(request);
        RedirectView redirectView = new RedirectView();
        if (paymentStatus != 1) {
            throw new AppException(ErrorCode.PAYMENT_FAILED);
        }

        PaymentDetail paymentDetail = createAndSavePaymentDetail(request);

        String transactionId = request.getParameter("vnp_OrderInfo");
        Transaction transaction = transactionRepository.findByTransactionId(transactionId);

        String username = transaction.getUsername();
        Long orderId = transaction.getOrderId();

        log.info("username: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        if (orderId == null) {
            redirectView.setUrl("http://localhost:3000/payment-result?status=failed");
            return redirectView;
        }

        updateOrderAndSendConfirmation(orderId, paymentDetail, user);

        redirectView.setUrl("http://localhost:3000/payment-result?status=success");
        return redirectView;
    }

    private PaymentDetail createAndSavePaymentDetail(HttpServletRequest request) {
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPriceString = request.getParameter("vnp_Amount");

        double totalPriceDouble = Double.parseDouble(totalPriceString) / 100;

        PaymentDetail paymentDetail = PaymentDetail.builder()
                .orderInfo(orderInfo)
                .totalPrice(String.valueOf(totalPriceDouble))
                .transactionId(transactionId)
                .paymentTime(paymentTime)
                .provider("vnpay")
                .build();

        paymentDetailRepository.save(paymentDetail);

        return paymentDetail;
    }


    private void updateOrderAndSendConfirmation(Long orderId, PaymentDetail paymentDetail, User user) throws MessagingException {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_FOUND));

        order.setPaymentDetail(paymentDetail);
        order.setOrderStatus(OrderStatus.CONFIRMED);
        Order savedOrder = orderRepository.save(order);

        // Send order confirmation email
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("order", savedOrder);
        templateModel.put("orderItems", savedOrder.getOrderItems().stream().toList());
        emailService.sendTemplateEmail(user.getEmail(), "Order Confirmation", templateModel);
    }

}
