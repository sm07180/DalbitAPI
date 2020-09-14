package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MemberNotifyEditVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_MemberNotifyEditVo {

    private String mem_no;      //요청 회원번호
    private int all_ok;         //전체알림
    private int set_1;          //마이 스타 알림
    private int set_2;          //선물 받은 달 알림
    private int set_3;          //팬 알림
    private int set_4;          //댓글 알림
    private int set_5;          //달빛 라이브 알림
    private int set_6;          //push 알림
    private int set_7;          //좋아요 알림
    private String alim_slct;   //알림타입

    public P_MemberNotifyEditVo() { }
    public P_MemberNotifyEditVo(MemberNotifyEditVo memberNotifyEditVo, HttpServletRequest request) {
        setMem_no(new MemberVo().getMyMemNo(request));
        setAll_ok(memberNotifyEditVo.getIsAll());
        setSet_1(memberNotifyEditVo.getIsMyStar());
        setSet_2(memberNotifyEditVo.getIsGift());
        setSet_3(memberNotifyEditVo.getIsFan());
        setSet_4(memberNotifyEditVo.getIsComment());
        setSet_5(memberNotifyEditVo.getIsRadio());
        setSet_6(memberNotifyEditVo.getIsPush());
        setSet_7(memberNotifyEditVo.getIsLike());
        setAlim_slct(DalbitUtil.isEmpty(memberNotifyEditVo.getAlimType()) ? "v" : ("-1".equals(memberNotifyEditVo.getAlimType()) ? "v" : memberNotifyEditVo.getAlimType()));
    }

}
