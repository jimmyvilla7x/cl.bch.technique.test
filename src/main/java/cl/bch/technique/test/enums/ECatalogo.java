/*
 * @(#)ECatalogo.java
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
package cl.bch.technique.test.enums;

/**
 * ECatalogo.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 20-01-2025
 */
public enum ECatalogo {
    RUT_NO_EXISTE("E001", "El RUT no existe."),
    CONTRASENA_INVALIDA("E002", "La contraseña es inválida."),
    CLIENTE_BLOQUEADO("E003", "El cliente está bloqueado debido a múltiples intentos fallidos."),
    RUT_Y_CONTRASENA_REQUERIDOS("E004", "El RUT y la contraseña son requeridos."),
    JWT_INVALIDO("E005", "El token JWT es inválido."),
    ETAPA_INVALIDA("E006", "La etapa no corresponde."),
    ERROR_CARGAR_CLIENTES("E007", "No se pudo cargar la lista de clientes."),
    ERROR_ID_CLIENTE("E008", "El id de usuario no existe"),
    ERROR_RUT_DUPLICADO("E009", "El rut ya existe"),
    ERROR_DESCONOCIDO("E100", "Ha ocurrido un error inesperado.");

    private final String code;
    private final String message;

    ECatalogo(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}