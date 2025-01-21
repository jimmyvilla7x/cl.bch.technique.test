package cl.bch.technique.test.controllers;

import cl.bch.technique.test.dtos.ClienteRq;
import cl.bch.technique.test.dtos.ClienteRs;
import cl.bch.technique.test.dtos.LoginRq;
import cl.bch.technique.test.dtos.LoginRs;
import cl.bch.technique.test.dtos.ErrorRs;
import cl.bch.technique.test.exceptions.CustomException;
import cl.bch.technique.test.services.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TestClienteController.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 20-01-2025
 */

@ExtendWith(MockitoExtension.class)
public class TestClienteController {

    @InjectMocks
    private ClienteController clienteController;

    @Mock
    private ClienteService clienteService;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testObtenerLoginResponseExitoso() {
        LoginRq loginRq = new LoginRq("12508387-0", "password");
        LoginRs loginRs = new LoginRs("mockToken", false, "consulta_cliente");

        when(clienteService.login(loginRq)).thenReturn(loginRs);

        ResponseEntity<Object> response = clienteController.obtenerLoginResponse(loginRq, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof LoginRs);
        LoginRs responseBody = (LoginRs) response.getBody();
        assertEquals("mockToken", responseBody.getToken());
    }

    @Test
    void testObtenerLoginResponseConErrorDeContrasena() {
        LoginRq loginRq = new LoginRq("12508387-0", "wrongPassword");

        when(clienteService.login(loginRq)).thenThrow(new CustomException("CONTRASENA_INVALIDA", "Contraseña inválida"));

        ResponseEntity<Object> response = clienteController.obtenerLoginResponse(loginRq, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorRs);
        ErrorRs errorRs = (ErrorRs) response.getBody();
        assertEquals("CONTRASENA_INVALIDA", errorRs.getCode());
        assertEquals("Contraseña inválida", errorRs.getMessage());
    }

    @Test
    void testObtenerLoginResponseConErrorJWT() {
        LoginRq loginRq = new LoginRq("12508387-0", "password");

        when(clienteService.login(loginRq)).thenThrow(new CustomException("JWT_INVALIDO", "JWT inválido"));

        ResponseEntity<Object> response = clienteController.obtenerLoginResponse(loginRq, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorRs);
        ErrorRs errorRs = (ErrorRs) response.getBody();
        assertEquals("JWT_INVALIDO", errorRs.getCode());
        assertEquals("JWT inválido", errorRs.getMessage());
    }

    @Test
    void testConsultaClienteConTokenValido() {
        Long clienteId = 1L;
        String token = "validToken";

        String rut = "12508387-0";
        String firstName = "test";
        String lastName = "test";
        String dateBirth = "1996-05-10";
        String mobilePhone = "213212656";
        String email = "test@gmail.com";
        String address = null;
        String cityId = null;
        boolean sessionActive = true;
        String password = "1234";

        ClienteRs clienteRs = new ClienteRs(clienteId, rut, firstName, lastName, dateBirth, mobilePhone, email, address, cityId, sessionActive, password);

        when(clienteService.consulta(clienteId, token)).thenReturn(clienteRs);

        ResponseEntity<Object> response = clienteController.consultaCliente(clienteId, token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof ClienteRs);
        ClienteRs responseBody = (ClienteRs) response.getBody();
        assertEquals(clienteId, responseBody.getId());
    }

    @Test
    void testConsultaClienteConTokenInvalido() {
        Long clienteId = 1L;
        String token = "invalidToken";

        when(clienteService.consulta(clienteId, token)).thenThrow(new CustomException("JWT_INVALIDO", "JWT inválido"));

        ResponseEntity<Object> response = clienteController.consultaCliente(clienteId, token);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorRs); // Cambiar ClienteRs a ErrorRs
        ErrorRs errorRs = (ErrorRs) response.getBody();
        assertEquals("JWT_INVALIDO", errorRs.getCode());  // Verifica el mensaje de error también
    }


    @Test
    void testGuardarClienteExitoso() {
        ClienteRq clienteRq = new ClienteRq();
        String token = "validToken";

        doNothing().when(clienteService).guardar(clienteRq, token);

        ResponseEntity<Object> response = clienteController.guardarCliente(clienteRq, bindingResult, token);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGuardarClienteConErrorJWT() {
        ClienteRq clienteRq = new ClienteRq();
        String token = "invalidToken";

        doThrow(new CustomException("JWT_INVALIDO", "JWT inválido")).when(clienteService).guardar(clienteRq, token);

        ResponseEntity<Object> response = clienteController.guardarCliente(clienteRq, bindingResult, token);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorRs);
        ErrorRs errorRs = (ErrorRs) response.getBody();
        assertEquals("JWT_INVALIDO", errorRs.getCode());
    }
}
