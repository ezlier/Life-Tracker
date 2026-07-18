package ezria.lifetrackr.Mapper;

import ezria.lifetrackr.Entity.Bill;
import ezria.lifetrackr.VO.BillVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface BillMapper {
    void insertBatch(List<Bill> billList);

    ArrayList<BillVO> selectBillByPage(Long userid, int offset, int pageSize, String income);

    @Select("SELECT COUNT(*) FROM bill where userid = #{userid}")
    int countBills(Long userid);

    @Delete("DELETE from bill where userid = #{userid} and id = #{id}")
    void deleteBillById(Long userid, Long id);
}
