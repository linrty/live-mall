package top.linrty.live.shop.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @Description: TODO
 * @Author: Linrty
 * @Email: linrty.cn@gmail.com
 * @Date: 2024/8/7 17:08
 * @Version: 1.0
 **/
@Data
@TableName("t_category_info")
@Accessors(chain = true)
public class CategoryInfo {
    @TableId(type = IdType.AUTO)
    @TableField(value = "id")
    @Schema(description = "id")
    private Integer id;

    @TableField(value = "level")
    @Schema(description = "分类级别")
    private Integer level;

    @TableField(value = "category_name")
    @Schema(description = "分类名称")
    private String categoryName;

    @TableField(value = "parent_id")
    @Schema(description = "父级id")
    private Integer parentId;

    @TableField(value = "status")
    @Schema(description = "状态")
    private Integer status;

    @TableField(value = "create_time")
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(value = "update_time")
    @Schema(description = "更新时间")
    private Date updateTime;
}
