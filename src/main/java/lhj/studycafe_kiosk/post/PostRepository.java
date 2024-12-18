package lhj.studycafe_kiosk.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Post;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class PostRepository {

    @PersistenceContext
    EntityManager em;

    public List<Post> getPosts() {
        return em.createQuery("select p from Post p where p.shown = true", Post.class)
                .getResultList();
    }

    public Post getPost(Long postId) {
        return em.find(Post.class, postId);
    }

    public void savePost(Post post) {
        em.persist(post);
    }

    public void hidePost(Post post) {
        post.hidePost();
    }

    public void updatePost(Post post, String newContent) {
        post.changeContent(newContent);
    }

    public void updatePostChecked(Post post) {
        post.checkPost();
    }

    public void updatePostUnchecked(Post post) {
        post.uncheckPost();
    }
}
