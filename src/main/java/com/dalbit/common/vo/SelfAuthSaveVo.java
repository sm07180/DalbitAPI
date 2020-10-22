package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter @Setter @ToString
public class SelfAuthSaveVo {

    String rec_cert		;           // 결과수신DATA
    String k_certNum    ;			// 파라미터로 수신한 요청번호
    String certNum		;			// 요청번호
    String date			;			// 요청일시
    String CI	    	;			// 연계정보(CI)
    String DI	    	;			// 중복가입확인정보(DI)
    String phoneNo		;			// 휴대폰번호
    String phoneCorp	;			// 이동통신사
    String birthDay		;			// 생년월일
    String gender		;			// 성별
    String nation		;			// 내국인
    String name			;			// 성명
    String M_name		;			// 미성년자 성명
    String M_birthDay	;			// 미성년자 생년월일
    String M_Gender		;			// 미성년자 성별
    String M_nation		;			// 미성년자 내외국인
    String result		;			// 결과값

    String certMet		;			// 인증방법
    String ip			;			// ip주소
    String plusInfo		;

    String encPara		;
    String encMsg1		;
    String encMsg2		;
    String msgChk       ;

    String msg          ;
}
