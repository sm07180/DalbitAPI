package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter @Setter
public class ExchangeApplyVo {

    @Min(570)
    private int byeol;
    private String accountName; //예금주
    private String bankCode;
    private String accountNo;
    private String socialNo;
    private String phoneNo;
    private String address1;
    private String address2;
    private String addFile1;
    private String addFile2;
    private int termsAgree;

}
