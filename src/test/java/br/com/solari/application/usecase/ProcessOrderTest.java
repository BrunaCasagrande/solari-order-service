package br.com.solari.application.usecase;

import br.com.solari.application.domain.Orderstatus;
import br.com.solari.application.dto.*;
import br.com.solari.application.gateway.OrchestrationGateway;
import br.com.solari.application.gateway.OrderGateway;
import br.com.solari.infrastructure.event.OrderEvent;
import br.com.solari.infrastructure.event.PaymentData;
import br.com.solari.infrastructure.event.PaymentMethod;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProcessOrderTest {

  @Mock private OrderGateway orderGateway;
  @Mock private OrchestrationGateway orchestrationGateway;
  @InjectMocks private ProcessOrder processOrder;


  @Test
  void shouldMarkOrderAsErrorWhenClientNotFound() {
    OrderEvent orderEvent = createOrderEvent();

    when(orchestrationGateway.getClientFromClientService(orderEvent.getCpf())).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> processOrder.processOrder(orderEvent));

    verify(orderGateway).saveOrder(orderEvent);
    verify(orderGateway).updateOrder(orderEvent, Orderstatus.ERRO);
  }

  @Test
  void shouldMarkOrderAsErrorWhenProductNotFound() {
    OrderEvent orderEvent = createOrderEvent();
    ClientDTO clientDTO = mock(ClientDTO.class);
    when(clientDTO.getCpf()).thenReturn("12345678900");

    when(orchestrationGateway.getClientFromClientService(orderEvent.getCpf())).thenReturn(Optional.of(clientDTO));
    when(orchestrationGateway.getProductFromProductService("12345")).thenReturn(Optional.empty());

    assertDoesNotThrow(() -> processOrder.processOrder(orderEvent));

    verify(orderGateway).saveOrder(orderEvent);
    verify(orderGateway).updateOrder(orderEvent, Orderstatus.ERRO);
  }

  @Test
  void shouldMarkOrderAsErrorWhenInventoryInsufficient() {
    OrderEvent orderEvent = createOrderEvent();
    ClientDTO clientDTO = mock(ClientDTO.class);
    ProductDTO productDTO = mock(ProductDTO.class);
    InventoryDTO inventoryDTO = mock(InventoryDTO.class);

    when(clientDTO.getCpf()).thenReturn("12345678900");
    when(productDTO.getSku()).thenReturn("12345");
    when(inventoryDTO.getSku()).thenReturn("12345");
    when(inventoryDTO.getQuantity()).thenReturn(1); // menor que a quantidade do pedido

    when(orchestrationGateway.getClientFromClientService(orderEvent.getCpf())).thenReturn(Optional.of(clientDTO));
    when(orchestrationGateway.getProductFromProductService("12345")).thenReturn(Optional.of(productDTO));
    when(orchestrationGateway.getInventoryFromInventoryService("12345")).thenReturn(Optional.of(List.of(inventoryDTO)));

    assertDoesNotThrow(() -> processOrder.processOrder(orderEvent));

    verify(orderGateway).saveOrder(orderEvent);
    verify(orderGateway).updateOrder(orderEvent, Orderstatus.ERRO);
  }

  @Test
  void shouldMarkOrderAsErrorWhenPaymentFails() {
    OrderEvent orderEvent = createOrderEvent();
    ClientDTO clientDTO = mock(ClientDTO.class);
    ProductDTO productDTO = mock(ProductDTO.class);
    InventoryDTO inventoryDTO = mock(InventoryDTO.class);
    PaymentResponseDto paymentResponse = mock(PaymentResponseDto.class);

    when(clientDTO.getCpf()).thenReturn("12345678900");
    when(productDTO.getSku()).thenReturn("12345");
    when(inventoryDTO.getSku()).thenReturn("12345");
    when(inventoryDTO.getQuantity()).thenReturn(10);
    when(paymentResponse.getStatus()).thenReturn("DECLINED");

    when(orchestrationGateway.getClientFromClientService(orderEvent.getCpf())).thenReturn(Optional.of(clientDTO));
    when(orchestrationGateway.getProductFromProductService("12345")).thenReturn(Optional.of(productDTO));
    when(orchestrationGateway.getInventoryFromInventoryService("12345")).thenReturn(Optional.of(List.of(inventoryDTO)));
    when(orchestrationGateway.processPayment(anyString())).thenReturn(paymentResponse);

    processOrder.processOrder(orderEvent);

    verify(orderGateway).saveOrder(orderEvent);
    verify(orderGateway).updateOrder(orderEvent, Orderstatus.FECHADO_COM_SUCESSO);
    verify(orderGateway).updateOrder(orderEvent, Orderstatus.FECHADO_SEM_CREDITO);
  }

  private OrderEvent createOrderEvent() {
    PaymentData paymentData = mock(PaymentData.class);
    doReturn("1234567890123456").when(paymentData).getCreditCardNumber();
    doReturn(PaymentMethod.CREDIT_CARD).when(paymentData).getPaymentMethod();
    return new OrderEvent("order1", Map.of("12345", 2), "12345678900", paymentData);
  }
}
