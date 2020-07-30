package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class ExchangeApplyVo {

    @Min(message = "{\"ko_KR\" : \"별을\"}", value = 570)
    private int byeol;

    @NotBlank(message = "{\"ko_KR\" : \"예금주를\"}")
    @NotNull(message = "{\"ko_KR\" : \"예금주를\"}")
    private String accountName; //예금주

    @NotBlank(message = "{\"ko_KR\" : \"은행을\"}")
    @NotNull(message = "{\"ko_KR\" : \"은행을\"}")
    private String bankCode;

    @NotBlank(message = "{\"ko_KR\" : \"계좌번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"계좌번호를\"}")
    private String accountNo;

    @NotBlank(message = "{\"ko_KR\" : \"주민등록번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"주민등록번호를\"}")
    @Size(message = "{\"ko_KR\" : \"주민등록번호를\"}", max = 13)
    private String socialNo;

    @NotBlank(message = "{\"ko_KR\" : \"휴대폰번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"휴대폰번호를\"}")
    @Size(message = "{\"ko_KR\" : \"휴대폰번호를\"}", max = 15)
    private String phoneNo;

    @NotBlank(message = "{\"ko_KR\" : \"주소를\"}")
    @NotNull(message = "{\"ko_KR\" : \"주소를\"}")
    private String address1;
    private String address2;

    @NotBlank(message = "{\"ko_KR\" : \"신분증사본을\"}")
    @NotNull(message = "{\"ko_KR\" : \"신분증사본을\"}")
    private String addFile1;

    @NotBlank(message = "{\"ko_KR\" : \"통장사본을\"}")
    @NotNull(message = "{\"ko_KR\" : \"통장사본을\"}")
    private String addFile2;

    private String addFile3;

    @Min(message = "{\"ko_KR\" : \"약관동의를\"}", value = 1)
    @NotNull(message = "{\"ko_KR\" : \"약관동의를\"}")
    private int termsAgree;

}
