package com.nhnacademy.Coupon.dto.response;

import com.nhnacademy.Coupon.entity.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCouponResponse {
    private Long userCouponId;
    private Long userId;
    private CouponResponse coupon;
    private CouponStatus status;
    private LocalDateTime issuedAt; // 발급 일자
    private LocalDateTime expiryAt; // 쿠폰 만료 일자
    private LocalDateTime usedAt; // 쿠폰 사용일
}
