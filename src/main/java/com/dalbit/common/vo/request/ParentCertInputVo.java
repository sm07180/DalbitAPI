package com.dalbit.common.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParentCertInputVo {
    public ParentCertInputVo() {}
    /*public ParentCertInputVo(String memNo, String pMemEmail, String agreementDate) {
        this.memNo = memNo;
        this.pMemEmail = pMemEmail;
        this.agreementDate = agreementDate;
    }*/
    public ParentCertInputVo(String memNo, String pMemName, String pMemSex, String pMemBirthYear, String pMemBirthDay, String pMemHphone) {
        this.memNo = memNo;
        this.pMemName = pMemName;
        this.pMemSex = pMemSex.equals("0") ? "m" : "f";
        this.pMemBirthYear = pMemBirthYear;
        this.pMemBirthDay = pMemBirthDay;
        this.pMemHphone = pMemHphone;
    }

    private String memNo;
    private String pMemEmail; // 대리인 이메일 주소
    private String agreementDate; // 동의 기간

    // 법정대리인 인증등록(결제)
    private String pMemName;
    private String pMemSex;
    private String pMemBirthYear;
    private String pMemBirthDay;
    private String pMemHphone;
}
