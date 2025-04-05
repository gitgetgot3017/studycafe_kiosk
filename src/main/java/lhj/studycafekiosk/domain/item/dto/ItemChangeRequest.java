package lhj.studycafekiosk.domain.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lhj.studycafekiosk.domain.item.domain.ItemType;
import lombok.Getter;

@Getter
public class ItemChangeRequest {

    @NotNull
    private ItemType itemType;

    @NotBlank
    private String itemName;

    private Integer usageTime; // 이용 가능 시간(hour 단위) -> 일일권, 충전권에서 사용

    private Integer usagePeriod; // 이용 가능 기간(day 단위) -> 충전권, 기간권, 고정석에서 사용

    private int price;
}
