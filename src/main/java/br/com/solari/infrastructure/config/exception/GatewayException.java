package br.com.solari.infrastructure.config.exception;

import lombok.Getter;

import java.util.List;

public class GatewayException extends RuntimeException {
  private static final String DEFAULT_CODE = "gateway_exception";
  private final String code;

  public GatewayException(final String message) {
    super(message);
    this.code = DEFAULT_CODE;
  }

    @Getter
    public static class DomainException extends RuntimeException {

      private final String errorCode;
      private final String message;
      private final List<ErrorDetail> errors;

      public DomainException(final String message, final String errorCode, List<ErrorDetail> errors) {
        super(message);

        this.message = message;
        this.errorCode = errorCode;
        this.errors = errors;
      }
    }

    public record ErrorDetail(String field, String errorMessage, Object rejectedValue) {}
}
