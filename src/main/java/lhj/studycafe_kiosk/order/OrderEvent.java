package lhj.studycafe_kiosk.order;

import lhj.studycafe_kiosk.domain.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OrderEvent extends ApplicationEvent {

    private Member member;

    public OrderEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }
}
