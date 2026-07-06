package ezria.lifetrackr.Common.Utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    // 生成安全密钥（生产环境建议从配置文件读取，且密钥长度≥256位）
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    // 令牌过期时间：12小时（可根据业务调整）
    private static final long EXPIRE_TIME = 12 * 60 * 60 * 1000L;

    /**
     * 生成JWT令牌
     * @param claims 自定义载荷（如用户ID、用户名）
     * @return JWT令牌字符串
     */
    public static String generateToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRE_TIME);
        return Jwts.builder()
                .setClaims(claims)          // 放入自定义载荷
                .setIssuedAt(now)           // 设置令牌签发时间
                .setExpiration(expiration)  // 设置过期时间
                .signWith(SECRET_KEY)       // 使用密钥签名（默认HS256）
                .compact();                 // 生成令牌
    }

    /**
     * 解析JWT令牌
     * @param token JWT令牌
     * @return 载荷信息
     * @throws ExpiredJwtException 令牌过期
     * @throws MalformedJwtException 令牌格式错误
     * @throws SignatureException 签名校验失败
     */
    public static Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY) // 设置验签密钥
                .build()
                .parseClaimsJws(token)     // 解析令牌
                .getBody();                // 获取载荷
    }
}