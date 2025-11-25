package com.nhnacademy.coupon.repository;

import com.nhnacademy.coupon.domain.coupon.repository.CouponPolicyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest  // JPA 테스트용 어노테이션
class CouponPolicyRepositoryTest {

    @Autowired
    private CouponPolicyRepository couponPolicyRepository;


}