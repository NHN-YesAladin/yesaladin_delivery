package shop.yesaladin.delivery.transport.domain.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.yesaladin.delivery.transport.domain.model.Transport;

/**
 * Transport 테이블에 QueryDSL로 접근하기 위한 Repository의 구현체 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
public class TransportRepositoryCustomImpl extends QuerydslRepositorySupport implements TransportRepositoryCustom {

    public TransportRepositoryCustomImpl() {
        super(Transport.class);
    }


}
