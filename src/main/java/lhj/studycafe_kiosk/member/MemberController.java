package lhj.studycafe_kiosk.member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lhj.studycafe_kiosk.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public JoinFailResponse loginFail(LoginFailException e) {
        return new JoinFailResponse("로그인", e.getMessage());
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

        LoginResponse loginResponse = new LoginResponse();
        return new ResponseEntity<>(loginResponse, HttpStatus.ACCEPTED);
    }

    private void validateDuplicatePhone(String phone) {

        if (memberRepository.getPhoneYn(phone)) {
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

    @Getter
    static class JoinRequest {

        @NotBlank
        private String name;

        @Pattern(regexp = "^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$")
        @NotNull
        private String phone;

        @Pattern(regexp = "^[0-9]{4}$")
        @NotNull
        private String password;

        @NotNull
        private LocalDate birth;
    }

    @AllArgsConstructor
    @Getter
    static class JoinResponse {

        private String message;
        private Long id;
    }

    @AllArgsConstructor
    @Getter
    static class JoinFailResponse {

        private String domain;
        private String message;
    }

    @Getter
    static class LoginRequest {

        @Pattern(regexp = "^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$")
        @NotNull
        private String phone;

        @NotNull
        private String password;
    }

    @Getter
    static class LoginResponse {

        private String message;
        private Long id;
    }

    @Getter
    static class LoginFailResponse {

        private String domain;
        private String message;
    }

    static class DuplicatePhoneException extends RuntimeException {

        public DuplicatePhoneException(String message) {
            super(message);
        }
    }

    static class LoginFailException extends RuntimeException {

        public LoginFailException(String message) {
            super(message);
        }
    }
}
