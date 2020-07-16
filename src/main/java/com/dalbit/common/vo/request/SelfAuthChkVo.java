package com.dalbit.common.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
public class SelfAuthChkVo {

    @NotBlank(message = "{\"ko_KR\" : \"수신결과를\"}")
    @NotNull(message = "{\"ko_KR\" : \"수신결과를\"}")
    String rec_cert;        // 결과수신DATA
    @NotBlank(message = "{\"ko_KR\" : \"요청번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"요청번호를\"}")
    String certNum;			// 파라미터로 수신한 요청번호

}
