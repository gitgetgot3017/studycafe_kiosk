package lhj.studycafe_kiosk.chat;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.ChatMember;
import lhj.studycafe_kiosk.domain.ChatMessage;
import lhj.studycafe_kiosk.domain.Member;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ChatRepository {

    @PersistenceContext
    EntityManager em;

    public ChatMember findChatMember(Member member) {
        return em.createQuery("select c from ChatMember c where c.member = :member", ChatMember.class)
                .setParameter("member", member)
                .getSingleResult();
    }

    public void saveChatMember(ChatMember chatMember) {
        em.persist(chatMember);
    }

    public void saveEnterMessage(ChatMessage chatMessage) {
        em.persist(chatMessage);
    }

    public List<ChatMessage> findChatMember(ChatMember chatMember) {
        return em.createQuery("select c from ChatMessage c where c.sentDateTime >= :enterDateTime order by c.sentDateTime", ChatMessage.class)
                .setParameter("enterDateTime", chatMember.getEnterDateTime())
                .getResultList();
    }
}
