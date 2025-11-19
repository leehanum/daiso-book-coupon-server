package com.nhnacademy.coupon.controller;

import com.nhnacademy.coupon.dto.request.CouponCreateRequest;
import com.nhnacademy.coupon.dto.request.UserCouponIssueRequest;
import com.nhnacademy.coupon.dto.response.CouponApplyResponse;
import com.nhnacademy.coupon.dto.response.CouponResponse;
import com.nhnacademy.coupon.dto.response.UserCouponResponse;
import com.nhnacademy.coupon.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Coupon", description = "쿠폰 관리 API")
@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @Operation(summary = "쿠폰 정책 생성", description = "새로운 쿠폰 정책을 생성합니다.")
    @PostMapping
    public ResponseEntity<CouponResponse> createCoupon(
            @Valid @RequestBody CouponCreateRequest request){
        CouponResponse response = couponService.createCoupon(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "쿠폰 발급", description = "사용자에게 쿠폰을 발급합니다.")
    @PostMapping("/issue")
    public ResponseEntity<UserCouponResponse> issueCoupon(
            @Valid @RequestBody UserCouponIssueRequest request){
        UserCouponResponse response = couponService.issueCoupon(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "사용자 쿠폰 목록 조회", description = "특정 사용자의 쿠폰 목록을 조회합니다.")
    @GetMapping("/users/{userId}")
    public ResponseEntity<Page<UserCouponResponse>> getUserCoupons(@PathVariable Long userId, Pageable pageable){
        Page<UserCouponResponse> response = couponService.getUserCoupons(userId, pageable);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "사용 가능한 쿠폰 조회", description = "사용자의 사용 가능한 쿠폰을 조회합니다.")
    @GetMapping("/users/{userId}/available")
    public ResponseEntity<List<UserCouponResponse>> getAvailableCoupons(@PathVariable Long userId){
        List<UserCouponResponse> response = couponService.getAvailableCoupons(userId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "쿠폰 적용", description = "주문 시 쿠폰을 적용하고 할인 금액을 계산합니다.")
    @PostMapping("/{userCouponId}/apply")
    public ResponseEntity<CouponApplyResponse> applyCoupon(
            @PathVariable Long userCouponId, @RequestParam BigDecimal bigDecimal){
        CouponApplyResponse response = couponService.applyCoupon(userCouponId, bigDecimal);
        return ResponseEntity.ok(response);
    }
}
