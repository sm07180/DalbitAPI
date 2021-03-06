package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_BroadcastSettingVo extends P_ApiVo {

     private String mem_no;   //요청 회원번호

     public P_BroadcastSettingVo(){}
     public P_BroadcastSettingVo(HttpServletRequest request){
          setMem_no(MemberVo.getMyMemNo(request));
     }

}
