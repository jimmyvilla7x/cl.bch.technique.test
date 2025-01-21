/*
 * @(#)ClienteService.java
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
package cl.bch.technique.test.services;

import cl.bch.technique.test.dtos.ClienteRq;
import cl.bch.technique.test.dtos.ClienteRs;
import cl.bch.technique.test.dtos.LoginRq;
import cl.bch.technique.test.dtos.LoginRs;

/**
 * ClienteService.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 20-01-2025
 */
public interface ClienteService {

    LoginRs login(LoginRq loginRq);

    ClienteRs consulta(Long id, String token);

    void guardar(ClienteRq clienteRq, String token);
}