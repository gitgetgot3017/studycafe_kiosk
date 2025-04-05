package lhj.studycafe_kiosk.domain.usage_status.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsageStatusFailResponse {

    private String domain;
    private String message;
}
