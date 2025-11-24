package com.nhnacademy.coupon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // 생일 쿠폰 정책 조회
    List<Coupon> findByIsBirthdayTrue();

    // Welcome 쿠폰 정책 조회
    List<Coupon> findByIsWelcomeTrue();

    // 특정 도서용 쿠폰 정책 조회
    List<Coupon> findByTargetBookId(Long bookId);

    // 특정 카테고리용 쿠폰 정책 조회
    List<Coupon> findByCategoryId(Long categoryId);
}