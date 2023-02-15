package shop.yesaladin.delivery.scheduler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import shop.yesaladin.delivery.transport.domain.model.Transport;
import shop.yesaladin.delivery.transport.service.inter.TransportService;

/**
 * Shop API 서버의 주문 상태 변경 이력 테이블에 배송 완료 상태를 추가하기 위한 스케줄러 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class OrderStatusChangeScheduler {

    private final TransportService transportService;
    private final RestTemplate restTemplate;
    private ThreadPoolTaskScheduler scheduler;
    private static final String EXECUTE_CRON = "0 0/1 * * * ?";

    private static final Set<Long> orderIdSet = Collections.synchronizedSet(new HashSet<>());

    private Runnable getRunnable() {
        return () -> {
            Transport transport = transportService.getLatestTransport();
            if (Objects.nonNull(transport) && !orderIdSet.contains(transport.getOrderId())) {
                // restTemplate call
                log.info("latest transport orderId={}", transport.getOrderId());
                log.info("status={}", transport.getTransportStatusCode());
                orderIdSet.add(transport.getOrderId());
            }
        };
    }

    public void startScheduler() {
        this.scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        scheduler.setWaitForTasksToCompleteOnShutdown(false);
        scheduler.schedule(getRunnable(), getTrigger());
    }

    public void stopScheduler() {
        this.scheduler.shutdown();
    }

    private Trigger getTrigger() {
        return new CronTrigger(EXECUTE_CRON);
    }

    @PostConstruct
    public void init() {
        startScheduler();
    }

    @PreDestroy
    public void destroy() {
        stopScheduler();
    }
}
