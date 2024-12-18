package lhj.studycafe_kiosk.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.member.dto.*;
import lhj.studycafe_kiosk.member.exception.*;
import lhj.studycafe_kiosk.seat.exception.EmptySeatOutException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @PostMapping("/join")
    public HttpEntity<JoinResponse> join(@RequestBody @Validated JoinRequest joinRequest) {

        validateDuplicatePhone(joinRequest.getPhone());
        validateDuplicateMemberInfo(joinRequest.getPhone(), joinRequest.getPassword());

        Member member = changeJoinRequestToMember(joinRequest);
        memberService.join(member);

        JoinResponse joinResponse = new JoinResponse("회원가입이 성공적으로 완료되었습니다.", member.getId());
        return new ResponseEntity<>(joinResponse, HttpStatus.CREATED);
    }

    private void validateDuplicatePhone(String phone) {

        if (memberService.existPhone(phone)) {
            throw new DuplicatePhoneException("이미 등록된 휴대폰 번호입니다.");
        }
    }

    // 전화번호 뒤 4자리 + 비밀번호 앞 2자리가 같은 회원을 방지하지 위함
    private void validateDuplicateMemberInfo(String phone, String password) {

        try {
            String subPhone = phone.substring(phone.length() - 4); // 전화번호 뒤 4자리 추출
            String subPassword = password.substring(0, 2); // 비밀번호 앞 2자리 추출
            memberRepository.getExistMember(subPhone, subPassword);
            throw new DuplicateMemberException("비밀번호를 변경해주세요.");
        } catch (EmptySeatOutException e) {
            // 전화번호 뒤 4자리 + 비밀번호 앞 2자리가 같은 회원이 존재하지 않는 경우. 즉, 회원가입 가능한 경우
        }
    }

    @PostMapping("/login")
    public HttpEntity<LoginResponse> login(@RequestBody @Validated LoginRequest loginRequest, HttpServletRequest request) {

        Member member = validateLoginUser(loginRequest);

        // 로그인 성공 처리
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", member.getId());

        LoginResponse loginResponse = new LoginResponse("로그인이 성공적으로 완료되었습니다.", member.getId(), member.getGrade());
        return new ResponseEntity<>(loginResponse, HttpStatus.ACCEPTED);
    }

    @PostMapping("/logout")
    public HttpEntity<LogoutResponse> logout(HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        Long loginId = null;
        if (session != null) {
            loginId = (Long) session.getAttribute("loginMember");
            session.invalidate();
        }

        LogoutResponse logoutResponse = new LogoutResponse("로그아웃이 정상적으로 완료되었습니다.", loginId);
        return new ResponseEntity<>(logoutResponse, HttpStatus.OK);
    }

    @GetMapping
    public HttpEntity<MemberInfoResponse> getMemberInfo(@SessionAttribute("loginMember") Long memberId) {

        Member member = memberRepository.getMember(memberId);
        if (member == null) {
            throw new NotExistMemberException("존재하지 않는 회원입니다.");
        }

        MemberInfoResponse memberInfoResponse = changeMemberToMemberInfoResponse(member);
        return new ResponseEntity<>(memberInfoResponse, HttpStatus.OK);
    }

    @PatchMapping
    public HttpEntity<ChangeMemberInfoResponse> changeMemberInfo(@SessionAttribute("loginMember") Long memberId, @RequestParam("type") String type, @RequestBody ChangeMemberInfoRequest changeMemberInfoRequest) {

        if (!type.equals("general") && !type.equals("phone") && !type.equals("password")) {
            throw new IllegalArgumentException("잘못된 쿼리 파라미터를 요청하였습니다.");
        }

        System.out.println("changeMemberInfoRequest = " + changeMemberInfoRequest);
        validateChangeMemberInfo(type, changeMemberInfoRequest);

        memberService.changeMemberInfo(memberId, type, changeMemberInfoRequest);

        ChangeMemberInfoResponse changeMemberInfoResponse = new ChangeMemberInfoResponse("회원 정보 수정이 정상적으로 완료되었습니다.", memberId);
        return new ResponseEntity<>(changeMemberInfoResponse, HttpStatus.ACCEPTED);
    }

    private Member changeJoinRequestToMember(JoinRequest joinRequest) {

        return new Member(null, joinRequest.getPhone(), joinRequest.getPassword(), null, joinRequest.isOptionalClause());
    }

    private Member validateLoginUser(LoginRequest loginRequest) {

        Optional<Member> opMember = memberService.login(loginRequest.getPhone(), loginRequest.getPassword());
        if (opMember.isEmpty()) {
            throw new LoginFailException("전화번호 혹은 비밀번호를 잘못 입력하셨습니다.");
        }
        return opMember.get();
    }

    private MemberInfoResponse changeMemberToMemberInfoResponse(Member member) {

        return new MemberInfoResponse(member.getName(), member.getPhone(), member.getBirth());
    }

    private void validateChangeMemberInfo(String type, ChangeMemberInfoRequest changeMemberInfoRequest) {

        if (type.equals("general")) {
            if (!StringUtils.hasText(changeMemberInfoRequest.getName()) || changeMemberInfoRequest.getBirth() == null) {
                throw new ChangeMemberInfoException();
            }
        } else if (type.equals("phone")) {
            String phone = changeMemberInfoRequest.getPhone();
            if (phone == null || !Pattern.matches("^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$", phone)) {
                throw new ChangeMemberInfoException();
            }
        } else if (type.equals("password")) {
            String curPassword = changeMemberInfoRequest.getCurPassword();
            String newPassword = changeMemberInfoRequest.getNewPassword();
            if (curPassword == null || !Pattern.matches("^[0-9]{4}$", curPassword) || newPassword == null || !Pattern.matches("^[0-9]{4}$", newPassword)) {
                throw new ChangeMemberInfoException();
            }
        }
    }
}
