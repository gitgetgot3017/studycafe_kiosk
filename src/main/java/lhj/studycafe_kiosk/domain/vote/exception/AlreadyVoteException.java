package lhj.studycafe_kiosk.domain.vote.exception;

public class AlreadyVoteException extends RuntimeException {

    public AlreadyVoteException(String message) {
        super(message);
    }
}
