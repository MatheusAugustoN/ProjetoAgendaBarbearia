package barber.agenda.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${api.security.token.secret}")
    private String secret;

    // 1. Gerar a chave de assinatura (Forma correta para JJWT 0.12+)
    private SecretKey getSigningKey() {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 2. Extrair o Username (ou qualquer Claim)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 3. O CORAÇÃO DA MUDANÇA: Novo Parser
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Mudou de setSigningKey para verifyWith
                .build()
                .parseSignedClaims(token)    // Mudou de parseClaimsJws para parseSignedClaims
                .getPayload();               // Mudou de getBody para getPayload
    }

    // 4. Gerar Token (Exemplo simplificado)
    public String generateToken(String username, java.util.List<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles) // Adiciona as permissões no payload do token
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey())
                .compact();
    }

    public boolean isTokenValid(String token, String username) {
        final String extractedUser = extractUsername(token);
        return (extractedUser.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}