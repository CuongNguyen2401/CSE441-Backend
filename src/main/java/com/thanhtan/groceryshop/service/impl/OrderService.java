package com.thanhtan.groceryshop.service.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thanhtan.groceryshop.dto.request.OrderItemRequest;
import com.thanhtan.groceryshop.dto.request.OrderRequest;
import com.thanhtan.groceryshop.dto.request.UpdateOrderRequest;
import com.thanhtan.groceryshop.dto.response.MonthlySalesResponse;
import com.thanhtan.groceryshop.dto.response.OrderResponse;
import com.thanhtan.groceryshop.entity.*;
import com.thanhtan.groceryshop.enums.Month;
import com.thanhtan.groceryshop.enums.OrderStatus;
import com.thanhtan.groceryshop.exception.AppException;
import com.thanhtan.groceryshop.exception.ErrorCode;
import com.thanhtan.groceryshop.exception.ResourceNotFound;
import com.thanhtan.groceryshop.mapper.OrderItemMapper;
import com.thanhtan.groceryshop.mapper.OrderMapper;
import com.thanhtan.groceryshop.repository.CouponRepository;
import com.thanhtan.groceryshop.repository.OrderRepository;
import com.thanhtan.groceryshop.repository.ProductRepository;
import com.thanhtan.groceryshop.repository.UserRepository;
import com.thanhtan.groceryshop.service.IEmailService;
import com.thanhtan.groceryshop.service.INotificationService;
import com.thanhtan.groceryshop.service.IOrderService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService implements IOrderService {

    OrderRepository orderRepository;

    OrderMapper orderMapper;

    OrderItemMapper orderItemMapper;

    ProductRepository productRepository;

    JPAQueryFactory queryFactory;

    UserRepository userRepository;

    CouponRepository couponRepository;

    INotificationService notificationService;

    SimpMessagingTemplate messagingTemplate;

    IEmailService emailService;


    @Override
    public List<OrderResponse> getAllOrderWithoutOrderItems() {
        QOrder qOrder = QOrder.order;
        List<Order> orders = queryFactory
                .select(orderProjection())
                .from(qOrder)
                .orderBy(qOrder.createdDate.desc())
                .fetch();

        return orderMapper.toOrderResponseList(orders);
    }

    @Override
    public Page<OrderResponse> getAllOrderWithoutOrderItems(Pageable pageable, String searchTerm) {
        QOrder qOrder = QOrder.order;

        JPAQuery<Order> query = queryFactory
                .select(orderProjection())
                .from(qOrder);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            query.where(qOrder.customerName.containsIgnoreCase(searchTerm)
                    .or(qOrder.email.containsIgnoreCase(searchTerm))
                    .or(qOrder.orderStatus.stringValue().containsIgnoreCase(searchTerm)));
        }

        query.orderBy(getOrderSpecifier(pageable.getSort(), qOrder))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Order> orders = query.fetch();

        JPAQuery<Order> totalQuery = queryFactory.selectFrom(qOrder);

        if (searchTerm != null && !searchTerm.isEmpty()) {
            totalQuery.where(qOrder.customerName.containsIgnoreCase(searchTerm)
                    .or(qOrder.email.containsIgnoreCase(searchTerm)));
        }
        long total = totalQuery.fetchCount();

        return new PageImpl<>(orderMapper.toOrderResponseList(orders), pageable, total);
    }

    @Override
    public List<MonthlySalesResponse> getMonthlySales() {
        List<Tuple> results = orderRepository.getMonthlySales();

        return results.stream()
                .map(tuple -> new MonthlySalesResponse(
                        Month.fromInt(tuple.get(0, Integer.class)),
                        tuple.get(1, Double.class)))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponse> findAllOrdersByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return orderRepository.findAllOrdersByUsername(username)
                .stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Double getMonthlyTotalSalesRevenue() {
        List<Tuple> results = orderRepository.getMonthlyTotalSalesRevenue();

        return results.stream()
                .map(tuple -> tuple.get(1, Double.class))
                .reduce(0.0, Double::sum);
    }

    @Override
    public Double getYearTotalSalesRevenue() {
        List<Tuple> results = orderRepository.getYearlyotalSalesRevenue();

        return results.stream()
                .map(tuple -> tuple.get(1, Double.class))
                .reduce(0.0, Double::sum);
    }

    @Override
    public Long findNumberOfOrderDaily() {
        return orderRepository.findNumberOfOrderDaily();
    }


    @Override
    public OrderResponse getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));
        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest orderRequest) throws MessagingException {
        Order savedOrder = orderMapper.toOrder(orderRequest);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        double totalPay = processOrderItems(savedOrder, orderRequest.getOrderItems());
        if (!orderRequest.getCouponCode().isEmpty()) {
            Coupon coupon = couponRepository.findByCode(orderRequest.getCouponCode());
            if (coupon == null)
                throw new AppException(ErrorCode.COUPON_NOT_EXISTED);
            totalPay = totalPay - (totalPay * coupon.getDiscount());
            coupon.setQuantity(coupon.getQuantity() - 1);
            savedOrder.setCoupon(coupon);
            couponRepository.save(coupon);
            couponRepository.deleteUserCouponsByCouponIds(List.of(coupon.getId()));
        }

        savedOrder.setTotalPay(totalPay);
        savedOrder.setOrderStatus(OrderStatus.PENDING);
        savedOrder.setUser(user);

        savedOrder = orderRepository.save(savedOrder);

        notificationService.sendNotificationToAdmin(savedOrder.getUser(), savedOrder.getId());
        Map<String, Object> templateModel = new HashMap<>();
        templateModel.put("order", savedOrder);
        templateModel.put("orderItems", savedOrder.getOrderItems().stream().toList());
        emailService.sendTemplateEmail(user.getEmail(), "Order Confirmation", templateModel);

        OrderResponse orderResponse = orderMapper.toOrderResponse(savedOrder);
        messagingTemplate.convertAndSend("/topic/order", orderResponse);
        return orderResponse;
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(UpdateOrderRequest orderRequest) {
        Order order = orderRepository.findById(orderRequest.getId())
                .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));

        orderRepository.deleteByOrderId(order.getId());
        orderMapper.updateOrder(order, orderRequest);

        double totalPay = processOrderItems(order, orderRequest.getOrderItems());
        order.setTotalPay(totalPay);
        order.setOrderStatus(orderRequest.getOrderStatus());
        order = orderRepository.save(order);


        return orderMapper.toOrderResponse(order);

    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));
        order.setOrderStatus(status);
        order = orderRepository.save(order);
        notificationService.sendUpdateOrderNotificationToUser(order.getId(), order.getUser().getId());
        return orderMapper.toOrderResponse(order);
    }
    @Override
    public double processOrderItems(Order savedOrder, List<OrderItemRequest> orderItemRequests) {
        double totalPay = 0;
        Set<OrderItem> orderItems = new HashSet<>();
        for (OrderItemRequest orderItemRequest : orderItemRequests) {
            OrderItem orderItem = orderItemMapper.toOrderItem(orderItemRequest);
            Product product = productRepository.findById(orderItemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFound(ErrorCode.RESOURCE_NOT_FOUND));

            if (orderItem.getQuantity() > product.getQuantity()) {
                throw new AppException(ErrorCode.PRODUCT_QUANTITY_EXCEEDED);
            } else {
                product.setQuantity(product.getQuantity() - orderItem.getQuantity());
            }

            orderItem.setProduct(product);
            orderItem.setOrder(savedOrder);
            orderItems.add(orderItem);
            totalPay += calculateItemTotal(orderItem);
        }
        savedOrder.setOrderItems(orderItems);
        return totalPay;
    }

    private double calculateItemTotal(OrderItem orderItem) {
        double price = (orderItem.getProduct().getSalePrice() < orderItem.getProduct().getPrice())
                ? orderItem.getProduct().getSalePrice()
                : orderItem.getProduct().getPrice();
        return price * orderItem.getQuantity();
    }

    private QBean<Order> orderProjection() {
        QOrder qOrder = QOrder.order;
        return Projections.bean(Order.class,
                qOrder.id,
                qOrder.createdDate,
                qOrder.customerName,
                qOrder.email,
                qOrder.phoneNumber,
                qOrder.address,
                qOrder.totalPay,
                qOrder.note,
                qOrder.orderStatus);
    }


    private OrderSpecifier<?> getOrderSpecifier(Sort sort, QOrder qOrder) {
        if (sort.isSorted()) {
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            Sort.Direction direction = order.getDirection();
            return switch (property) {
                case "createdDate" -> (direction.isAscending() ? qOrder.createdDate.asc() : qOrder.createdDate.desc());
                case "totalPay" -> (direction.isAscending() ? qOrder.totalPay.asc() : qOrder.totalPay.desc());
                default -> throw new IllegalArgumentException("Unknown property: " + property);
            };
        } else {
            return qOrder.createdDate.desc();
        }
    }
}
