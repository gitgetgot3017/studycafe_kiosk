package lhj.studycafe_kiosk.member;

import lhj.studycafe_kiosk.member.dto.ChangeMemberInfoFailResponse;
import lhj.studycafe_kiosk.member.dto.FindMemberFailResponse;
import lhj.studycafe_kiosk.member.dto.JoinFailResponse;
import lhj.studycafe_kiosk.member.dto.LoginFailResponse;
import lhj.studycafe_kiosk.member.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = MemberController.class)
public class MemberExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, ChangeMemberInfoException.class})
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

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler
    public FindMemberFailResponse findMemberFail(NotExistMemberException e) {
        return new FindMemberFailResponse("회원", e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ChangeMemberInfoFailResponse findMemberFail(PasswordMismatchException e) {
        return new ChangeMemberInfoFailResponse("회원", e.getMessage());
    }
}
