package lhj.studycafe_kiosk.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lhj.studycafe_kiosk.domain.ItemType;
import lombok.Getter;

import java.time.Duration;

@Getter
public class ItemRegRequest {

    @NotNull
    private ItemType itemType;

    private Duration duration;

    @NotBlank
    private String itemName;

    private int price;
}
