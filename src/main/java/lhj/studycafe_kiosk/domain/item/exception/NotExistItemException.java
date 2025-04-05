package lhj.studycafe_kiosk.domain.item.exception;

import lhj.studycafe_kiosk.domain.order.exception.ImproperRequestException;

public class NotExistItemException extends ImproperRequestException {

    public NotExistItemException(String message) {
        super(message);
    }
}
