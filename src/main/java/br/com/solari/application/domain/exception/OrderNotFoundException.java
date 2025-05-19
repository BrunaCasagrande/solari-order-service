package br.com.solari.application.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends BusinessException {

  private static final String MESSAGE = "Order with ID=[%s] not found.";
  private static final String ERROR_CODE = String.valueOf(HttpStatus.NOT_FOUND);

  public OrderNotFoundException(final String id) {
    super(String.format(MESSAGE, id), ERROR_CODE);
  }
}
