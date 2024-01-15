package com.plumekanade.mark.data.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 行业
 @author kanade
 @date 2024-01-15 */
@Data
@NoArgsConstructor
public class CompanyRosterIndustry implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId
    private String id;
    private String firstIndustry;
    private String secondIndustry;
    private String thirdIndustry;

    public CompanyRosterIndustry(String firstIndustry, String secondIndustry, String thirdIndustry) {
        this.firstIndustry = firstIndustry;
        this.secondIndustry = secondIndustry;
        this.thirdIndustry = thirdIndustry;
    }
}
