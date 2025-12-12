package com.nhnacademy.coupon.domain.coupon.controller.api;

import com.nhnacademy.coupon.domain.coupon.dto.request.issue.UserCouponIssueRequest;
import com.nhnacademy.coupon.domain.coupon.dto.response.user.UserCouponResponse;
import com.nhnacademy.coupon.domain.coupon.service.CouponPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponIssueController {

    private final CouponPolicyService couponIssueService;

    public CouponIssueController(CouponPolicyService couponIssueService) {
        this.couponIssueService = couponIssueService;
    }

    @Operation(summary = "쿠폰 발급", description = "사용자에게 쿠폰을 발급합니다.")
    @PostMapping("/{couponPolicyId}/download")
    public ResponseEntity<UserCouponResponse> downloadCoupon(
            @RequestHeader("X-User-Id") Long userId,
            @PathVariable Long couponPolicyId) {

        UserCouponIssueRequest request = new UserCouponIssueRequest(couponPolicyId);

        UserCouponResponse response = couponIssueService.issueCoupon(userId, request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
