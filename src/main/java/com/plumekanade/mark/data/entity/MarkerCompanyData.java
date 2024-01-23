package com.plumekanade.mark.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.plumekanade.mark.data.consts.ProjectConst;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 马克企业数据导入
 * @author kanade
 * @date 2023-12-15
 */
@Data
@TableName("marker_company_data")
public class MarkerCompanyData implements Serializable {
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
    private Integer dataState = 0;

    /** 处理特殊转义符号 */
    public void handlePropsData() {
        this.companyName = handleStr(this.companyName);
        this.engName = handleStr(this.engName);
        this.manageState = handleStr(this.manageState);
        this.legalName = handleStr(this.legalName);
        this.registerCapital = handleStr(this.registerCapital);
        this.realCapital = handleStr(this.realCapital);
        this.establishDate = handleStr(this.establishDate);
        this.verifyDate = handleStr(this.verifyDate);
        this.businessTerm = handleStr(this.businessTerm);
        this.province = handleStr(this.province);
        this.city = handleStr(this.city);
        this.district = handleStr(this.district);
        this.registrar = handleStr(this.registrar);
        this.taxpayerCode = handleStr(this.taxpayerCode);
        this.creditCode = handleStr(this.creditCode);
        this.businessRegisterCode = handleStr(this.businessRegisterCode);
        this.organizationCode = handleStr(this.organizationCode);
        this.taxpayerCredential = handleStr(this.taxpayerCredential);
        this.insureNum = handleStr(this.insureNum);
        this.companyType = handleStr(this.companyType);
        this.previousName = handleStr(this.previousName);
        this.registerAddress = handleStr(this.registerAddress);
        this.url = handleStr(this.url);
        this.contactPhone = handleStr(this.contactPhone);
        this.email = handleStr(this.email);
        this.businessScope = handleStr(this.businessScope);
        this.companySize = handleStr(this.companySize);
        this.source = handleStr(this.source);
        this.industry = handleStr(this.industry);
        this.firstClassify = handleStr(this.firstClassify);
        this.secondClassify = handleStr(this.secondClassify);
        this.thirdClassify = handleStr(this.thirdClassify);
        this.lon = handleStr(this.lon);
        this.lat = handleStr(this.lat);
    }

    public String handleStr(String str) {
        if (StringUtils.isNotBlank(str)) {
            str = str.replaceAll(ProjectConst.B_SPACE, ProjectConst.NO_STR).replaceAll(ProjectConst.SPACE, ProjectConst.NO_STR).replaceAll(ProjectConst.WRAP, ProjectConst.NO_STR);
        }
        if ("-".equals(str)) {
            str = null;
        }
        return str;
    }
}
