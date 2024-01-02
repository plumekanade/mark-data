package com.plumekanade.mark.data.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.mark.data.entity.CompanyRoster;
import com.plumekanade.mark.data.mapper.CompanyRosterMapper;
import org.springframework.stereotype.Service;

/**
 * @author kanade
 * @date 2023-12-20
 */
@Service
public class CompanyRosterService extends ServiceImpl<CompanyRosterMapper, CompanyRoster> {
    /** 根据名称获取企业名单 */
    public CompanyRoster getByName(String name) {
        return baseMapper.getByName(name);
    }
}
