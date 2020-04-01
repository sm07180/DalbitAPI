package com.dalbit.common.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class SelfAuthChkVo {

    @NotBlank
    String rec_cert;        // 결과수신DATA
    @NotBlank
    String certNum;			// 파라미터로 수신한 요청번호

}