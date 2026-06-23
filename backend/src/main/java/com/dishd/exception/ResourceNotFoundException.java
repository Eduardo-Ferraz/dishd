package com.dishd.exception;

/** Lancada quando um recurso solicitado nao existe (resulta em HTTP 404). */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String recurso, Object id) {
        return new ResourceNotFoundException(recurso + " nao encontrado(a): " + id);
    }
}
