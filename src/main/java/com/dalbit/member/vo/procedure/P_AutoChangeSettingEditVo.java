package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.AutoChangeSettingEditVo;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Getter
@Setter
public class P_AutoChangeSettingEditVo {

    public P_AutoChangeSettingEditVo(){}
    public P_AutoChangeSettingEditVo(AutoChangeSettingEditVo autoChangeSettingEditVo, HttpServletRequest request) {
        setMem_no(new MemberVo().getMyMemNo(request));
        setAuto_change("1".equals(autoChangeSettingEditVo.getAutoChange()) || "TRUE".equals(autoChangeSettingEditVo.getAutoChange().toUpperCase()) ? 1 : 0);
    }

    private String mem_no;
    private int auto_change;        // 변경 값 ( 0 : off, 1 : on )
}
