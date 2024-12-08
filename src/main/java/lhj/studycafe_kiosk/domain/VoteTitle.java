package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;

@Entity
public class VoteTitle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_title_id")
    private Long id;

    private String title;

    private boolean isMultiple; // 다중선택 가능 여부
}
