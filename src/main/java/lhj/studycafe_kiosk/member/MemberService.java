package lhj.studycafe_kiosk.member;

import lhj.studycafe_kiosk.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public void join(Member member) {

        memberRepository.saveMember(member);
    }
}
