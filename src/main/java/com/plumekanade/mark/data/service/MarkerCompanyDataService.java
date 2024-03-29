package com.plumekanade.mark.data.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.mark.data.entity.MarkerCompanyData;
import com.plumekanade.mark.data.mapper.MarkerCompanyDataMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kanade
 * @date 2023-12-15
 */
@Service
public class MarkerCompanyDataService extends ServiceImpl<MarkerCompanyDataMapper, MarkerCompanyData> {
    public List<MarkerCompanyData> limitList(String maxId, int size) {
        return baseMapper.limitList(maxId, size);
    }
}
