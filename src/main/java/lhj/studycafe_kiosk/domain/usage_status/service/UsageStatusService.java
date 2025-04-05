package lhj.studycafe_kiosk.domain.usage_status.service;

import lhj.studycafe_kiosk.domain.usage_status.repository.UsageStatusRepository;
import lhj.studycafe_kiosk.domain.usage_status.domain.UsageStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UsageStatusService {

    private final UsageStatusRepository usageStatusRepository;

    public void recordUsageStatus(UsageStatus usageStatus) {
        usageStatusRepository.saveUsageStatus(usageStatus);
    }
}
