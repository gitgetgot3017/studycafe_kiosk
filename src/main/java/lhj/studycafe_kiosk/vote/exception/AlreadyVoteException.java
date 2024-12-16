package lhj.studycafe_kiosk.vote.exception;

public class AlreadyVoteException extends RuntimeException {

    public AlreadyVoteException(String message) {
        super(message);
    }
}
