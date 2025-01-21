/*
 * @(#)CustomException.java
 *
 * Copyright (c) BANCO DE CHILE (Chile). All rights reserved.
 *
 * All rights to this product are owned by BANCO DE CHILE and may only
 * be used under the terms of its associated license document. You may NOT
 * copy, modify, sublicense, or distribute this source file or portions of
 * it unless previously authorized in writing by BANCO DE CHILE.
 * In any event, this notice and the above copyright must always be included
 * verbatim with this file.
 */
package cl.bch.technique.test.exceptions;

/**
 * CustomException.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 20-01-2025
 */
public class CustomException extends RuntimeException {

    private final String codigo;
    private final String mensaje;

    public CustomException(String codigo, String mensaje) {
        super(mensaje);
        this.mensaje = mensaje;
        this.codigo = codigo;
    }

    public CustomException(String codigo, String mensaje, Throwable t) {
        super(mensaje, t);
        this.mensaje = mensaje;
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getMensaje() {
        return mensaje;
    }
}