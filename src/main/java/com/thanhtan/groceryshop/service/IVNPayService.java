package com.thanhtan.groceryshop.service;

import jakarta.servlet.http.HttpServletRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface IVNPayService {
    Map<String, String > createOrder(double total, String urlReturn) throws UnsupportedEncodingException;
    int orderReturn(HttpServletRequest request);
}
