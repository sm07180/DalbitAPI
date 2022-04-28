package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MemberNotifyEditVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_MemberNotifyEditVo extends P_ApiVo {

    private String mem_no;      //요청 회원번호
    private Integer all_ok;         //전체알림
    private Integer set_1;
    private Integer set_2;
    private Integer set_3;
    private Integer set_4;
    private Integer set_5;
    private Integer set_6;
    private Integer set_7;
    private Integer set_8;
    private Integer set_9;
    private Integer set_10;
    private Integer set_11;
    private Integer set_12;
    private Integer set_15;
    private String alim_slct;   //알림타입

    public P_MemberNotifyEditVo() { }
    public P_MemberNotifyEditVo(MemberNotifyEditVo memberNotifyEditVo, HttpServletRequest request) {
        setMem_no(MemberVo.getMyMemNo(request));
        setAll_ok(memberNotifyEditVo.getIsAll());
        setSet_1(memberNotifyEditVo.getIsMyStar());
        setSet_2(memberNotifyEditVo.getIsGift());
        setSet_3(memberNotifyEditVo.getIsFan());
        setSet_4(memberNotifyEditVo.getIsComment());
        setSet_5(memberNotifyEditVo.getIsRadio());
        setSet_6(memberNotifyEditVo.getIsPush());
        setSet_7(memberNotifyEditVo.getIsLike());
        setSet_8(memberNotifyEditVo.getIsReply());
        setSet_9(memberNotifyEditVo.getIsStarClip());
        setSet_10(memberNotifyEditVo.getIsMyClip());
        setSet_11(memberNotifyEditVo.getIsReceive());
        setSet_11(memberNotifyEditVo.getIsReceive());
        setSet_12(memberNotifyEditVo.getIsMailbox());
        setSet_15(memberNotifyEditVo.getIsTeam());
        setAlim_slct(memberNotifyEditVo.getAlimType());
    }

}
