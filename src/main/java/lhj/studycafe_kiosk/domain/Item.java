package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(unique = true)
    private String itemName;

    private int price;

    public Item(ItemType itemType, String itemName, int price) {
        this.itemType = itemType;
        this.itemName = itemName;
        this.price = price;
    }
}
