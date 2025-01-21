/*
 * @(#)ClienteRq.java
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
package cl.bch.technique.test.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClienteRq.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 20-01-2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRq {

    private Long id;

    @NotNull(message = "El RUT es obligatorio")
    @Pattern(regexp = "^[0-9]{7,8}-[0-9kK]{1}$", message = "El RUT debe ser válido")
    private String rut;

    @JsonProperty("first_name")
    @NotNull(message = "El primer nombre es obligatorio")
    @Size(min = 1, message = "El primer nombre no puede estar vacío")
    private String firstName;

    @JsonProperty("last_name")
    @NotNull(message = "El apellido es obligatorio")
    @Size(min = 1, message = "El apellido no puede estar vacío")
    private String lastName;

    @JsonProperty("date_birth")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private String dateBirth;

    @JsonProperty("mobile_phone")
    @NotNull(message = "El teléfono móvil es obligatorio")
    private String mobilePhone;

    @NotNull(message = "El correo electrónico es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "El correo electrónico debe ser válido")
    private String email;

    private String address;

    @JsonProperty("city_id")
    private String cityId;

    @JsonProperty("session_active")
    private boolean sessionActive;

    @NotNull(message = "La password es obligatoria")
    @Size(min = 1, message = "La password no puede estar vacia")
    private String password;
}