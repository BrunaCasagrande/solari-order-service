package br.com.solari.application.usecase;

import br.com.solari.application.domain.Orderstatus;
import br.com.solari.application.gateway.OrchestrationGateway;
import br.com.solari.application.gateway.OrderGateway;
import br.com.solari.fixture.OrderFixture;
import br.com.solari.infrastructure.event.OrderEvent;
import br.com.solari.infrastructure.presenter.dto.ClientDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static br.com.solari.fixture.OrderFixture.CPF;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProcessOrderUseCaseTest {

    private final OrderGateway orderGateway = mock(OrderGateway.class);
    private final OrchestrationGateway orchestrationGateway = mock(OrchestrationGateway.class);

    private final ProcessOrderUseCase processOrderUseCase = new ProcessOrderUseCase(orderGateway, orchestrationGateway);

    @Test
    void shouldProcessOrderSuccessfully() {
        final OrderEvent orderEvent = OrderFixture.existingOrderEvent();
        final ClientDTO clientDTO = OrderFixture.existingClientDTO();

        doNothing().when(orderGateway).saveOrder(orderEvent);
        when(orchestrationGateway.getClientFromClientService(orderEvent.getCpf()))
                .thenReturn(Optional.of(clientDTO));

        processOrderUseCase.processOrder(orderEvent);

        verify(orderGateway, times(1)).saveOrder(orderEvent);
        verify(orchestrationGateway, times(1)).getClientFromClientService(orderEvent.getCpf());
        verify(orderGateway, never()).updateOrder(any(OrderEvent.class), any());

    }
    @Test
    void shouldMarkOrderForReprocessingWhenExceptionOccurs() {
        final OrderEvent orderEvent = OrderFixture.existingOrderEvent();
        doNothing().when(orderGateway).saveOrder(orderEvent);

        when(orchestrationGateway.getClientFromClientService(orderEvent.getCpf()))
                .thenThrow(new RuntimeException("Erro no serviço"));

        processOrderUseCase.processOrder(orderEvent);

        verify(orderGateway, times(1)).saveOrder(orderEvent);
        verify(orchestrationGateway, times(1)).getClientFromClientService(orderEvent.getCpf());
        verify(orderGateway, times(1)).updateOrder(orderEvent, Orderstatus.ERRO);
    }


}
