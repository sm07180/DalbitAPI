package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_MemberShortCutVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberShortCutOutVo {
    private int     orderNo;
    private boolean isOn;
    private String  order;
    private String  text;
    private String endDt;
    private Long endTs;

    public MemberShortCutOutVo(P_MemberShortCutVo target) {
        this.orderNo=target.getOrderNo();
        this.isOn="on".equals(target.getOnOff());
        this.order=target.getOrder();
        this.text=target.getText();
        if(!DalbitUtil.isEmpty(target.getEndDate())){
            this.endDt=DalbitUtil.getUTCFormat(target.getEndDate());
            this.endTs=DalbitUtil.getUTCTimeStamp(target.getEndDate());
        }
    }
}
