package top.linrty.live.common.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @Description: 分页响应对象
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/5 16:38
 * @Version: 1.0
 **/
@Data
@Accessors(chain = true)
public class PageRespVO <T> implements Serializable {

    @Schema(description = "分页数据")
    private List<T> list;

    @Schema(description = "是否有下一页")
    private boolean hasNext;

    @Schema(description = "当前页")
    private long page;

    @Schema(description = "每页数量")
    private long pageSize;

    @Schema(description = "总页数")
    private long totalPage;

    @Schema(description = "总记录数")
    private long total;
}
