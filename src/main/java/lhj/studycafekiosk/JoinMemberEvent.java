package lhj.studycafekiosk;

import lhj.studycafekiosk.domain.member.domain.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class JoinMemberEvent extends ApplicationEvent {

    private Member member;

    public JoinMemberEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }
}
