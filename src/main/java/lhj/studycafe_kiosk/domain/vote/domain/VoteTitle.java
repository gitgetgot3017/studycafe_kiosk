package lhj.studycafe_kiosk.domain.vote.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class VoteTitle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_title_id")
    private Long id;

    private String title;

    private boolean isMultiple; // 다중선택 가능 여부

    public VoteTitle(String title, boolean isMultiple) {
        this.title = title;
        this.isMultiple = isMultiple;
    }
}
