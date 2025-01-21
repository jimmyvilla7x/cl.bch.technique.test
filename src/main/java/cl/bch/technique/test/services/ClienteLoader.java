/*
 * @(#)ClienteLoader.java
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
import java.io.IOException;
import java.util.List;

/**
 * ClienteLoader.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 21-01-2025
 */
public interface ClienteLoader {
    List<ClienteRq> cargarClientes() throws IOException;
}