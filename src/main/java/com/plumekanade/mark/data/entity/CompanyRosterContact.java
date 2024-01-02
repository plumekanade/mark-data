package com.plumekanade.mark.data.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 17名单库的联系人表
 * @author kanade
 * @date 2023-12-19
 */
@Data
@NoArgsConstructor
@TableName("company_roster_contact")
public class CompanyRosterContact implements Serializable {
    private static final long serialVersionUID = -4010014379131251840L;
    @TableId
    private String id;
    /** 名单库id */
    private String companyRosterId;
    /** 名称 */
    private String name;
    /** 联系电话 */
    private String phone;
    /** 创建人 */
    private String creator;
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 修改人 */
    private String reviser;
    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /** 如果两个联系人字段为空的时候 拿法人作为联系人 */
    public CompanyRosterContact(BfCompanyRoster roster, String legalName) {
        if (StringUtils.isNotBlank(roster.getLinkUser())) {
            this.name = roster.getLinkUser();
        } else if (StringUtils.isNotBlank(roster.getContactName())) {
            this.name = roster.getContactName();
        } else {
            this.name = legalName;
        }
        this.phone = StringUtils.isNotBlank(roster.getLinkPhone()) ? roster.getLinkPhone() : roster.getContactPhone();
        if (this.phone != null) {
            this.phone = this.phone.trim();
        }
        this.creator = "系统导入";
    }
}
