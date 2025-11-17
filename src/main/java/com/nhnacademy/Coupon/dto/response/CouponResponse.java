package com.nhnacademy.Coupon.dto.response;

import com.nhnacademy.Coupon.entity.DiscountWay;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record CouponResponse (
        Long couponId,
        String couponName,
        DiscountWay discountWay,
        BigDecimal discount,
        Long categoryId,
        Long targetBookId,
        boolean
        isBirthday,
        boolean isWelcome,
        Long minOrderAmount,
        Long maxDiscountAmount,
        Integer availabilityDays,
        LocalDateTime createdAt
){
}
