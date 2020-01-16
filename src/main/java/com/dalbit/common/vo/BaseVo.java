package com.dalbit.common.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 공통 VO
 * 생성일시, 생성자, 수정일시, 수정자와 같은 공통 변수를 선언하여 사용한다.
 */
public class BaseVo implements Serializable {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}
