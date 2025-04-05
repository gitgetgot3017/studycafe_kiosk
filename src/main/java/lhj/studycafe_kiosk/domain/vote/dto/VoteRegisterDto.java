package lhj.studycafe_kiosk.domain.vote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class VoteRegisterDto {

    @NotBlank(message = "투표 제목을 입력해주세요.")
    private String voteTitle;

    private boolean multiple;

    @Size(min = 2, message = "투표 옵션은 최소 2개 이상이어야 합니다.")
    private List<String> voteOptions;
}
