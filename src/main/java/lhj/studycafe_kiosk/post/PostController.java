package lhj.studycafe_kiosk.post;


import lhj.studycafe_kiosk.domain.Post;
import lhj.studycafe_kiosk.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostRepository postRepository;

    @GetMapping
    public HttpEntity<List<PostResponse>> getPosts() {

        List<Post> posts = postRepository.getPosts();

        List<PostResponse> postResponses = changeAllPostToPostResponse(posts);
        return new ResponseEntity<>(postResponses, HttpStatus.OK);
    }

    private List<PostResponse> changeAllPostToPostResponse(List<Post> posts) {

        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post : posts) {
            postResponses.add(changePostToPostResponse(post));
        }
        return postResponses;
    }

    private PostResponse changePostToPostResponse(Post post) {
        return new PostResponse(post.getContent(), getFormattedEndDateTime(post.getPostDateTime()), post.isReflect());
    }

    private String getFormattedEndDateTime(LocalDateTime dateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return dateTime.format(formatter);
    }
}
