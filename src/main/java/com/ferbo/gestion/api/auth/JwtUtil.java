package com.ferbo.gestion.api.auth;

import io.jsonwebtoken.ExpiredJwtException;
import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil 
{
    private static final Logger log = LogManager.getLogger(JwtUtil.class);

    private String secret;

    @Value("${jwt.accessExpiration}")
    private long accessExpiration;
    
    @Value("${jwt.refreshExpiration}")
    private long refreshExpiration;
    
    @PostConstruct
    public void init() {
        try {
            InitialContext ctx = new InitialContext();
            secret = (String) ctx.lookup("java:comp/env/jwt.secret");
        } catch (NamingException e) {
            log.error("No se pude extraer del JNDI el secreto. {}", e);
            throw new RuntimeException("No se pudo cargar desde el JNDI");
        }
    }

    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(accessExpiration);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Instant now = Instant.now();
        Instant expiration = now.plusMillis(refreshExpiration);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expirado", e);
        } catch (UnsupportedJwtException e) {
            log.error("Token no soportado", e);
        } catch (MalformedJwtException e) {
            log.error("Token mal formado", e);
        } catch (SecurityException e) {
            log.error("Firma inválida", e);
        } catch (IllegalArgumentException e) {
            log.error("Token vacío", e);
        }
        return false;
    }
    
}
