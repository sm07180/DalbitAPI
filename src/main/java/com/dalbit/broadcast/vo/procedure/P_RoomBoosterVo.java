package com.dalbit.broadcast.vo.procedure;


import com.dalbit.broadcast.vo.request.BoosterVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_RoomBoosterVo extends P_ApiVo {

    public P_RoomBoosterVo(){}
    public P_RoomBoosterVo(BoosterVo boosterVo, HttpServletRequest request){
        setMem_no(new MemberVo().getMyMemNo(request));
        setRoom_no(boosterVo.getRoomNo());
        setItem_no(boosterVo.getItemNo());
        setItem_cnt(boosterVo.getItemCnt());
        setItem_code(DalbitUtil.getProperty("item.code.boost"));
        setItem_use("1".equals(boosterVo.getIsItemUse()) || "TRUE".equals(boosterVo.getIsItemUse().toUpperCase()) ? 1 : 0);
    }

    private String mem_no;          //요청 회원번호
    private String room_no;         //해당 방 번호
    private String item_no;         //선물할 아이템 번호
    private Integer item_cnt;       //아이템 개수
    private String item_code;       //선물할 아이템 번호
    private int item_use;           //보유아이템 사용여부
}
