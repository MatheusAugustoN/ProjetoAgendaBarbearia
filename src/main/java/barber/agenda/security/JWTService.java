package barber.agenda.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JWTService {
    // Chave segura gerada automaticamente ou vinda de variável de ambiente
    private final Key CHAVE = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final Long EXPIRACAO = 28800000L; // 8 horas

    public String gerarToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // Guarda as permissões dentro do token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRACAO))
                .signWith(CHAVE)
                .compact();
    }

    public Claims validarETerClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(CHAVE)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
