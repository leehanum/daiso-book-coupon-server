package com.nhnacademy.coupon.domain.coupon.dto.request.usage;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
public record BatchCouponUseRequest(
        @NotNull Long orderId,
        @Valid @Size(min = 1) List<CouponUseItemRequest> items
) {}