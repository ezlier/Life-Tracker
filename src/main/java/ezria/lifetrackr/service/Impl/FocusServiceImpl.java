package ezria.lifetrackr.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ezria.lifetrackr.DTO.FocusSessionDTO;
import ezria.lifetrackr.Entity.FocusSession;
import ezria.lifetrackr.Mapper.FocusMapper;
import ezria.lifetrackr.service.FocusService;
import ezria.lifetrackr.service.TimeLineEventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class FocusServiceImpl implements FocusService {
    @Autowired
    private FocusMapper focusMapper;

    @Autowired
    private TimeLineEventService timeLineEventService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer startFocusSession(FocusSessionDTO focusSessionDTO) {
        FocusSession focusSession = new FocusSession();
        BeanUtils.copyProperties(focusSessionDTO, focusSession);
        focusSession.setIsCompleted(false);
        focusSession.setStatus("running");
        focusMapper.insertFocusSession(focusSession);

        timeLineEventService.saveFocusSessionEvent(focusSession, (long) focusSession.getId().intValue());
        return focusSession.getId().intValue();
    }

    @Override
    public void pauseFocusSession(Long id) {
        FocusSession session = focusMapper.selectById(id);
        if (session == null) {
            throw new RuntimeException("专注记录不存在: " + id);
        }
        if (!"running".equals(session.getStatus())) {
            throw new RuntimeException("当前状态不允许暂停: " + session.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        int elapsed = (int) Duration.between(session.getStartTime(), now).getSeconds();
        int prevActual = session.getActualDuration() != null ? session.getActualDuration() : 0;
        session.setActualDuration(prevActual + elapsed);

        session.setStatus("paused");
        session.setStartTime(now);

        focusMapper.updateById(session);
    }

    @Override
    public void resumeFocusSession(Long id) {
        FocusSession session = focusMapper.selectById(id);
        if (session == null) {
            throw new RuntimeException("专注记录不存在: " + id);
        }
        if (!"paused".equals(session.getStatus())) {
            throw new RuntimeException("当前状态不允许恢复: " + session.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        int pauseDuration = (int) Duration.between(session.getStartTime(), now).getSeconds();
        int prevPause = session.getPauseDuration() != null ? session.getPauseDuration() : 0;
        session.setPauseDuration(prevPause + pauseDuration);

        session.setStatus("running");
        session.setStartTime(now);

        focusMapper.updateById(session);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeFocusSession(Long id) {
        FocusSession session = focusMapper.selectById(id);
        if (session == null) {
            throw new RuntimeException("专注记录不存在: " + id);
        }
        if ("completed".equals(session.getStatus()) || "canceled".equals(session.getStatus())) {
            throw new RuntimeException("当前状态不允许完成: " + session.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        if ("running".equals(session.getStatus())) {
            int elapsed = (int) Duration.between(session.getStartTime(), now).getSeconds();
            int prevActual = session.getActualDuration() != null ? session.getActualDuration() : 0;
            session.setActualDuration(prevActual + elapsed);
        } else if ("paused".equals(session.getStatus())) {
            int pauseDuration = (int) Duration.between(session.getStartTime(), now).getSeconds();
            int prevPause = session.getPauseDuration() != null ? session.getPauseDuration() : 0;
            session.setPauseDuration(prevPause + pauseDuration);
        }

        session.setStatus("completed");
        session.setIsCompleted(true);
        session.setEndTime(now);

        focusMapper.updateById(session);
        timeLineEventService.completedFocusSessionEvent(session);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelFocusSession(Long id) {
        FocusSession session = focusMapper.selectById(id);
        if (session == null) {
            throw new RuntimeException("专注记录不存在: " + id);
        }
        if ("completed".equals(session.getStatus()) || "canceled".equals(session.getStatus())) {
            throw new RuntimeException("当前状态不允许取消: " + session.getStatus());
        }

        LocalDateTime now = LocalDateTime.now();
        if ("running".equals(session.getStatus())) {
            int elapsed = (int) Duration.between(session.getStartTime(), now).getSeconds();
            int prevActual = session.getActualDuration() != null ? session.getActualDuration() : 0;
            session.setActualDuration(prevActual + elapsed);
        } else if ("paused".equals(session.getStatus())) {
            int pauseDuration = (int) Duration.between(session.getStartTime(), now).getSeconds();
            int prevPause = session.getPauseDuration() != null ? session.getPauseDuration() : 0;
            session.setPauseDuration(prevPause + pauseDuration);
        }

        session.setStatus("canceled");
        session.setIsCompleted(true);
        session.setEndTime(now);

        focusMapper.updateById(session);
        timeLineEventService.cancelFocusSessionEvent(session);
    }

    @Override
    public Page<FocusSession> getFocusSessions(Long userId, String mode, String goal, String status,
                                                Boolean isCompleted, Integer pageNum, Integer pageSize) {
        Page<FocusSession> page = new Page<>(pageNum, pageSize);

        QueryWrapper<FocusSession> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (mode != null && !mode.isEmpty()) {
            wrapper.eq("mode", mode);
        }
        if (goal != null && !goal.isEmpty()) {
            wrapper.like("goal", goal);
        }
        if (status != null && !status.isEmpty()) {
            wrapper.eq("status", status);
        }
        if (isCompleted != null) {
            wrapper.eq("is_completed", isCompleted);
        }
        wrapper.orderByDesc("start_time");

        return focusMapper.selectPage(page, wrapper);
    }
}
