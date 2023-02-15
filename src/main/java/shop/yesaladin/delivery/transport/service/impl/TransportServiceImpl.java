package shop.yesaladin.delivery.transport.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.yesaladin.delivery.transport.domain.model.Transport;
import shop.yesaladin.delivery.transport.domain.model.TransportStatusCode;
import shop.yesaladin.delivery.transport.domain.repository.TransportRepository;
import shop.yesaladin.delivery.transport.dto.TransportResponseDto;
import shop.yesaladin.delivery.transport.exception.TransportNotFoundException;
import shop.yesaladin.delivery.transport.service.inter.TransportService;

/**
 * 배송 도메인의 Service 구현체 입니다.
 *
 * @author 송학현
 * @since 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TransportServiceImpl implements TransportService {

    private final TransportRepository transportRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TransportResponseDto registerTransport(Long orderId) {
        String trackingNo = UUID.randomUUID().toString();

        Transport transport = Transport.builder()
                .receptionDatetime(LocalDate.now())
                .orderId(orderId)
                .trackingNo(trackingNo)
                .transportStatusCode(TransportStatusCode.INPROGRESS)
                .build();
        Transport savedTransport = transportRepository.save(transport);

        return TransportResponseDto.fromEntity(savedTransport);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public TransportResponseDto completeTransport(Long transportId) {
        Transport transport = getTransport(transportId);
        transport.completeTransport();
        return TransportResponseDto.fromEntity(transport);
    }

    private Transport getTransport(Long transportId) {
        return transportRepository.findById(transportId)
                .orElseThrow(() -> new TransportNotFoundException(transportId));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<TransportResponseDto> findAll() {
        return transportRepository.findAll().stream()
                .map(TransportResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public TransportResponseDto findById(Long transportId) {
        Transport transport = getTransport(transportId);

        return TransportResponseDto.fromEntity(transport);
    }
}
