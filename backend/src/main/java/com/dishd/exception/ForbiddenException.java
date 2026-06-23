package com.dishd.exception;

/** Lancada quando o usuario nao tem permissao para a acao (resulta em HTTP 403). */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException(String message) {
        super(message);
    }
}
