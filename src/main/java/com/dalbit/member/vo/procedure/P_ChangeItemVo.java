package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ChangeItemListVo;
import com.dalbit.member.vo.request.ChangeItemVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ChangeItemVo {

    public P_ChangeItemVo(){}
    public P_ChangeItemVo(ChangeItemVo changeItemVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setItemCode(changeItemVo.getItemCode());
        setOs(new DeviceVo(request).getOs());
    }

    /* Input */
    private String mem_no;
    private String itemCode;
    private int os;

}
