package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.event.vo.request.PhoneInputVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class P_PhoneInputVo extends P_ApiVo {

    private String mem_no;
    private String phone;

    public P_PhoneInputVo(){}

    public P_PhoneInputVo(PhoneInputVo phoneInputVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setPhone(phoneInputVo.getPhone());
    }

}
