package com.plumekanade.mark.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 马克企业（注销）数据导入
 * @author kanade
 * @date 2023-12-15
 */
@Data
@TableName("marker_company_off_data")
public class MarkerCompanyOffData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @TableId
    private String id;
    /** 企业名称 */
    private String companyName;
    /** 英文名称 */
    private String engName;
    /** 经营状态 */
    private String manageState;
    /** 法人（法定代表人） */
    private String legalName;
    /** 注册资本 */
    private String registerCapital;
    /** 实缴资本 */
    private String realCapital;
    /** 成立日期 */
    private String establishDate;
    /** 核准日期 */
    private String verifyDate;
    /** 营业期限 */
    private String businessTerm;
    /** 所属省份 */
    private String province;
    /** 所属城市 */
    private String city;
    /** 所属区县 */
    private String district;
    /** 登记机关 */
    private String registrar;
    /** 统一社会信用代码 */
    private String creditCode;
    /** 纳税人识别号 */
    private String taxpayerCode;
    /** 工商注册号 */
    private String businessRegisterCode;
    /** 组织机构代码 */
    private String organizationCode;
    /** 纳税人资质 */
    private String taxpayerCredential;
    /** 参保人数 */
    private String insureNum;
    /** 企业类型 */
    private String companyType;
    /** 曾用名 */
    private String previousName;
    /** 注册地址 */
    private String registerAddress;
    /** 网址 */
    private String url;
    /** 联系电话 */
    private String contactPhone;
    /** 邮箱 */
    private String email;
    /** 经营范围 */
    private String businessScope;
    /** 公司规模 */
    private String companySize;
    /** 来源 */
    private String source;
    /** 所属行业 */
    private String industry;
    /** 一级行业分类 */
    private String firstClassify;
    /** 二级行业分类 */
    private String secondClassify;
    /** 三级行业分类 */
    private String thirdClassify;
    /** 经度 */
    private String lon;
    /** 纬度 */
    private String lat;
}
