package ezria.lifetrackr.service.Impl;

import ezria.lifetrackr.Common.Utils.ExcelUtil;
import ezria.lifetrackr.DTO.BillDTO;
import ezria.lifetrackr.Entity.Bill;
import ezria.lifetrackr.Mapper.BillMapper;
import ezria.lifetrackr.VO.BillVO;
import ezria.lifetrackr.service.BillService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BillServiceImpl implements BillService {
    @Autowired
    private BillMapper billMapper;


    @Override
    public void BillImport(Long userid, MultipartFile file) {
        if (file.isEmpty()) {
            return;
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.endsWith(".xlsx")) {
            return;
        }

        try{
            List<Bill> billList = ExcelUtil.readBillExcel(file, userid);
            if (billList.isEmpty()) return;

            billMapper.insertBatch(billList);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Object> GetBill(Long userid, int pageNum, int pageSize, String income) {
        int offset = (pageNum - 1) * pageSize;
        ArrayList<BillVO> list = billMapper.selectBillByPage(userid, offset, pageSize, income);
        int total = billMapper.countBills(userid);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public void DeleteBill(Long userid, Long id) {
        billMapper.deleteBillById(userid, id);
    }

    @Override
    public void CreateBill(Long userid, BillDTO billDTO) {
        billDTO.setUserid(userid);
        Bill bill = new Bill();
        BeanUtils.copyProperties(billDTO, bill);
        List<Bill> list = new ArrayList<>();
        list.add(bill);
        billMapper.insertBatch(list);
    }
}
