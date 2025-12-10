package com.nhnacademy.coupon.domain.coupon.dto.request.usage;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class BatchCouponUseRequest {

    @NotNull(message = "주문 ID는 필수입니다.")
    @Schema(description = "주문 ID", example = "1001")
    private Long orderId; // 디버그용

    @NotEmpty(message = "사용할 쿠폰 ID 리스트는 비어있을 수 없습니다.")
    @Schema(description = "사용할 쿠폰 ID 목록", example = "[10, 15, 23]")
    private List<Long> userCouponIds;
}