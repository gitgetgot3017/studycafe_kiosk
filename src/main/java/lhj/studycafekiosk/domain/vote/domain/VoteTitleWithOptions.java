package lhj.studycafekiosk.domain.vote.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VoteTitleWithOptions {

    private Long voteTitleId;
    private List<Long> voteOptionIds;
    private String voteTitle;
    private List<String> contents;
    private boolean isMultiple;
}
