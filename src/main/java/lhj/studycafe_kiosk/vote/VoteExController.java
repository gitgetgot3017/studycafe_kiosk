package lhj.studycafe_kiosk.vote;

import lhj.studycafe_kiosk.vote.dto.VoteFailResponse;
import lhj.studycafe_kiosk.vote.exception.AlreadyVoteException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = VoteController.class)
public class VoteExController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public VoteFailResponse alreadyVoteFail1(AlreadyVoteException e) {
        return new VoteFailResponse("투표", e.getMessage());
    }
}
