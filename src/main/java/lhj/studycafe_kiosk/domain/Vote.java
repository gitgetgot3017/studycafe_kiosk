package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;

@Entity
public class Vote {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "vote_title_id")
    private VoteTitle voteTitle;

    @OneToOne
    @JoinColumn(name = "vote_option_id")
    private VoteOption voteOption;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
