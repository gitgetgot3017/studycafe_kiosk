package lhj.studycafekiosk.domain.usagestatus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UsageStatusFailResponse {

    private String domain;
    private String message;
}
