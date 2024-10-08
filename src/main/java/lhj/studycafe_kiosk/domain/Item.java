package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Entity
@Getter
@NoArgsConstructor
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private Duration duration;

    @Column(unique = true)
    private String itemName;

    private int price;

    public Item(ItemType itemType, String itemName, Duration duration, int price) {
        this.itemType = itemType;
        this.itemName = itemName;
        this.duration = duration;
        this.price = price;
    }

    public void changeItem(Item changedItem) {
        this.itemType = changedItem.getItemType();
        this.itemName = changedItem.getItemName();
        this.price = changedItem.getPrice();
    }
}
