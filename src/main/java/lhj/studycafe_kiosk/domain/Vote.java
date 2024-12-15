package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Vote {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vote_title_id")
    private VoteTitle voteTitle;

    @ManyToOne
    @JoinColumn(name = "vote_option_id")
    private VoteOption voteOption;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    public Vote(VoteTitle voteTitle, VoteOption voteOption, Member member) {
        this.voteTitle = voteTitle;
        this.voteOption = voteOption;
        this.member = member;
    }
}
