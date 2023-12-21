package com.plumekanade.mark.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 企业名单库标签关联表
 * @author kanade
 * @date 2023-12-19
 */
@Data
@TableName("company_roster_tag_relate")
public class CompanyRosterTagRelate implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private String id;
    /** 名单库id */
    private String companyRosterId;
    /** 标签id */
    private String tagId;
}
