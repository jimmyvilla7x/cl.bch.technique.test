package cl.bch.technique.test.services;

import cl.bch.technique.test.dtos.ClienteRq;
import cl.bch.technique.test.dtos.ClienteRs;
import cl.bch.technique.test.dtos.LoginRq;
import cl.bch.technique.test.dtos.LoginRs;
import cl.bch.technique.test.enums.ECatalogo;
import cl.bch.technique.test.exceptions.CustomException;
import cl.bch.technique.test.services.impl.ClienteServiceImpl;
import cl.bch.technique.test.util.JwtUtil;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * TestClienteServiceImpl.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 20-01-2025
 */

public class TestClienteServiceImpl {

    @InjectMocks
    private ClienteServiceImpl clienteService;

    @Mock
    private ClienteLoader clienteLoader;

    @Mock
    private JwtUtil jwtUtil;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        List<ClienteRq> mockClientes = Collections.singletonList(new ClienteRq(1L, "12508387-0", "password", "Jane", "Doe", null, null, null, null, true, "1234"));

        when(clienteLoader.cargarClientes()).thenReturn(mockClientes);

        clienteService = new ClienteServiceImpl(jwtUtil, clienteLoader, mockClientes);
    }

    @Test
    public void testLoginSuccess() {
        LoginRq loginRq = new LoginRq("12508387-0", "1234");

        when(jwtUtil.generateToken(anyString(), anyInt(), anyBoolean(), anyString())).thenReturn("token");
        when(jwtUtil.validateToken(anyString())).thenReturn(true);

        LoginRs response = clienteService.login(loginRq);

        assertNotNull(response);
        assertEquals("token", response.getToken());
        assertFalse(response.isBloqueo());
        assertEquals("consulta_cliente", response.getSiguienteEtapa());
    }

    @Test
    public void testLoginRutNoExiste() {
        LoginRq loginRq = new LoginRq("12508387-X", "1234");

        CustomException exception = assertThrows(CustomException.class, () -> clienteService.login(loginRq));
        assertEquals(ECatalogo.RUT_NO_EXISTE.getCode(), exception.getCodigo());
    }

    @Test
    public void testConsultaSuccess() {
        String token = "validToken";
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getSiguienteEtapaFromToken(token)).thenReturn("consulta_cliente");
        when(jwtUtil.generateToken(anyString(), anyInt(), anyBoolean(), anyString())).thenReturn("newToken");

        ClienteRs response = clienteService.consulta(1L, token);

        assertNotNull(response);
        assertEquals("newToken", response.getNuevoToken());
    }

    @Test
    public void testGuardarSuccess() throws IOException {
        clienteLoader.cargarClientes();
        String token = "validToken";
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getSiguienteEtapaFromToken(token)).thenReturn("guardar_cliente");

        ClienteRq clienteRq = new ClienteRq(1L, "98765432-1", "password", "Jane", "Doe", null, null, null, null, true, "pass");

        clienteService.guardar(clienteRq, token);

        verify(clienteLoader, times(1)).cargarClientes();
    }

    @Test
    public void testGuardarDuplicateRut() {
        String token = "validToken";
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.getSiguienteEtapaFromToken(token)).thenReturn("guardar_cliente");

        ClienteRq clienteRq = new ClienteRq(1L, "12508387-0", "password", "Jane", "Doe", null, null, null, null, true, "pass");

        CustomException exception = assertThrows(CustomException.class, () -> clienteService.guardar(clienteRq, token));
        assertEquals(ECatalogo.ERROR_RUT_DUPLICADO.getCode(), exception.getCodigo());
    }
}
