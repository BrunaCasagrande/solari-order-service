package br.com.solari.infrastructure.event.messaging;

import br.com.solari.application.usecase.ProcessOrder;
import br.com.solari.infrastructure.event.OrderEvent;
import br.com.solari.infrastructure.event.PaymentData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class OrderConsumerTest {

    @Mock private ProcessOrder processOrder;
    @Mock private ObjectMapper objectMapper;

    private OrderConsumer orderConsumer;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        orderConsumer = new OrderConsumer(processOrder);

        Field field = OrderConsumer.class.getDeclaredField("objectMapper");
        field.setAccessible(true);
        field.set(orderConsumer, objectMapper);
    }

    @Test
    void shouldProcessOrderSuccessfully() throws Exception {
        String json = "{\"id\":\"order1\",\"cpf\":\"12345678900\",\"products\":{\"12345\":2},\"paymentData\":{}}";
        PaymentData paymentData = new PaymentData(null, null);
        OrderEvent expectedOrder = new OrderEvent("order1", Map.of("12345", 2), "12345678900", paymentData);
        ConsumerRecord<String, String> record = mock(ConsumerRecord.class);

        when(record.value()).thenReturn(json);
        when(objectMapper.readValue(json, OrderEvent.class)).thenReturn(expectedOrder);

        orderConsumer.consumirPedido(record);

        ArgumentCaptor<OrderEvent> captor = ArgumentCaptor.forClass(OrderEvent.class);
        verify(processOrder).processOrder(captor.capture());

        OrderEvent actual = captor.getValue();
        assertEquals(expectedOrder.getId(), actual.getId());
        assertEquals(expectedOrder.getCpf(), actual.getCpf());
        assertEquals(expectedOrder.getProducts(), actual.getProducts());
        assertNotNull(actual.getPaymentData());
    }

    @Test
    void shouldHandleExceptionWhenProcessingFails() throws Exception {
        String json = "{\"id\":\"order1\",\"cpf\":\"12345678900\",\"products\":{\"12345\":2},\"paymentData\":{}}";
        ConsumerRecord<String, String> record = mock(ConsumerRecord.class);

        when(record.value()).thenReturn(json);
        when(objectMapper.readValue(json, OrderEvent.class)).thenThrow(new RuntimeException("Error parsing JSON"));

        orderConsumer.consumirPedido(record);

        verify(processOrder, never()).processOrder(any());
    }
}
