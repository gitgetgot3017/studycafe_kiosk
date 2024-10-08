package lhj.studycafe_kiosk.order;

import lhj.studycafe_kiosk.domain.Item;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderEvent extends ApplicationEvent {

    private Member member;
    private Item item;
    private Order order;

    public OrderEvent(Object source, Member member, Item item, Order order) {
        super(source);
        this.member = member;
        this.item = item;
        this.order = order;
    }
}
