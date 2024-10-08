package lhj.studycafe_kiosk.order.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangeOrderIsUsedResponse {

    private String domain;
    private String message;
}
