package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;

@Getter
@Setter
@ToString
public class ValidationResultVo {
    private boolean isSuccess = true;
    private ArrayList validationMessageDetail;

}
