package lhj.studycafekiosk.domain.vote.handler;

import lhj.studycafekiosk.domain.member.dto.JoinFailResponse;
import lhj.studycafekiosk.domain.vote.controller.VoteController;
import lhj.studycafekiosk.domain.vote.dto.VoteFailResponse;
import lhj.studycafekiosk.domain.vote.exception.AlreadyVoteException;
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
