package lhj.studycafekiosk.domain.vote.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class VoteOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_option_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vote_title_id")
    private VoteTitle voteTitle;

    private String content;

    public VoteOption(VoteTitle voteTitle, String content) {
        this.voteTitle = voteTitle;
        this.content = content;
    }
}
