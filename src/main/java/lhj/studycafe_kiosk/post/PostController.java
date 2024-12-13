package lhj.studycafe_kiosk.post;


import lhj.studycafe_kiosk.domain.Member;
import lhj.studycafe_kiosk.domain.Post;
import lhj.studycafe_kiosk.member.MemberRepository;
import lhj.studycafe_kiosk.post.dto.PostRequest;
import lhj.studycafe_kiosk.post.dto.PostResponse;
import lhj.studycafe_kiosk.post.dto.PostSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @GetMapping
    public HttpEntity<List<PostResponse>> getPosts(@SessionAttribute(value = "loginMember", required = false) Long memberId) {

        List<Post> posts = postRepository.getPosts();

        List<PostResponse> postResponses = changeAllPostToPostResponse(posts, memberId);
        return new ResponseEntity<>(postResponses, HttpStatus.OK);
    }

    private List<PostResponse> changeAllPostToPostResponse(List<Post> posts, Long memberId) {

        List<PostResponse> postResponses = new ArrayList<>();
        for (Post post : posts) {
            postResponses.add(changePostToPostResponse(post, memberId));
        }
        return postResponses;
    }

    private PostResponse changePostToPostResponse(Post post, Long memberId) {
        return new PostResponse(post.getContent(), getFormattedEndDateTime(post.getPostDateTime()), post.isReflect(), post.getMember().getId().equals(memberId));
    }

    private String getFormattedEndDateTime(LocalDateTime dateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return dateTime.format(formatter);
    }

    @PostMapping
    public HttpEntity<PostSuccessResponse> post(@SessionAttribute("loginMember") Long memberId, @RequestBody PostRequest postRequest) {

        Member member = memberRepository.getMember(memberId);
        Post post = new Post(member, postRequest.getContent(), LocalDateTime.now(), false);
        postService.savePost(post);

        PostSuccessResponse postSuccessResponse = new PostSuccessResponse("게시글이 정상적으로 등록되었습니다.");
        return new ResponseEntity(postSuccessResponse, HttpStatus.CREATED);
    }
}
