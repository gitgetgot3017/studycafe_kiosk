package lhj.studycafe_kiosk.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponse {

    private String content;
    private String postDateTime;
    private boolean reflect;
}
