/*
 * @(#)ClienteConfig.java
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
package cl.bch.technique.test.config;

import cl.bch.technique.test.dtos.ClienteRq;
import cl.bch.technique.test.services.ClienteLoader;
import java.io.IOException;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClienteConfig.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 21-01-2025
 */
@Configuration
public class ClienteConfig {

    private final ClienteLoader clienteLoader;

    public ClienteConfig(ClienteLoader clienteLoader) {
        this.clienteLoader = clienteLoader;
    }

    @Bean
    public List<ClienteRq> clientes() throws IOException {
        return clienteLoader.cargarClientes();
    }
}