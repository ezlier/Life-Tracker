package ezria.lifetrackr.service;

import ezria.lifetrackr.Config.PresenceProperties;
import ezria.lifetrackr.DTO.FocusSessionDTO;
import ezria.lifetrackr.Entity.FocusSession;
import ezria.lifetrackr.Mapper.FocusMapper;
import ezria.lifetrackr.service.Impl.FocusServiceImpl;
import ezria.lifetrackr.websocket.PresenceRegistry;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
class FocusServiceImplTests {
    @Mock
    private FocusMapper focusMapper;

    @Mock
    private TimeLineEventService timeLineEventService;

    @Mock
    private PresenceRegistry presenceRegistry;

    @Mock
    private PresenceProperties presenceProperties;

    @InjectMocks
    private FocusServiceImpl focusService;

    @Test
    void newFocusIsTrackedBeforeClientBindsWebSocket() {
        FocusSessionDTO dto = new FocusSessionDTO();
        dto.setUserId(7L);
        dto.setGoal("Read");
        when(presenceProperties.getHeartbeatTimeoutMs()).thenReturn(25_000L);
        doAnswer(invocation -> {
            FocusSession session = invocation.getArgument(0);
            session.setId(42L);
            return 1;
        }).when(focusMapper).insertFocusSession(any(FocusSession.class));

        assertEquals(42, focusService.startFocusSession(dto));

        verify(presenceRegistry).trackRecoveryCandidate(eq(7L), eq(42L), anyLong());
    }

    @Test
    void autoPauseWritesTimelineEventAfterConditionalUpdate() {
        FocusSession session = new FocusSession();
        session.setId(42L);
        session.setUserId(7L);
        session.setStatus("paused");
        when(focusMapper.autoPauseIfRunning(7L, 42L)).thenReturn(1);
        when(focusMapper.selectByIdAndUser(7L, 42L)).thenReturn(session);

        assertTrue(focusService.autoPauseFocusSession(7L, 42L));

        verify(timeLineEventService).autoPausedFocusSessionEvent(session);
    }

    @Test
    void autoPauseIsNoOpWhenFocusIsNoLongerRunning() {
        when(focusMapper.autoPauseIfRunning(7L, 42L)).thenReturn(0);

        assertFalse(focusService.autoPauseFocusSession(7L, 42L));

        verifyNoInteractions(timeLineEventService);
    }
}
