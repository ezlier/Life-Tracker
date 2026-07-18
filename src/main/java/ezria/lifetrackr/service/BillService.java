package ezria.lifetrackr.service;

import ezria.lifetrackr.DTO.BillDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface BillService {
    void BillImport(Long userid, MultipartFile file);

    Map<String, Object> GetBill(Long userid, int pageNum, int pageSize, String income);

    void DeleteBill(Long userid, Long id);

    void CreateBill(Long userid, BillDTO billDTO);
}
