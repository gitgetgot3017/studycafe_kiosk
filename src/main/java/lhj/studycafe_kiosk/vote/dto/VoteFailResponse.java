package lhj.studycafe_kiosk.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteFailResponse {

    private String domain;
    private String message;
}
