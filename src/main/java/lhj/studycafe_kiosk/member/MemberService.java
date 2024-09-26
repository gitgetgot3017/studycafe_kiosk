package lhj.studycafe_kiosk.member;

import lhj.studycafe_kiosk.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public void join(Member member) {

        memberRepository.saveMember(member);
    }

    public boolean existPhone(String phone) {

        return memberRepository.getJoinMember(phone).size() > 0;
    }

    public Optional<Long> login(String phone, String password) {

        List<Member> member = memberRepository.getLoginMember(phone, password);

        if (member.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(member.get(0).getId());
    }
}
