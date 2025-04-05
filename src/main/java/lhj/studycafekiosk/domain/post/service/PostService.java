package lhj.studycafekiosk.domain.post.service;

import lhj.studycafekiosk.domain.post.repository.PostRepository;
import lhj.studycafekiosk.domain.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    public void savePost(Post post) {
        postRepository.savePost(post);
    }

    public void deletePost(Post post) {
        postRepository.hidePost(post);
    }

    public void modifyPost(Post post, String newContent) {
        postRepository.updatePost(post, newContent);
    }

    public void checkPost(Post post) {
        postRepository.updatePostChecked(post);
    }

    public void uncheckPost(Post post) {
        postRepository.updatePostUnchecked(post);
    }
}
