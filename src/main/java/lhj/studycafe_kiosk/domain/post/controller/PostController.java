package lhj.studycafe_kiosk.domain.post.controller;


import lhj.studycafe_kiosk.domain.member.domain.Member;
import lhj.studycafe_kiosk.domain.post.repository.PostRepository;
import lhj.studycafe_kiosk.domain.post.service.PostService;
import lhj.studycafe_kiosk.domain.post.dto.PostSuccessResponse;
import lhj.studycafe_kiosk.domain.post.domain.Post;
import lhj.studycafe_kiosk.domain.member.repository.MemberRepository;
import lhj.studycafe_kiosk.domain.post.dto.ModifyPostRequest;
import lhj.studycafe_kiosk.domain.post.dto.PostRequest;
import lhj.studycafe_kiosk.domain.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
        return new PostResponse(post.getId(), post.getContent(), getFormattedEndDateTime(post.getPostDateTime()), post.isReflect(), post.getMember().getId().equals(memberId));
    }

    private String getFormattedEndDateTime(LocalDateTime dateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
        return dateTime.format(formatter);
    }

    @PostMapping
    public HttpEntity<PostSuccessResponse> post(@SessionAttribute("loginMember") Long memberId, @RequestBody PostRequest postRequest) {

        Member member = memberRepository.getMember(memberId);
        Post post = new Post(member, postRequest.getContent(), LocalDateTime.now(), false, true);
        postService.savePost(post);

        PostSuccessResponse postSuccessResponse = new PostSuccessResponse("게시글이 정상적으로 등록되었습니다.");
        return new ResponseEntity(postSuccessResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable("postId") Long postId) {
        Post post = postRepository.getPost(postId);
        postService.deletePost(post);
    }

    @PatchMapping("/{postId}")
    public void modifyPost(@PathVariable("postId") Long postId, @RequestBody @Validated ModifyPostRequest modifyPostRequest) {

        Post post = postRepository.getPost(postId);
        postService.modifyPost(post, modifyPostRequest.getContent());
    }

    @PostMapping("/{postId}/check")
    public void checkPost(@PathVariable("postId") Long postId) {

        Post post = postRepository.getPost(postId);
        postService.checkPost(post);
    }

    @PostMapping("/{postId}/uncheck")
    public void uncheckPost(@PathVariable("postId") Long postId) {

        Post post = postRepository.getPost(postId);
        postService.uncheckPost(post);
    }
}
