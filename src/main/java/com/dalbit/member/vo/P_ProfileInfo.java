package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_ProfileInfo {

    @Builder.Default private String mem_no = "11577931027280";  //회원번호
    @Builder.Default private String target_mem_no = "11577931027280";   //스타회원번호
}
