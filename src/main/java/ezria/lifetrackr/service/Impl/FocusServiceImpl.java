package ezria.lifetrackr.service.Impl;

import ezria.lifetrackr.DTO.FocusSessionDTO;
import ezria.lifetrackr.Entity.FocusSession;
import ezria.lifetrackr.Mapper.FocusMapper;
import ezria.lifetrackr.service.FocusService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class FocusServiceImpl implements FocusService {
    @Autowired
    private FocusMapper focusMapper;

    @Override
    public Integer startFocusSession(FocusSessionDTO focusSessionDTO) {
        FocusSession focusSession = new FocusSession();
        BeanUtils.copyProperties(focusSessionDTO, focusSession);
        focusSession.setIsCompleted(false);
        focusSession.setStatus("running");
        focusMapper.insertFocusSession(focusSession);
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
    }

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
        session.setEndTime(now);

        focusMapper.updateById(session);
    }
}
