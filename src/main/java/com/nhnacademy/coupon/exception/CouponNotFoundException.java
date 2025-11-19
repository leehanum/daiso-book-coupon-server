package com.nhnacademy.coupon.exception;

// 런타임에 발생하는 언체크 예외로 정의
public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException() {
        super("쿠폰을 찾을 수 없습니다.");
    }

    public CouponNotFoundException(String message) {
        super(message);
    }

    public CouponNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}