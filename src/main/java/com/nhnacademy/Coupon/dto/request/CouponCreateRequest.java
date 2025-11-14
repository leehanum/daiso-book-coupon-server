package com.nhnacademy.Coupon.dto.request;

import com.nhnacademy.Coupon.entity.DiscountWay;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CouponCreateRequest {

    @NotBlank(message = "쿠폰 이름은 필수입니다.")
    @Size(max = 100)
    private String couponName;

    @NotNull(message = "할인 방식은 필수입니다.")
    private DiscountWay discountWay;

    @NotNull(message = "할인 금액/비율은 필수입니다.")
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal discount;

    private Long categoryId;

    private Long targetBookId;

    private boolean isBirthday = false;

    private boolean isWelcome = false;

    @Min(value = 0, message = "최소 주문 금액은 0 이상이어야 합니다.")
    private Long minOrderAmount;

    private Long maxDiscountAmount;

    @NotNull(message = "사용 가능 일수는 필수입니다.")
    @Min(value = 1)
    private Integer availabilityDays;
}
