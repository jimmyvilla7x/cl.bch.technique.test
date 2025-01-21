package cl.bch.technique.test.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * JwtUtil.
 *
 * @author Jimmy Villa.
 * @version 1.0.0, 20-01-2025
 */
@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Genera un token JWT con los datos proporcionados.
     * @param rut El RUT del usuario.
     * @param nroIntento Número de intentos.
     * @param bloqueo Estado de bloqueo.
     * @param siguienteEtapa La siguiente etapa del proceso.
     * @return Token JWT generado.
     */
    public String generateToken(String rut, int nroIntento, boolean bloqueo, String siguienteEtapa) {
        // Parametros para token
        Map<String, Object> claims = new HashMap<>();
        claims.put("rut", rut);
        claims.put("nroIntento", nroIntento);
        claims.put("bloqueo", bloqueo);
        claims.put("siguiente_etapa", siguienteEtapa);

        // Firmar y generar el JWT
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora de expiración
            .signWith(secretKey)
            .compact();
    }

    /**
     * Valida un token JWT.
     * @param token El token JWT a validar.
     * @return true si el token es válido, false de lo contrario.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("Token expirado", e);
        } catch (MalformedJwtException e) {
            logger.error("Token mal formado", e);
        } catch (Exception e) {
            logger.error("Error al validar el token", e);
        }
        return false;
    }

    /**
     * Extrae el RUT del token JWT.
     * @param token El token JWT.
     * @return El RUT extraído del token.
     */
    public String getRutFromToken(String token) {
        return (String) getClaimFromToken(token, "rut");
    }

    /**
     * Extrae la siguiente etapa del token JWT.
     * @param token El token JWT.
     * @return La siguiente etapa extraída del token.
     */
    public String getSiguienteEtapaFromToken(String token) {
        return (String) getClaimFromToken(token, "siguiente_etapa");
    }

    /**
     * Obtiene el valor de un claim específico del token JWT.
     * @param token El token JWT.
     * @param claimKey La clave del claim.
     * @return El valor del claim.
     */
    private Object getClaimFromToken(String token, String claimKey) {
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
        return claims.get(claimKey);
    }
}
