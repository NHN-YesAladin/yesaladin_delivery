package shop.yesaladin.delivery.transport.domain.repository;

import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import shop.yesaladin.delivery.transport.domain.model.Transport;

/**
 * Transport 테이블에 QueryDSL로 접근하기 위한 Repository Interface 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
@NoRepositoryBean
public interface TransportRepositoryCustom {

    /**
     * 주문 번호를 기준 로 배송을 조회 합니다.
     *
     * @param orderId 배송의 주문 번호 입니다.
     * @return 주문 번호로 조회한 배송 엔티티
     * @author 송학현
     * @since 1.0
     */
    Optional<Transport> findByOrderId(Long orderId);
}
