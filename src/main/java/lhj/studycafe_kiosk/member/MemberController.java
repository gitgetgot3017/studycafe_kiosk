package lhj.studycafe_kiosk.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.exception.DuplicatePhoneException;
import lhj.studycafe_kiosk.exception.LoginFailException;
import lhj.studycafe_kiosk.member.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public JoinFailResponse memberFail() {
        return new JoinFailResponse("회원", "필드 검증에 실패하였습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public JoinFailResponse joinFail(DuplicatePhoneException e) {
        return new JoinFailResponse("회원가입", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public LoginFailResponse loginFail(LoginFailException e) {
        return new LoginFailResponse("로그인", e.getMessage());
    }

    @PostMapping("/join")
    public HttpEntity<JoinResponse> join(@RequestBody @Validated JoinRequest joinRequest) {

        validateDuplicatePhone(joinRequest.getPhone());

        Member member = changeFormToDomain(joinRequest);
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

    private void validateDuplicatePhone(String phone) {

        if (memberService.existPhone(phone)) {
            throw new DuplicatePhoneException("이미 등록된 휴대폰 번호입니다.");
        }
    }

    private Member changeFormToDomain(JoinRequest joinRequest) {

        return new Member(joinRequest.getName(), joinRequest.getPhone(), joinRequest.getPassword(), joinRequest.getBirth());
    }

    private Long validateLoginUser(LoginRequest loginRequest) {

        Optional<Long> opMemberId = memberService.login(loginRequest.getPhone(), loginRequest.getPassword());
        if (opMemberId.isEmpty()) {
            throw new LoginFailException("전화번호 혹은 비밀번호를 잘못 입력하셨습니다.");
        }
        return opMemberId.get();
    }
}
