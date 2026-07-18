package ezria.lifetrackr.module.Admin.Service;


import java.util.List;
import java.util.Map;

public interface AdminItemService {
    Map<String, Object> getItemsByPage(int pageNum, int pageSize);

    void deleteItems(List<Long> ids);
}
