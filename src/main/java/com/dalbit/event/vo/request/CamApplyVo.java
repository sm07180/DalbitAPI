package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
public class CamApplyVo {
    @NotBlank(message = "{\"ko_KR\" : \"이름을\"}")
    @NotNull(message = "{\"ko_KR\" : \"이름을\"}")
    public String name;

    @NotBlank(message = "{\"ko_KR\" : \"휴대폰 번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"휴대폰 번호를\"}")
    private String phone;

    @NotBlank(message = "{\"ko_KR\" : \"우편번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"우편번호를\"}")
    private String postCode;

    @NotBlank(message = "{\"ko_KR\" : \"주소를\"}")
    @NotNull(message = "{\"ko_KR\" : \"주소를\"}")
    private String address_1;

    @NotBlank(message = "{\"ko_KR\" : \"상세 주소를\"}")
    @NotNull(message = "{\"ko_KR\" : \"상세 주소를\"}")
    private String address_2;

    @NotBlank(message = "{\"ko_KR\" : \"보유 장비를\"}")
    @NotNull(message = "{\"ko_KR\" : \"보유 장비를\"}")
    private String device;

    @NotBlank(message = "{\"ko_KR\" : \"방송 소개를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방송 소개를\"}")
    private String contents;
}
