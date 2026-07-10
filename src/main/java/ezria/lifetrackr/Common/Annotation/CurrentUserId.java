package ezria.lifetrackr.Common.Annotation;

import java.lang.annotation.*;

/**
 * 标记 Controller 方法参数，自动注入当前登录用户的 userId。
 * <p>
 * 用法：{@code public Result foo(@CurrentUserId Long userId)}
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUserId {
}
