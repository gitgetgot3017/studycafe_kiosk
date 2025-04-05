package lhj.studycafekiosk.domain.usagestatus.service;

import lhj.studycafekiosk.domain.usagestatus.repository.UsageStatusRepository;
import lhj.studycafekiosk.domain.usagestatus.domain.UsageStatus;
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
