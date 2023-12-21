package com.plumekanade.mark.data.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.plumekanade.mark.data.enums.CompanyRosterSourceEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 17名单库来源关联表
 * @author kanade
 * @date 2023-12-19
 */
@Data
@NoArgsConstructor
@TableName("company_roster_source")
public class CompanyRosterSource implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private String id;
    /** 名单库id */
    private String companyRosterId;
    /** 来源，枚举类CompanyRosterSourceEnum */
    private CompanyRosterSourceEnum source;
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public CompanyRosterSource(String companyRosterId, CompanyRosterSourceEnum source) {
        this.companyRosterId = companyRosterId;
        this.source = source;
    }
}
