package com.plumekanade.mark.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plumekanade.mark.data.entity.MarkerCompanyData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author kanade
 * @date 2023-12-15
 */
@Mapper
public interface MarkerCompanyDataMapper extends BaseMapper<MarkerCompanyData> {

    @Insert("insert into marker_company_data values (null, #{companyName}, #{engName}, #{manageState}, #{legalName}, #{registerCapital}, #{realCapital}, #{establishDate}, #{verifyDate}, #{businessTerm}, #{province}, #{city}, #{district}, #{registrar}, #{creditCode}, #{taxpayerCode}, #{businessRegisterCode}, #{organizationCode}, #{taxpayerCredential}, #{insureNum}, #{companyType}, #{previousName}, #{registerAddress}, #{url}, #{contactPhone}, #{email}, #{businessScope}, #{companySize}, #{source}, #{industry}, #{firstClassify}, #{secondClassify}, #{thirdClassify}, #{lon}, #{lat})")
    void insertData(@Param("companyName") String companyName, @Param("engName") String engName, @Param("manageState") String manageState, @Param("legalName") String legalName, @Param("registerCapital") String registerCapital, @Param("realCapital") String realCapital, @Param("establishDate") String establishDate, @Param("verifyDate") String verifyDate, @Param("businessTerm") String businessTerm, @Param("province") String province, @Param("city") String city, @Param("district") String district, @Param("registrar") String registrar, @Param("creditCode") String creditCode, @Param("taxpayerCode") String taxpayerCode, @Param("businessRegisterCode") String businessRegisterCode, @Param("organizationCode") String organizationCode, @Param("taxpayerCredential") String taxpayerCredential, @Param("insureNum") String insureNum, @Param("companyType") String companyType, @Param("previousName") String previousName, @Param("registerAddress") String registerAddress, @Param("url") String url,@Param("contactPhone") String contactPhone, @Param("email") String email, @Param("businessScope") String businessScope, @Param("companySize") String companySize, @Param("source") String source, @Param("industry") String industry, @Param("firstClassify") String firstClassify, @Param("secondClassify") String secondClassify, @Param("thirdClassify") String thirdClassify, @Param("lon") String lon, @Param("lat") String lat);
}
