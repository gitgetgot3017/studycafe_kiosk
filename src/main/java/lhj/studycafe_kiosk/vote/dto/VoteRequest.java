package lhj.studycafe_kiosk.vote.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data // TODO: 커밋 전 코드 수정 필요
public class VoteRequest {

    @NotNull
    private Long voteTitleId;

    @NotEmpty
    private List<Long> voteOptionIds;
}
