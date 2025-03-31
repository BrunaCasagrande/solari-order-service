package br.com.solari.application.domain.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {
    private final String errorCode;

    public BusinessException(final String message, final String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
