package lhj.studycafe_kiosk.member;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public JoinFailResponse joinFail1() {
        return new JoinFailResponse("회원가입", "필드 검증에 실패하였습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public JoinFailResponse joinFail2(DuplicatePhoneException e) {
        return new JoinFailResponse("회원가입", e.getMessage());
    }

    @PostMapping("/join")
    public HttpEntity<JoinResponse> join(@RequestBody @Validated JoinRequest joinRequest) {

        validateDuplicatePhone(joinRequest.getPhone());

        Member member = changeFormToDomain(joinRequest);
        memberService.join(member);

        JoinResponse joinResponse = new JoinResponse("회원가입이 성공적으로 완료되었습니다.", member.getId());
        return new ResponseEntity<>(joinResponse, HttpStatus.CREATED);
    }

    private void validateDuplicatePhone(String phone) {

        if (memberRepository.getPhoneYn(phone)) {
            throw new DuplicatePhoneException("이미 등록된 휴대폰 번호입니다.");
        }
    }

    private Member changeFormToDomain(JoinRequest joinRequest) {

        return new Member(joinRequest.getName(), joinRequest.getPhone(), joinRequest.getPassword(), joinRequest.getBirth());
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

    static class DuplicatePhoneException extends RuntimeException {

        public DuplicatePhoneException(String message) {
            super(message);
        }
    }
}
