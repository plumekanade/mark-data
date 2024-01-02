package com.plumekanade.mark.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 八方网企业表
 * @author kanade
 * @date 2023-12-19
 */
@Data
@TableName("bf_company_roster")
public class BfCompanyRoster implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private String id;
    /** 网址 */
    private String companyUrl;
    /** 企业名称 */
    private String companyName;
    /** 联系人 */
    private String contactName;
    /** 联系方式 */
    private String contactPhone;
    /** 企业关键字 */
    private String companyKeyword;
    /** 企业加入八方网的日期 */
    private String companyDate;
    /** 八方黄页的一级标题 */
    private String title;
    /** 八方黄页的二级标题 */
    private String subTitle;
    /** 爬取数据生成的企业id */
    private String crawCompanyId;
    /** 是否有商品，1有 0无 */
    private Integer hasSku;
    /** 企业地址 */
    private String companyAddress;
    /** 固话 */
    private String fixedPhone;
    /** 联系人 */
    private String linkUser;
    /** 联系电话 */
    private String linkPhone;
    /** 邮编号码 */
    private String postCode;
    /** 传真号码 */
    private String faxCode;
    /** 企业经济性质 */
    private String enterpriseEconomic;
    /** 法人 */
    private String legalName;
    /** 企业类型 */
    private String enterpriseType;
    /** 企业注册地址 */
    private String registerAddress;
    /** 注册资本 */
    private String registerCapital;
    /** 成立日期 */
    private String establishDate;
    /** 员工人数 */
    private String staffNum;
    /** 月产量 */
    private String monthlyProduction;
    /** 年营业额 */
    private String yearTurnover;
    /** 年出口额 */
    private String yearExport;
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
    /** 企业名称 */
    private String enterpriseName;
    /** 工商注册号 */
    private String businessRegisterCode;
    /** 经营状态 */
    private String manageState;
    /** 住所 */
    private String residence;
    /** 类型 */
    private String type;
    /** 法人 */
    private String legalUser;
    /** 注册资本 */
    private String regCapital;
    /** 成立日期 */
    private String buildDate;
    /** 营业期限自 */
    private String fromBusinessTerm;
    /** 营业期限至 */
    private String toBusinessTerm;
    /** 登记机关 */
    private String registrar;
    /** 核准日期 */
    private String approvalDate;
    /** 经营范围 */
    private String businessScope;
    /** 简介 */
    private String profile;
    /** 爬取数据生成的企业id，与craw_company_id一致 */
    private String companyIdY;
    /** 统一社会信用代码 */
    private String creditCode;
    /** 登记状态 */
    private String registrationState;
    /** 是否已添加到名单库，0否 1是 */
    private Integer state;
}
