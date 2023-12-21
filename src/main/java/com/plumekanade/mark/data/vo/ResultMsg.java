package com.plumekanade.mark.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 返回值
 *
 * @author kanade
 * @date 2022-09-07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultMsg implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code = 0;
    private boolean shortFlag = false;
    private String message = "ok";
    private Object data;

    public static ResultMsg success() {
        return new ResultMsg();
    }
}
