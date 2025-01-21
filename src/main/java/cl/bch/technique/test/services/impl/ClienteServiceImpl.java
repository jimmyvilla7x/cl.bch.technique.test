/*
 * @(#)ClienteServiceImpl.java
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
import cl.bch.technique.test.dtos.ClienteRs;
import cl.bch.technique.test.dtos.LoginRq;
import cl.bch.technique.test.dtos.LoginRs;
import cl.bch.technique.test.enums.ECatalogo;
import cl.bch.technique.test.exceptions.CustomException;
import cl.bch.technique.test.services.ClienteLoader;
import cl.bch.technique.test.services.ClienteService;
import cl.bch.technique.test.util.JwtUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClienteServiceImpl.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 20-01-2025
 */
@Slf4j
@Service
public class ClienteServiceImpl implements ClienteService {
    private final JwtUtil jwtUtil;
    private final ClienteLoader clienteLoader;
    private final List<ClienteRq> clientes;
    Map<String, Integer> failedAttempts = new HashMap<>();
    Map<String, Boolean> lockedUsers = new HashMap<>();
    final int MAX_FAILED_ATTEMPTS = 3;

    @Autowired
    public ClienteServiceImpl(JwtUtil jwtUtil, ClienteLoader clienteLoader, List<ClienteRq> clientes) {
        this.jwtUtil = jwtUtil;
        this.clienteLoader = clienteLoader;
        this.clientes = clientes;  // Los clientes se inyectan desde el bean
    }

    @Override
    public LoginRs login(LoginRq loginRq) {

        String rut = loginRq.getRut();
        if (lockedUsers.getOrDefault(rut, false)) {
            throw new CustomException(ECatalogo.CLIENTE_BLOQUEADO.getCode(), ECatalogo.CLIENTE_BLOQUEADO.getMessage());
        }

        ClienteRq cliente = clientes.stream()
            .filter(c -> c.getRut().equals(rut))
            .findFirst()
            .orElseThrow(() -> new CustomException(ECatalogo.RUT_NO_EXISTE.getCode(), ECatalogo.RUT_NO_EXISTE.getMessage()));

        if (!cliente.getPassword().equals(loginRq.getPassword())) {
            int attempts = failedAttempts.getOrDefault(rut, 0) + 1;
            failedAttempts.put(rut, attempts);

            if (attempts >= MAX_FAILED_ATTEMPTS) {
                lockedUsers.put(rut, true);
                throw new CustomException(ECatalogo.CLIENTE_BLOQUEADO.getCode(), ECatalogo.CLIENTE_BLOQUEADO.getMessage());
            }

            throw new CustomException(ECatalogo.CONTRASENA_INVALIDA.getCode(), ECatalogo.CONTRASENA_INVALIDA.getMessage());
        }



        failedAttempts.put(rut, 0);

        boolean bloqueo = !cliente.isSessionActive();
        String siguienteEtapa = "consulta_cliente";

        String token = jwtUtil.generateToken(cliente.getRut(), 1, bloqueo, siguienteEtapa);

        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(ECatalogo.JWT_INVALIDO.getCode(), ECatalogo.JWT_INVALIDO.getMessage());
        }

        return new LoginRs(token, bloqueo, siguienteEtapa);
    }



    @Override
    public ClienteRs consulta(Long id, String token) {

        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(ECatalogo.JWT_INVALIDO.getCode(), ECatalogo.JWT_INVALIDO.getMessage());
        }

        if (!"consulta_cliente".equals(jwtUtil.getSiguienteEtapaFromToken(token))) {
            throw new CustomException(ECatalogo.ETAPA_INVALIDA.getCode(), ECatalogo.ETAPA_INVALIDA.getMessage());
        }

        ClienteRq cliente = clientes.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new CustomException(ECatalogo.ERROR_ID_CLIENTE.getCode(), ECatalogo.ERROR_ID_CLIENTE.getMessage()));

        String nuevoToken = jwtUtil.generateToken(cliente.getRut(), 1, false, "guardar_cliente");

        return new ClienteRs(
            cliente.getId(),
            cliente.getRut(),
            cliente.getFirstName(),
            cliente.getLastName(),
            cliente.getDateBirth(),
            cliente.getMobilePhone(),
            cliente.getEmail(),
            cliente.getAddress(),
            cliente.getCityId(),
            cliente.isSessionActive(),
            nuevoToken
        );
    }

    @Override
    public void guardar(ClienteRq clienteRq, String token) {

        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(ECatalogo.JWT_INVALIDO.getCode(), ECatalogo.JWT_INVALIDO.getMessage());
        }

        if (!"guardar_cliente".equals(jwtUtil.getSiguienteEtapaFromToken(token))) {
            throw new CustomException(ECatalogo.ETAPA_INVALIDA.getCode(), ECatalogo.ETAPA_INVALIDA.getMessage());
        }

        if (clientes.stream().anyMatch(c -> c.getRut().equals(clienteRq.getRut()))) {
            throw new CustomException(ECatalogo.ERROR_RUT_DUPLICADO.getCode(), ECatalogo.ERROR_RUT_DUPLICADO.getMessage());
        }

        log.info("Cliente con RUT {} guardado exitosamente.", clienteRq.getRut());
    }

}