package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.event.vo.request.CamApplyVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_CamApplyVo extends P_ApiVo {
    public P_CamApplyVo(){}
    public P_CamApplyVo(CamApplyVo camApplyVo, HttpServletRequest request){

    }


}
