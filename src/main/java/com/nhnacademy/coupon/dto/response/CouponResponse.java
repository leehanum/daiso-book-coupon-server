package com.nhnacademy.coupon.dto.response;

import com.nhnacademy.coupon.entity.DiscountWay;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "쿠폰 응답")
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
