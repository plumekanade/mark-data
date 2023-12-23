package com.plumekanade.mark.data.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.plumekanade.mark.data.utils.MapperUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 17名单库
 * @author kanade
 * @date 2023-12-19
 */
@Data
@NoArgsConstructor
@TableName("company_roster")
public class CompanyRoster implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private String id;
    /** 企业名称 */
    private String name;
    /** 企业英文名称 */
    private String engName;
    /** 统一社会信用代码 */
    private String creditCode;
    /** 企业网址（官网） */
    private String url;
    /** 联系电话 */
    private String contactPhone;
    /** 企业关键字 */
    private String keywords;
    /** 企业入驻八方资源网的日期 */
    private String bfJoinDate;
    /** 八方网黄页的一级标题 */
    private String bfHyTitle;
    /** 八方网黄页的二级标题 */
    private String bfHySubTitle;
    /** 企业在八方网的域名前缀地址 */
    private String bfPrefixId;
    /** 企业在八方网是否有商品，1有 0无 */
    private Integer bfSku;
    /** 企业地址 */
    private String address;
    /** 固话 */
    private String fixedPhone;
    /** 邮编号码 */
    private String postCode;
    /** 传真号码 */
    private String faxCode;
    /** 企业类型 */
    private String type;
    /** 注册地址 */
    private String regAddress;
    /** 法人（法定代表人） */
    private String legalName;
    /** 企业分类（八方的企业类型enterprise_type） */
    private String classify;
    /** 注册资本 */
    private String regCapital;
    /** 实缴资本 */
    private String realCapital;
    /** 成立日期 */
    private LocalDate establishDate;
    /** 公司规模 */
    private String companySize;
    /** 月产量 */
    private String monthlyProduction;
    /** 年营业额 */
    private String annualTurnover;
    /** 年出口额 */
    private String annualExport;
    /** 管理体系认证 */
    private String manageSystemAuth;
    /** 主要经营地点 */
    private String mainManageLocation;
    /** 主要客户 */
    private String mainCustomer;
    /** 厂房面积 */
    private String plantArea;
    /** 是否提供OEM代加工 */
    private String oemAgentProcess;
    /** 开户银行 */
    private String openBank;
    /** 银行账户 */
    private String bankAccount;
    /** 主要市场 */
    private String mainMarket;
    /** 主营产品或服务 */
    private String mainProductServe;
    /** 工商注册号 */
    private String businessRegCode;
    /** 组织机构代码 */
    private String organizationCode;
    /** 纳税人识别号 */
    private String taxpayerCode;
    /** 经营状态（八方数据需要判断经营状态manage_state和登记状态registration_state，一个空就用另一个） */
    private String manageState;
    /** 住所 */
    private String residence;
    /** 营业期限 */
    private String businessTerm;
    /** 登记机关 */
    private String registrar;
    /** 核准日期 */
    private String approvalDate;
    /** 经营范围 */
    private String businessScope;
    /** 简介 */
    private String profile;
    /** 所属省份 */
    private String province;
    /** 所属城市 */
    private String city;
    /** 所属区县 */
    private String district;
    /** 纳税人资质 */
    private String taxpayerCredential;
    /** 参保人数 */
    private String insureNum;
    /** 曾用名 */
    private String previousName;
    /** 邮箱 */
    private String email;
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
    /** 是否注册17网，0否 1是 */
    private Integer regState;
    /** 数据更新时间 */
    private LocalDateTime dataTime;
    /** 创建时间 */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /** 创建人名称 */
    private String creator;
    /** 修改时间 */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    /** 修改人名称 */
    private String reviser;
    /** 名单库联系人（导入用） */
    @TableField(exist = false)
    private CompanyRosterContact contact;

    public CompanyRoster(String name, String creditCode, String manageState, String url, String contactPhone, String keywords, String address, String regAddress, String companySize, String businessScope) {
        this.name = name;
        this.creditCode = creditCode;
        this.manageState = manageState;
        this.url = url;
        this.contactPhone = contactPhone;
        this.keywords = keywords;
        this.address = address;
        this.regAddress = regAddress;
        this.companySize = companySize;
        this.businessScope = businessScope;
    }

    public void setBfProps(BfCompanyRoster companyRoster) {
        this.bfJoinDate = companyRoster.getCompanyDate();
        this.bfHyTitle = companyRoster.getTitle();
        this.bfHySubTitle = companyRoster.getSubTitle();
        this.bfSku = companyRoster.getHasSku();
        this.bfPrefixId = companyRoster.getCrawCompanyId();
        if (StringUtils.isBlank(this.bfPrefixId)) {
            this.bfPrefixId = companyRoster.getCompanyIdY();
        }
        this.monthlyProduction = companyRoster.getMonthlyProduction();
        this.annualTurnover = companyRoster.getYearTurnover();
        this.annualExport = companyRoster.getYearExport();
        this.classify = companyRoster.getEnterpriseType();
        this.mainCustomer = companyRoster.getMainCustomer();
        this.mainManageLocation = companyRoster.getMainManageLocation();
        this.mainMarket = companyRoster.getMainMarket();
        this.mainProductServe = companyRoster.getMainProductServe();
        this.postCode = companyRoster.getPostCode();
        this.faxCode = companyRoster.getFaxCode();
        this.manageState = companyRoster.getManageState();
        if (StringUtils.isBlank(this.manageState)) {
            this.manageState = companyRoster.getRegistrationState();
        }

        // 企业类型判断
        String limitCompany = "有限公司";
        String economic = companyRoster.getEnterpriseEconomic();
        String typeName = companyRoster.getType();
        boolean economicFlag = StringUtils.isNotBlank(economic);
        boolean typeFlag = StringUtils.isNotBlank(typeName);
        // 默认优先 type 字段内容
        this.type = typeFlag ? typeName : economic;
        if (this.name.contains(limitCompany)) {
            // 其中一个是否带有 “有限公司”
            if (economicFlag && economic.contains(limitCompany)) {
                this.type = economic;
            } else if (typeFlag && typeName.contains(limitCompany)) {
                this.type = typeName;
            }
        }
        this.legalName = StringUtils.isNotBlank(companyRoster.getLegalUser()) ? companyRoster.getLegalUser() : companyRoster.getLegalName();
        this.regCapital = StringUtils.isNotBlank(companyRoster.getRegCapital()) ? companyRoster.getRegCapital() : companyRoster.getRegisterCapital();
        String date = StringUtils.isNotBlank(companyRoster.getBuildDate()) ? companyRoster.getBuildDate() : companyRoster.getEstablishDate();
        if (StringUtils.isNotBlank(date)) {
            this.establishDate = LocalDate.parse(date, MapperUtils.DF);
        }
        this.companySize = companyRoster.getStaffNum();
        this.manageSystemAuth = companyRoster.getManageSystemAuth();
        this.plantArea = companyRoster.getPlantArea();
        this.oemAgentProcess = companyRoster.getOemAgentProcess();
        this.openBank = companyRoster.getOpenBank();
        this.bankAccount = companyRoster.getBankAccount();
        this.businessRegCode = companyRoster.getBusinessRegisterCode();
        this.residence = companyRoster.getResidence();
        this.businessTerm = "";
        if (StringUtils.isNotBlank(companyRoster.getFromBusinessTerm())) {
            this.businessTerm = companyRoster.getFromBusinessTerm();
        }
        if (StringUtils.isNotBlank(companyRoster.getToBusinessTerm())) {
            this.businessTerm += " 至 " + companyRoster.getToBusinessTerm();
        }
        this.registrar = companyRoster.getRegistrar();
        this.approvalDate = companyRoster.getApprovalDate();
        this.profile = companyRoster.getProfile();
    }

    public void setMarkProps(MarkerCompanyData markData) {
        this.engName = markData.getEngName();
        this.approvalDate = markData.getVerifyDate();
        this.legalName = markData.getLegalName();
        this.regCapital = markData.getRegisterCapital();
        this.realCapital = markData.getRealCapital();
        if (StringUtils.isNotBlank(markData.getEstablishDate())) {
            this.establishDate = LocalDate.parse(markData.getEstablishDate(), MapperUtils.DF);
        }
        if (StringUtils.isNotBlank(markData.getVerifyDate())) {
            this.approvalDate = markData.getVerifyDate();
        }
        this.businessTerm = markData.getBusinessTerm();
        this.province = markData.getProvince();
        this.city = markData.getCity();
        this.district = markData.getDistrict();
        this.registrar = markData.getRegistrar();
        this.taxpayerCode = markData.getTaxpayerCode();
        this.taxpayerCredential = markData.getTaxpayerCredential();
        this.businessRegCode = markData.getBusinessRegisterCode();
        this.organizationCode = markData.getOrganizationCode();
        this.insureNum = markData.getInsureNum();
        this.type = markData.getCompanyType();
        this.previousName = markData.getPreviousName();
        this.email = markData.getEmail();
        this.industry = markData.getIndustry();
        this.firstClassify = markData.getFirstClassify();
        this.secondClassify = markData.getSecondClassify();
        this.thirdClassify = markData.getThirdClassify();
        this.lon = markData.getLon();
        this.lat = markData.getLat();
    }
}
