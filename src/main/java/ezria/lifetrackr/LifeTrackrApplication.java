package ezria.lifetrackr;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@MapperScan("ezria.lifetrackr.Mapper")
public class LifeTrackrApplication {

    public static void main(String[] args) {
        SpringApplication.run(LifeTrackrApplication.class, args);
        log.info("LifeTrackrApplication started successfully.");
    }

}
