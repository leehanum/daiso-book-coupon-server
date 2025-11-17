package com.nhnacademy.Coupon.exception;

public class InvalidCouponException extends RuntimeException {

    public InvalidCouponException(String message) {
        super(message);
    }

    public InvalidCouponException(String message, Throwable cause) {
        super(message, cause);
    }
}