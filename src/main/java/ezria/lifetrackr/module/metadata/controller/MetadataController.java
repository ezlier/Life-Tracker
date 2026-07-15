package ezria.lifetrackr.module.metadata.controller;

import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.module.metadata.service.MetadataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/metadata")
public class MetadataController {
    @Autowired
    private MetadataService metadataService;

    @GetMapping("/search")
    public Result search(String type, String keyword) throws IOException {
        log.info("search type:{}, keyword:{}",type,keyword);
        return Result.success(metadataService.search(type, keyword));
    }

    @GetMapping("/detail")
    public Result detail(String type, String sourceId) {
        log.info("detail type:{}, sourceId:{}", type, sourceId);
        return Result.success(metadataService.detail(type, sourceId));
    }
}
