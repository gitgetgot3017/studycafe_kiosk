package lhj.studycafe_kiosk.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.Item;
import lhj.studycafe_kiosk.domain.ItemType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    public List<Item> getItems(ItemType itemType) {
        return em.createQuery("select i from Item i where i.itemType = :itemType")
                .setParameter("itemType", itemType)
                .getResultList();
    }

    public Long updateItemInfo(Long itemId, Item changedItem) {

        Item item = em.find(Item.class, itemId);
        item.changeItem(changedItem);
        return item.getId();
    }

    public Optional<Item> getItem(Long itemId) {
        Item item = em.find(Item.class, itemId);
        return Optional.ofNullable(item);
    }

    public List<Item> getItemType() {
        return em.createQuery("select i from Item i group by i.itemType", Item.class)
                .getResultList();
    }

    public void deleteItem(Item item) {
        em.remove(item);
    }

    public void deleteCategory(ItemType itemType) {
        em.createQuery("delete from Item i where i.itemType = :itemType")
                .setParameter("itemType", itemType)
                .executeUpdate();
    }
}
