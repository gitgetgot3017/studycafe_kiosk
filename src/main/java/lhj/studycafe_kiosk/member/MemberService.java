package lhj.studycafe_kiosk.member;

import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.member.dto.ChangeMemberInfoRequest;
import lhj.studycafe_kiosk.member.exception.PasswordMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void join(Member member) {

        memberRepository.saveMember(member);
        eventPublisher.publishEvent(new JoinMemberEvent(this, member)); // 회원가입 이벤트 발생
    }

    public boolean existPhone(String phone) {

        return memberRepository.getJoinMember(phone).size() > 0;
    }

    public Optional<Member> login(String phone, String password) {

        List<Member> member = memberRepository.getLoginMember(phone, password);

        if (member.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(member.get(0));
    }

    public void changeMemberInfo(Long memberId, String type, ChangeMemberInfoRequest changeMemberInfoRequest) {

        if (type.equals("password")) {
            Member member = memberRepository.getMember(memberId);
            if (!changeMemberInfoRequest.getCurPassword().equals(member.getPassword())) {
                throw new PasswordMismatchException("비밀번호가 일치하지 않습니다.");
            }
        }

        memberRepository.updateMember(memberId, type, changeMemberInfoRequest);
    }

    public void changePassword(Member member, String newPassword) {

        memberRepository.updatePassword(member, newPassword);
    }
}
