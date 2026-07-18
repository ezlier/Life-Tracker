package ezria.lifetrackr.module.Admin.Service;

import java.util.List;
import java.util.Map;

public interface AdminFocusSessionService {
    Map<String, Object> getByPage(int pageNum, int pageSize);

    void deleteSessions(List<Long> ids);
}
