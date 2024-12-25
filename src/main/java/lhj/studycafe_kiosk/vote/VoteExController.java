package lhj.studycafe_kiosk.vote;

import lhj.studycafe_kiosk.member.dto.JoinFailResponse;
import lhj.studycafe_kiosk.member.exception.ChangeMemberInfoException;
import lhj.studycafe_kiosk.vote.dto.VoteFailResponse;
import lhj.studycafe_kiosk.vote.exception.AlreadyVoteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
