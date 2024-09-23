package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Item {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    private String itemName;

    private int price;
}
