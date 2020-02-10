package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_MemberShortCutVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MemberShortCutOutVo {
    private String  memNo;
    private int     orderNo;
    private String  onOff;
    private String  order;
    private String  text;


    public MemberShortCutOutVo(P_MemberShortCutVo target) {
        this.orderNo=target.getOrderNo();
        this.onOff=target.getOnOff();
        this.order=target.getOrder();
        this.text=target.getText();
    }
}
