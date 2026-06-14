package ra.hwss1301.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${JWT_SECRET}")
    private String jwtSecret;
    @Value("${JWT_EXPIRATION}")
    private Long jwtExpiration;

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
    public Boolean validateToken(String token) throws MalformedJwtException, ExpiredJwtException, UnsupportedJwtException, IllegalArgumentException {
        Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
        return true;
    }
}
