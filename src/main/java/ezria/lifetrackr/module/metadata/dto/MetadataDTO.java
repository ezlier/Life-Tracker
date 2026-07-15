package ezria.lifetrackr.module.metadata.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetadataDTO {

    /**
     * TMDB
     * RAWG
     * GOOGLE_BOOK
     */
    private String source;

    /**
     * 第三方ID
     */
    private String sourceId;

    /**
     * Item类型
     */
    private String itemType;

    /**
     * 标题
     */
    private String title;

    /**
     * 原标题
     */
    private String originalTitle;

    /**
     * 封面
     */
    private String cover;

    /**
     * 简介
     */
    private String description;

    /**
     * 评分
     */
    private Double rating;

    /**
     * 发布日期
     */
    private LocalDate releaseDate;

    /**
     * 作者
     */
    private String author;

    /**
     * 出版社
     */
    private String publisher;
}
