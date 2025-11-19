package com.nhnacademy.coupon.repository;

import com.nhnacademy.coupon.entity.Coupon;
import com.nhnacademy.coupon.entity.DiscountWay;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest  // JPA 테스트용 어노테이션
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 생일_쿠폰_조회_테스트() {
        // given: 생일 쿠폰 저장
        Coupon birthdayCoupon = Coupon.builder()
                .couponName("생일 축하 쿠폰")
                .discountWay(DiscountWay.FIXED_AMOUNT)
                .discount(new BigDecimal("10000"))
                .isBirthday(true)
                .availabilityDays(30)
                .build();
        couponRepository.save(birthdayCoupon);

        // when: 생일 쿠폰 조회
        List<Coupon> result = couponRepository.findByIsBirthdayTrue();

        // then: 검증
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCouponName()).isEqualTo("생일 축하 쿠폰");
    }
}