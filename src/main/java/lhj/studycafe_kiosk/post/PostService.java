package lhj.studycafe_kiosk.post;

import lhj.studycafe_kiosk.domain.Post;
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
}
