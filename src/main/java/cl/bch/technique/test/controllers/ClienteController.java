/*
 * @(#)ClienteController.java
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
package cl.bch.technique.test.controllers;

import cl.bch.technique.test.dtos.ClienteRq;
import cl.bch.technique.test.dtos.ClienteRs;
import cl.bch.technique.test.dtos.ErrorRs;
import cl.bch.technique.test.dtos.LoginRq;
import cl.bch.technique.test.dtos.LoginRs;
import cl.bch.technique.test.exceptions.CustomException;
import cl.bch.technique.test.services.ClienteService;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClienteController.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 20-01-2025
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "cliente", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping("/login")
    public ResponseEntity<Object> obtenerLoginResponse(@RequestBody final @Valid LoginRq loginRq,
        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getValidationErrors(bindingResult));
        }

        try {
            LoginRs loginRs = clienteService.login(loginRq);
            return ResponseEntity.ok(loginRs);
        } catch (CustomException e) {
            ErrorRs errorRs = new ErrorRs();
            errorRs.setCode(e.getCodigo());
            errorRs.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRs);
        }
    }

    @GetMapping("/consulta/{id}")
    public ResponseEntity<Object> consultaCliente(@PathVariable Long id, @RequestHeader("Auth-x") String token) {
        try {
            ClienteRs clienteRs = clienteService.consulta(id, token);
            return ResponseEntity.ok(clienteRs);
        } catch (CustomException e) {
            ErrorRs errorRs = new ErrorRs();
            errorRs.setCode(e.getCodigo());
            errorRs.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRs);
        } catch (JwtException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ClienteRs());
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ClienteRs());
        }
    }

    @PostMapping("/guardar")
    public ResponseEntity<Object> guardarCliente(@Valid @RequestBody ClienteRq clienteRq,
        BindingResult bindingResult, @RequestHeader("Auth-x") String token) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getValidationErrors(bindingResult));
        }

        try {
            clienteService.guardar(clienteRq, token);
            return ResponseEntity.noContent().build();
        } catch (CustomException e) {
            ErrorRs errorRs = new ErrorRs();
            errorRs.setCode(e.getCodigo());
            errorRs.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRs);
        } catch (JwtException e) {
            ErrorRs errorRs = new ErrorRs();
            errorRs.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorRs);
        } catch (AccessDeniedException e) {
            ErrorRs errorRs = new ErrorRs();
            errorRs.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorRs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    private ErrorRs getValidationErrors(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMessage.append(fieldError.getField())
                .append(": ")
                .append(fieldError.getDefaultMessage())
                .append(";");
        }
        return new ErrorRs("E99", errorMessage.toString());
    }

}