package com.devsenior.cdiaz.course_security.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/**
 * Utilidad para el manejo de tokens JWT (JSON Web Tokens).
 * 
 * Esta clase proporciona funcionalidades para generar, validar y extraer información
 * de tokens JWT utilizados en la autenticación de la aplicación. Incluye métodos
 * para crear tokens con claims personalizados, verificar su validez y extraer
 * información específica como el nombre de usuario y la fecha de expiración.
 * 
 * La clase utiliza la biblioteca jjwt para el procesamiento de tokens JWT y
 * se configura mediante propiedades de Spring Boot para la clave secreta y
 * el tiempo de expiración.
 * 
 * @author DevSenior
 * @version 1.0
 * @since 1.0
 */
@Component
public class JwtUtil {

    /** Clave secreta para firmar y verificar tokens JWT */
    @Value("${jwt.secret}")
    private String secret;

    /** Tiempo de expiración del token en milisegundos */
    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Genera un token JWT para un usuario específico.
     * 
     * Este método crea un token JWT que incluye:
     * - El nombre de usuario como subject
     * - Los roles/autoridades del usuario como claims personalizados
     * - Fecha de emisión y expiración
     * - Firma digital usando la clave secreta configurada
     * 
     * @param userDetails Detalles del usuario para el cual generar el token
     * @return Token JWT como String
     * @throws IllegalArgumentException si userDetails es null
     */
    public String generateToken(UserDetails userDetails) {
        var claims = new HashMap<String, Object>();
        // Agregar la informacion al token
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList());

        // crear el token con los claims
        return createToken(claims, userDetails.getUsername());
    }

    /**
     * Extrae el nombre de usuario del token JWT.
     * 
     * @param token Token JWT del cual extraer el nombre de usuario
     * @return Nombre de usuario extraído del token
     * @throws IllegalArgumentException si el token es inválido o no puede ser parseado
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Utiliza el extractor genérico para obtener el 'subject'
    }

    /**
     * Extrae la fecha de expiración del token JWT.
     * 
     * @param token Token JWT del cual extraer la fecha de expiración
     * @return Fecha de expiración del token
     * @throws IllegalArgumentException si el token es inválido o no puede ser parseado
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Utiliza el extractor genérico para obtener el 'expiration'
    }

    /**
     * Verifica si el token JWT ha expirado.
     * 
     * @param token Token JWT a verificar
     * @return true si el token ha expirado, false en caso contrario
     * @throws IllegalArgumentException si el token es inválido o no puede ser parseado
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Compara la fecha de expiración del token con la fecha y hora actuales
    }

    /**
     * Valida un token JWT contra los detalles de un usuario.
     * 
     * La validación incluye:
     * - Verificar que el nombre de usuario en el token coincida con el proporcionado
     * - Verificar que el token no haya expirado
     * - Verificar la firma digital del token
     * 
     * @param token Token JWT a validar
     * @param userDetails Detalles del usuario contra el cual validar el token
     * @return true si el token es válido, false en caso contrario
     * @throws IllegalArgumentException si el token es inválido o no puede ser parseado
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final var username = extractUsername(token); // Extrae el nombre de usuario del token
        // Compara el nombre de usuario extraído con el del UserDetails y verifica si el token no ha expirado
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extrae un claim específico del token JWT usando una función extractora.
     * 
     * Este método genérico permite extraer cualquier claim del token JWT
     * proporcionando una función que especifica qué claim extraer.
     * 
     * @param <T> Tipo de dato del claim a extraer
     * @param token Token JWT del cual extraer el claim
     * @param claimResolver Función que especifica qué claim extraer del token
     * @return El valor del claim extraído
     * @throws IllegalArgumentException si el token es inválido o no puede ser parseado
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final var claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token JWT.
     * 
     * Este método parsea el token JWT, verifica su firma digital y
     * retorna todos los claims contenidos en el payload del token.
     * 
     * @param token Token JWT del cual extraer todos los claims
     * @return Objeto Claims con todos los claims del token
     * @throws IllegalArgumentException si el token es inválido, está malformado o la firma es incorrecta
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser() // Inicia el constructor del parser de JWT
                .verifyWith(getSignInKey()) // Establece la clave de firma para la verificación del token
                .build() // Construye el parser de JWT
                .parseSignedClaims(token) // Parsea el token JWT y verifica su firma (JSON Web Signature)
                .getPayload(); // Obtiene el cuerpo (payload) del token como un objeto Claims
    }

    /**
     * Obtiene la clave secreta para firmar y verificar tokens JWT.
     * 
     * La clave se decodifica desde Base64 y se convierte en una clave HMAC-SHA
     * para ser utilizada en la firma y verificación de tokens.
     * 
     * @return Clave secreta para operaciones criptográficas
     * @throws IllegalArgumentException si la clave secreta no es válida
     */
    private SecretKey getSignInKey() {
        var keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Crea un token JWT con claims personalizados.
     * 
     * Este método privado construye el token JWT final incluyendo:
     * - Claims personalizados proporcionados
     * - Nombre de usuario como subject
     * - Fecha de emisión (iat)
     * - Fecha de expiración (exp)
     * - Firma digital usando la clave secreta
     * 
     * @param claims Claims personalizados a incluir en el token
     * @param username Nombre de usuario que será el subject del token
     * @return Token JWT como String
     */
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }
}
