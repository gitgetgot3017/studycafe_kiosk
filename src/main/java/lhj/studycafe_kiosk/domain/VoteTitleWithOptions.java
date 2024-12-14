package lhj.studycafe_kiosk.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class VoteTitleWithOptions {

    private String voteTitle;
    private List<String> contents;
    private boolean isMultiple;
}
