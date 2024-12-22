package lhj.studycafe_kiosk.vote.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VoteResultDto {

    private String voteTitle;
    private List<VoteOptionResultDto> voteOptionResultDtoList;
}
