package com.nhnacademy.coupon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "Coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Coupon {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Long couponId;

    @Column(name = "coupon_name", nullable = false)
    private String couponName;

    @Enumerated(EnumType.STRING)
    @Column(name = "discount_way")
    private DiscountWay discountWay;

    @Column(name = "discount")
    private BigDecimal discount;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "target_book_id") // 도서 아이디
    private Long targetBookId;

    @Column(name = "is_birthday", nullable = false) // 생일 쿠폰 여부
    private boolean isBirthday;

    @Column(name = "is_welcome", nullable = false) // 왤컴 쿠폰 여부
    private boolean isWelcome = false;

    @Column(name = "min_order_amount") // 최소 주문 금액
    private Long minOrderAmount;

    @Column(name = "max_discount_amount") // 최대 할인 금액
    private Long maxDiscountAmount;

    @Column(name = "availability_days") // 사용 가능한 기간
    private Integer availabilityDays;

    @Builder.Default
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
