package lhj.studycafekiosk;

import lhj.studycafekiosk.domain.item.domain.Item;
import lhj.studycafekiosk.domain.member.domain.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderEvent extends ApplicationEvent {

    private Member member;
    private Item item;

    public OrderEvent(Object source, Member member, Item item) {
        super(source);
        this.member = member;
        this.item = item;
    }
}
