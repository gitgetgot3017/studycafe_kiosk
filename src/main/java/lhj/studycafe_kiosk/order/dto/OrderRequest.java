package lhj.studycafe_kiosk.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OrderRequest {

    @NotNull
    private Long itemId;

    private Long couponId;
}