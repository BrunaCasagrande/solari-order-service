package br.com.solari.application.usecase;

import br.com.solari.application.domain.Orderstatus;
import br.com.solari.application.gateway.OrchestrationGateway;
import br.com.solari.application.gateway.OrderGateway;
import br.com.solari.fixture.OrderFixture;
import br.com.solari.infrastructure.event.OrderEvent;
import br.com.solari.application.dto.ClientDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ProcessOrderTest {

    private final OrderGateway orderGateway = mock(OrderGateway.class);
    private final OrchestrationGateway orchestrationGateway = mock(OrchestrationGateway.class);

    private final ProcessOrder processOrder = new ProcessOrder(orderGateway, orchestrationGateway);

    @Test
    void shouldProcessOrderSuccessfully() {
        final OrderEvent orderEvent = OrderFixture.createOrderEventExemple();
        final ClientDTO clientDTO = OrderFixture.createClientDTOExemple();

        doNothing().when(orderGateway).saveOrder(orderEvent);
        when(orchestrationGateway.getClientFromClientService(orderEvent.getCpf()))
                .thenReturn(Optional.of(clientDTO));

        processOrder.processOrder(orderEvent);

        verify(orderGateway, times(1)).saveOrder(orderEvent);
        verify(orchestrationGateway, times(1)).getClientFromClientService(orderEvent.getCpf());
        verify(orderGateway, never()).updateOrder(any(OrderEvent.class), any());

    }
    @Test
    void shouldMarkOrderForReprocessingWhenExceptionOccurs() {
        final OrderEvent orderEvent = OrderFixture.createOrderEventExemple();
        doNothing().when(orderGateway).saveOrder(orderEvent);

        when(orchestrationGateway.getClientFromClientService(orderEvent.getCpf()))
                .thenThrow(new RuntimeException("Erro no serviço"));

        processOrder.processOrder(orderEvent);

        verify(orderGateway, times(1)).saveOrder(orderEvent);
        verify(orchestrationGateway, times(1)).getClientFromClientService(orderEvent.getCpf());
        verify(orderGateway, times(1)).updateOrder(orderEvent, Orderstatus.ERRO);
    }


}
