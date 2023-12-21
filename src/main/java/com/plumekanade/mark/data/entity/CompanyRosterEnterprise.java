package com.plumekanade.mark.data.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 名单库系统企业关联表
 * @author kanade
 * @date 2023-12-19
 */
@Data
@TableName("company_roster_enterprise")
public class CompanyRosterEnterprise implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private String id;
    /** 名单库id */
    private String companyRosterId;
    /** 系统企业表id */
    private String enterpriseId;
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
