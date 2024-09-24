package lhj.studycafe_kiosk.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lhj.studycafe_kiosk.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public HttpEntity<JoinResponse> join(@RequestBody @Validated JoinRequest joinRequest) {

        Member member = changeFormToDomain(joinRequest);
        memberService.join(member);

        JoinResponse joinResponse = new JoinResponse("회원가입이 성공적으로 완료되었습니다.", member.getId());
        return new ResponseEntity<>(joinResponse, HttpStatus.CREATED);
    }

    private Member changeFormToDomain(JoinRequest joinRequest) {

        return new Member(joinRequest.getName(), joinRequest.getPhone(), joinRequest.getBirth());
    }

    @Getter
    static class JoinRequest {

        @NotBlank
        private String name;

        @Pattern(regexp = "/^01([0|1|6|7|8|9])([0-9]{3,4})([0-9]{4})$/")
        private String phone;

        @Pattern(regexp = "/^([0-9]{2}(0[1-9]|1[0-2])(0[1-9]|[1,2][0-9]|3[0,1]))$/")
        private LocalDate birth;
    }

    @AllArgsConstructor
    static class JoinResponse {

        private String message;
        private Long id;
    }
}
