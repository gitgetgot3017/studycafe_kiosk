package lhj.studycafe_kiosk.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.member.dto.*;
import lhj.studycafe_kiosk.member.exception.ChangeMemberInfoException;
import lhj.studycafe_kiosk.member.exception.DuplicatePhoneException;
import lhj.studycafe_kiosk.member.exception.LoginFailException;
import lhj.studycafe_kiosk.member.exception.NotExistMemberException;
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

        Member member = changeJoinRequestToMember(joinRequest);
        memberService.join(member);

        JoinResponse joinResponse = new JoinResponse("회원가입이 성공적으로 완료되었습니다.", member.getId());
        return new ResponseEntity<>(joinResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public HttpEntity<LoginResponse> login(@RequestBody @Validated LoginRequest loginRequest, HttpServletRequest request) {

        Long memberId = validateLoginUser(loginRequest);

        // 로그인 성공 처리
        HttpSession session = request.getSession();
        session.setAttribute("loginMember", memberId);

        LoginResponse loginResponse = new LoginResponse("로그인이 성공적으로 완료되었습니다.", memberId);
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

    private void validateDuplicatePhone(String phone) {

        if (memberService.existPhone(phone)) {
            throw new DuplicatePhoneException("이미 등록된 휴대폰 번호입니다.");
        }
    }

    private Member changeJoinRequestToMember(JoinRequest joinRequest) {

        return new Member(null, joinRequest.getPhone(), joinRequest.getPassword(), null, joinRequest.isOptionalClause());
    }

    private Long validateLoginUser(LoginRequest loginRequest) {

        Optional<Long> opMemberId = memberService.login(loginRequest.getPhone(), loginRequest.getPassword());
        if (opMemberId.isEmpty()) {
            throw new LoginFailException("전화번호 혹은 비밀번호를 잘못 입력하셨습니다.");
        }
        return opMemberId.get();
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
