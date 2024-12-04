package lhj.studycafe_kiosk.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponse {

    private String content;
    private String postDateTime;
    private boolean reflect;
}
