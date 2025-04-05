package lhj.studycafe_kiosk.domain.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindItemFailResponse {

    private String domain;
    private String message;
}
