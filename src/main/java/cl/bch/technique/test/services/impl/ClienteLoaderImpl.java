/*
 * @(#)ClientesFixture.java
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
package cl.bch.technique.test.services.impl;

import cl.bch.technique.test.dtos.ClienteRq;
import cl.bch.technique.test.services.ClienteLoader;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * ClienteLoaderImpl.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 20-01-2025
 */
@Service
public class ClienteLoaderImpl implements ClienteLoader {

    public List<ClienteRq> cargarClientes() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("json/clientes.json");
        if (inputStream == null) {
            throw new IOException("No se pudo encontrar el archivo json/clientes.json");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(inputStream, new TypeReference<List<ClienteRq>>(){});
    }
}