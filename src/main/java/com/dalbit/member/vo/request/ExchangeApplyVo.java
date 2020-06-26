package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class ExchangeApplyVo {

    @Min(570)
    private int byeol;
    @NotBlank
    private String accountName; //예금주
    @NotBlank
    private String bankCode;
    @NotBlank
    private String accountNo;
    @NotBlank @Size(max = 13)
    private String socialNo;
    @NotBlank @Size(max = 15)
    private String phoneNo;
    @NotBlank
    private String address1;
    private String address2;
    @NotBlank
    private String addFile1;
    @NotBlank
    private String addFile2;
    private String addFile3;
    @Min(1)
    private int termsAgree;

}
