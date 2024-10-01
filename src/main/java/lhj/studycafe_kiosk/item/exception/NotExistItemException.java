package lhj.studycafe_kiosk.item.exception;

import lhj.studycafe_kiosk.order.exception.ImproperRequestException;

public class NotExistItemException extends ImproperRequestException {

    public NotExistItemException(String message) {
        super(message);
    }
}
