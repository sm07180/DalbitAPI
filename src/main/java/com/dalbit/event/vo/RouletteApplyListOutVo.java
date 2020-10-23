package com.dalbit.event.vo;

import com.dalbit.event.vo.procedure.P_RouletteApplyListVo;
import com.dalbit.event.vo.request.RouletteApplyVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter
@Setter
public class RouletteApplyListOutVo {

    private int itemNo;
    private String phone;
    private String applyDt;
    private long applyTs;

    public RouletteApplyListOutVo(P_RouletteApplyListVo target){
        setItemNo(target.getItemNo());
        setPhone(target.getPhoneNum());
        setApplyDt(DalbitUtil.getUTCFormat(target.getApplyDate()));
        setApplyTs(DalbitUtil.getUTCTimeStamp(target.getApplyDate()));
    }
}
