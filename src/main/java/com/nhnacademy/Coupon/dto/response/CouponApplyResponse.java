package com.nhnacademy.Coupon.dto.response;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record CouponApplyResponse(
        Long userCouponId,
        String couponName,
        BigDecimal originalAmount,     // 원래 금액
        BigDecimal discountAmount,     // 할인 금액
        BigDecimal finalAmount        // 최종 금액
) {
}
