package lhj.studycafekiosk.domain.item.exception;

import lhj.studycafekiosk.domain.order.exception.ImproperRequestException;

public class NotExistItemException extends ImproperRequestException {

    public NotExistItemException(String message) {
        super(message);
    }
}
