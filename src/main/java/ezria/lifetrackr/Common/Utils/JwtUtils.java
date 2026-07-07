package ezria.lifetrackr.Common.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtils {

    private final SecretKey secretKey;
    private final long expireHours;

    /**
     * 从 yaml 配置读取密钥和过期时间
     * @param secret      Base64 编码的密钥字符串（需 ≥256位即32字节）
     * @param expireHours 令牌过期时间（小时），默认 12
     */
    public JwtUtils(@Value("${jwt.secret}") String secret,
                    @Value("${jwt.expire-hours:12}") long expireHours) {
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.expireHours = expireHours;
    }

    /**
     * 生成JWT令牌
     * @param claims 自定义载荷（如用户ID、用户名）
     * @return JWT令牌字符串
     */
    public String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expireHours * 60 * 60 * 1000L);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 解析JWT令牌
     * @param token JWT令牌
     * @return 载荷信息
     * @throws ExpiredJwtException 令牌过期
     * @throws MalformedJwtException 令牌格式错误
     * @throws SignatureException 签名校验失败
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
