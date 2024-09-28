package lhj.studycafe_kiosk.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepository {

    @PersistenceContext
    EntityManager em;

    public void saveItem(Item item) {
        em.persist(item);
    }

    public List<Item> getExistItemName(String itemName) {
        return em.createQuery("select i from Item i where i.itemName = :itemName", Item.class)
                .setParameter("itemName", itemName)
                .getResultList();
    }

    public Item getItem(Long itemId) {
        return em.find(Item.class, itemId);
    }

    public List<Item> getItems() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
