/*
 * @(#)ClienteRs.java
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClienteRs.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 20-01-2025
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteRs {

    private Long id;
    private String rut;
    private String firstName;
    private String lastName;
    private String dateBirth;
    private String mobilePhone;
    private String email;
    private String address;
    private String cityId;
    private boolean sessionActive;
    private String nuevoToken;
}