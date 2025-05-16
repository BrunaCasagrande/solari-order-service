package br.com.solari.infrastructure.controller;

import static br.com.solari.fixture.OrderFixture.ID;
import static br.com.solari.fixture.OrderFixture.createOrderPresenterResponseExemple;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.solari.application.domain.exception.OrderNotFoundException;
import br.com.solari.application.usecase.SearchOrder;
import br.com.solari.fixture.OrderFixture;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private SearchOrder searchOrder;
  private String BASE_URL = "/solari/v1/order/";

  @Test
  void shouldFindOrderByIdSuccessfully() throws Exception {
    final var response = createOrderPresenterResponseExemple();
    final var order = OrderFixture.createOrderExemple();

    when(searchOrder.getOrder(ID)).thenReturn(Optional.of(order));
    mockMvc
        .perform(get(BASE_URL + "/{id}", ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(response.id()))
        .andExpect(jsonPath("$.status").value(response.status()))
        .andExpect(jsonPath("$.products.2").value(3))
        .andExpect(jsonPath("$.products.1").value(2))
        .andExpect(jsonPath("$.cpf").value(response.cpf()));
    verify(searchOrder).getOrder(ID);
  }

  @Test
  void shouldReturnNotFoundWhenOrderDoesNotExist() throws Exception {
    when(searchOrder.getOrder(ID)).thenThrow(new OrderNotFoundException(ID));

    mockMvc
        .perform(get(BASE_URL + "/{id}", ID).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Order with ID=[" + ID + "] not found."));

    verify(searchOrder).getOrder(ID);
  }
}
