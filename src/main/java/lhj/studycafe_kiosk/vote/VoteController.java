package lhj.studycafe_kiosk.vote;

import lhj.studycafe_kiosk.domain.VoteTitleWithOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/votes")
public class VoteController {

    private final VoteService voteService;

    @GetMapping
    public HttpEntity<List<VoteTitleWithOptions>> getVotes() {

        List<VoteTitleWithOptions> voteTitleWithOptions = voteService.getVotes();
        return new ResponseEntity<>(voteTitleWithOptions, HttpStatus.OK);
    }
}
