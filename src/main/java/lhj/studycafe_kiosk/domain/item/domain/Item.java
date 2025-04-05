package lhj.studycafe_kiosk.domain.item.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id") // TODO: item_id로 바꿨을 때 왜 오류가 발생하는지 원인 파악하기
    private Long id;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(unique = true)
    private String itemName;

    private Integer usageTime; // 이용 가능 시간(hour 단위) -> 일일권, 충전권에서 사용

    private Integer usagePeriod; // 이용 가능 기간(day 단위) -> 충전권, 기간권, 고정석에서 사용

    private int price;

    public Item(ItemType itemType, String itemName, Integer usageTime, Integer usagePeriod, int price) {
        this.itemType = itemType;
        this.itemName = itemName;
        this.usageTime = usageTime;
        this.usagePeriod = usagePeriod;
        this.price = price;
    }

    public void changeItem(Item changedItem) {
        this.itemType = changedItem.getItemType();
        this.itemName = changedItem.getItemName();
        this.usageTime = changedItem.getUsageTime();
        this.usagePeriod = changedItem.getUsagePeriod();
        this.price = changedItem.getPrice();
    }
}
