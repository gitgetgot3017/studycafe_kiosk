package lhj.studycafekiosk.domain.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VoteOptionResultDto {

    private String content;
    private Long count;
}
