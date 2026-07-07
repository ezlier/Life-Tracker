package ezria.lifetrackr.Interceptor;

import ezria.lifetrackr.Common.Utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取请求头中的 Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. 检查是否有令牌，且格式为 Bearer xxx
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("请求缺少令牌: {} {}", request.getMethod(), request.getRequestURI());
            writeError(response, 401, "未登录，请先登录");
            return false;
        }

        String token = authHeader.substring(7); // 去掉 "Bearer " 前缀

        // 3. 解析令牌
        try {
            Claims claims = jwtUtils.parseToken(token);
            // 将用户信息存入 request，方便后续 Controller/Service 使用
            // JWT 小数字默认反序列化为 Integer，需统一转为 Long
            Object id = claims.get("id");
            request.setAttribute("userId", id instanceof Long ? id : ((Number) id).longValue());
            request.setAttribute("username", claims.get("username"));
            log.info("令牌验证通过: userId={}, username={}", claims.get("id"), claims.get("username"));
        } catch (ExpiredJwtException e) {
            log.warn("令牌已过期: {}", e.getMessage());
            writeError(response, 401, "令牌已过期，请重新登录");
            return false;
        } catch (SignatureException | MalformedJwtException e) {
            log.warn("令牌无效: {}", e.getMessage());
            writeError(response, 401, "令牌无效");
            return false;
        } catch (Exception e) {
            log.error("令牌解析异常: {}", e.getMessage());
            writeError(response, 401, "认证失败");
            return false;
        }

        return true; // 放行
    }

    /**
     * 向客户端写入 JSON 格式的错误响应
     */
    private void writeError(HttpServletResponse response, int status, String message) throws Exception {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");
        String json = String.format("{\"code\":0,\"msg\":\"%s\",\"data\":null}", message);
        response.getWriter().write(json);
    }
}
