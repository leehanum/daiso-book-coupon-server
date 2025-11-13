package com.nhnacademy.Coupon.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "UserCoupons")
public class UserCoupon {

    @Id @GeneratedValue
    @Column(name = "user_coupon_id")
    private Long userCouponId;

    // User와의 FK. User 엔티티 pk 타입이 Long 이라고 가정
    @Column(name = "user_created_id",nullable = false)
    private Long userId;

    @ManyToOne // FK
    @Column(name = "coupon_id",nullable = false)
    private Coupon coupon;

    @Enumerated(EnumType.STRING) // 쿠폰 상태
    @Column(name = "status", nullable = false, length = 10)
    private CouponStatus status;

    @Column(name = "issue_at", nullable = false) // 쿠폰 발급일
    private LocalDateTime issueAt;

    @Column(name = "expiry_at", nullable = false) // 쿠폰 만료일 (쿠폰 정책의 availability_days를 기반으로 계산)
    private LocalDateTime expiryAt;

    @Column(name = "used_at") // 쿠폰 사용일 (Null 가능)
    private LocalDateTime usedAt;


}
