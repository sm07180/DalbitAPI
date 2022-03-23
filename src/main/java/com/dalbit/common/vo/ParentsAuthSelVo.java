package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ParentsAuthSelVo {
    private String mem_no;                  // 회원번호
    private String parents_mem_name;        // 회원대리인 이름
    private String parents_mem_sex;         // 회원대리인 성별
    private String parents_mem_birth_year;  // 회원대리인 생년
    private String parents_mem_birth_day;   // 회원대리인 월일
    private String parents_mem_email;       // 회원대리인 이메일
    private String parents_mem_hphone;      // 회원대리인 휴대전
    private String agreement_date;          // 동의기간
    private String expire_date;             // 만료일자
    private String auth_yn;                 // 동의완료여부
    private String ins_date;                // 인증일자
    private String upd_date;                // 수정일자

    private String mem_id;
    private String mem_name;
}
