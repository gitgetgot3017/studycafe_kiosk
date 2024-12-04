package lhj.studycafe_kiosk.post;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Post;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository {

    @PersistenceContext
    EntityManager em;

    public List<Post> getPosts() {
        return em.createQuery("select p from Post p", Post.class)
                .getResultList();
    }
}