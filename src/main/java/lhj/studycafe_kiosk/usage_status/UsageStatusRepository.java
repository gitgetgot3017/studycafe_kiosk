package lhj.studycafe_kiosk.usage_status;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lhj.studycafe_kiosk.domain.UsageStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class UsageStatusRepository {

    @PersistenceContext
    EntityManager em;

    public void saveUsageStatus(UsageStatus usageStatus) {
        em.persist(usageStatus);
    }
}
