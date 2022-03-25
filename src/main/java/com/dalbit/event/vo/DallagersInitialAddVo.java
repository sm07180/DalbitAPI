package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DallagersInitialAddVo {
    private Long memNo;                     //BIGINT		-- 회원번호
    private Long roomNo;                    //BIGINT		-- 방번호
    private Integer collectSlct;            //INT		-- 구분[1:방송청취,2:방송시간,3:보낸달,4: 부스터수,5:받은별]
    private String dallaGubun;              //CHAR(1)		-- 구분[d,a,l]
    private Long insDallaCnt;               //BIGINT		-- 받은 이니셜값
    private Long slctValCnt;                //BIGINT		-- 구분에서의 값 [방송청취,방송시간,보낸달,부스터수,받은별]
    private String feverYn;                 //VARCHAR(20)	-- 피버[y,n]
}
