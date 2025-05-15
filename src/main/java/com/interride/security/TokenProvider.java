package com.interride.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class TokenProvider {

    /*------------------------------*/
    /*  Valores sacados del .yml    */
    /*------------------------------*/
    @Value("${interride.jwt.secret}")
    private String jwtSecret;          // en Base64 o texto plano

    @Value("${interride.jwt.exp-ms}")
    private long jwtExpirationMs;      // ej. 86400000 (24 h)

    private SecretKey secretKey;

    @PostConstruct
    private void init() {
        // Si tu secreto NO está Base64-encoded usa getBytes()
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /*-----------------------------------------------------*/
    /*  *** MÉTODOS QUE ESPERAN OTRAS CLASES ***           */
    /*-----------------------------------------------------*/

    /** Crea un JWT y lo devuelve en String. */
    public String createAccessToken(Authentication authentication) {
        return createToken(authentication);
    }

    /** Tiempo (en milisegundos) que durará un token nuevo. */
    public long getExpiration() {
        return jwtExpirationMs;
    }

    /** Verifica firma + expiración. Devuelve true/false. */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    /** Extrae el uid (Integer) que metimos en el claim "uid". */
    public Integer getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("uid", Integer.class);
    }

    /*-----------------------------------------------------*/
    /*  Lógica de generación interna (HS256)               */
    /*-----------------------------------------------------*/
    private String createToken(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        Date now  = new Date();
        Date exp  = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .claim("uid", principal.getId())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Parsea el JWT y devuelve su fecha de expiración como Instant.
     */
    public Instant getExpirationDate(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date exp = claims.getExpiration();
        return exp.toInstant();
    }

    // Si prefieres Date:
    public Date getExpirationAsDate(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
