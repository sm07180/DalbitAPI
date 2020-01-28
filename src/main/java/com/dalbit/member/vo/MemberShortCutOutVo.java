package com.dalbit.member.vo;

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


    public MemberShortCutOutVo(P_MemberShortCutVo target, String mem_no) {
        this.memNo=mem_no;
        this.orderNo=target.getOrderNo();
        this.onOff=target.getOnOff();
        this.order=target.getOrder();
        this.text=target.getText();
    }
}
