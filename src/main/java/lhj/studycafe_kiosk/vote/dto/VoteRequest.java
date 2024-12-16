package lhj.studycafe_kiosk.vote.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class VoteRequest {

    @NotNull
    private Long voteTitleId;

    @NotEmpty
    private List<Long> voteOptionIds;
}
