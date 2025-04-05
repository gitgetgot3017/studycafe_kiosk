package lhj.studycafe_kiosk.domain.vote.handler;

import lhj.studycafe_kiosk.domain.member.dto.JoinFailResponse;
import lhj.studycafe_kiosk.domain.vote.controller.VoteController;
import lhj.studycafe_kiosk.domain.vote.dto.VoteFailResponse;
import lhj.studycafe_kiosk.domain.vote.exception.AlreadyVoteException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = VoteController.class)
public class VoteExController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public JoinFailResponse memberFail(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getFieldError()
                .getDefaultMessage();
        return new JoinFailResponse("투표", errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public VoteFailResponse alreadyVoteFail(AlreadyVoteException e) {
        return new VoteFailResponse("투표", e.getMessage());
    }
}
