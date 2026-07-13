package ezria.lifetrackr.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimelineEventType {
    CREATE_ITEM("创建内容"),
    START_ITEM("开始进行"),
    COMPLETE_ITEM("完成"),
    UPDATE_ITEM("修改"),
    DELETE_ITEM("删除"),
    RATE_ITEM("评分"),
    COMMENT_ITEM("评论"),
    FAVORITE_ITEM("收藏"),
    TAG_UPDATE("修改标签"),

    FOCUS_SESSION_START("开始专注"),
    FOCUS_SESSION_AUTO_PAUSE("客户端离线自动暂停"),
    FOCUS_SESSION_COMPLETE("完成专注"),
    FOCUS_SESSION_CANCEL("取消专注"),

    REGISTERED_SUCCESSFULLY("注册成功"),
    LOGIN_SUCCESSFULLY("登录成功");

    private final String description;
}
