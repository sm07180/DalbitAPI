package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class P_SpecialDjReq extends P_ApiVo {

    private String mem_no;
    @NotBlank(message = "{\"ko_KR\" : \"성명을\"}")
    @NotNull(message = "{\"ko_KR\" : \"성명을\"}")
    @Size(message = "{\"ko_KR\" : \"성명을\"}", min = 1, max = 20)
    private String name;
    @NotBlank(message = "{\"ko_KR\" : \"휴대폰 번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"휴대폰 번호를\"}")
    @Size(message = "{\"ko_KR\" : \"휴대폰 번호를\"}", min = 1, max = 20)
    private String phone;
    //@Size(message = "{\"ko_KR\" : \"제목을\"}", max = 2048)
    private String title;
    //@Size(message = "{\"ko_KR\" : \"내용을\"}", max = 2048)
    private String contents;
    //@NotBlank(message = "{\"ko_KR\" : \"주 방송시간을\"}")
    //@NotNull(message = "{\"ko_KR\" : \"주 방송시간을\"}")
    //@Size(message = "{\"ko_KR\" : \"주 방송시간을\"}", min = 1, max = 50)
    private String broadcast_time1;
    private String broadcast_time2;
    private String select_year;
    private String select_month;
}
