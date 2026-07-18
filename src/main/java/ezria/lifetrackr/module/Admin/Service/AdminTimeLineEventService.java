package ezria.lifetrackr.module.Admin.Service;

import java.util.List;
import java.util.Map;

public interface AdminTimeLineEventService {
    Map<String, Object> getByPage(int pageNum, int pageSize);

    void deleteEvents(List<Long> ids);
}
