package ezria.lifetrackr.service;

import ezria.lifetrackr.DTO.FocusSessionDTO;

public interface FocusService {
    Integer startFocusSession(FocusSessionDTO focusSessionDTO);

    void pauseFocusSession(Long id);

    void resumeFocusSession(Long id);

    void completeFocusSession(Long id);

    void cancelFocusSession(Long id);
}
