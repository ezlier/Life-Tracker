package ezria.lifetrackr.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDTO {
    private Long userid;
    private String counterparty;
    private String goods;
    private String income;
    private float money;
    private LocalDateTime tradingHours;
}
