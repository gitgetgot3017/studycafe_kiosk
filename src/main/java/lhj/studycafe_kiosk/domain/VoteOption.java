package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class VoteOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_option_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vote_title_id")
    private VoteTitle voteTitle;

    private String content;
}
