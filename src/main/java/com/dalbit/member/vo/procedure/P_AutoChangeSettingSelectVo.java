package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Getter
@Setter
public class P_AutoChangeSettingSelectVo {
    public P_AutoChangeSettingSelectVo(){}
    public P_AutoChangeSettingSelectVo(HttpServletRequest request){
        setMem_no(new MemberVo().getMyMemNo(request));
    }

    private String mem_no;
}
