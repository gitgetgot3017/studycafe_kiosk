package lhj.studycafe_kiosk.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

    private LocalDateTime postDateTime;

    private boolean reflect;

    public Post(Member member, String content, LocalDateTime postDateTime, boolean reflect) {
        this.member = member;
        this.content = content;
        this.postDateTime = postDateTime;
        this.reflect = reflect;
    }
}
