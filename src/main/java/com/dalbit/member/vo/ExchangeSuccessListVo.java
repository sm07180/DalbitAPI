package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ExchangeSuccessListVo {

    private String accountName;
    private String accountNo;
    private String bankCode;
    private String socialNo;
    private String phoneNo;
    private String address1;
    private String address2;
    private String addFile1;
    private String addFile2;
    private int termsAgree;

}
