package com.plumekanade.mark.data.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plumekanade.mark.data.entity.CompanyRoster;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author kanade
 * @date 2023-12-20
 */
@Mapper
public interface CompanyRosterMapper extends BaseMapper<CompanyRoster> {

    /** 根据名称获取企业名单 */
    @Select("select * from company_roster where name = #{name}")
    CompanyRoster getByName(@Param("name") String name);
}
