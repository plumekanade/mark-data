package com.plumekanade.mark.data.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 17名单库的标签池
 * @author kanade
 * @date 2023-12-19
 */
@Data
@TableName("company_roster_tag")
public class CompanyRosterTag implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private String id;
    /** 名称 */
    private String name;
    /** 排序号 */
    private Integer sort;
    /** 备注信息 */
    private String remark;
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 删除标记，0已删除 1未删除 */
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
