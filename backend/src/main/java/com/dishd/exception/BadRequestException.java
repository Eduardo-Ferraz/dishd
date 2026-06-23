package com.dishd.exception;

/** Lancada para requisicoes invalidas de regra de negocio (resulta em HTTP 400). */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
