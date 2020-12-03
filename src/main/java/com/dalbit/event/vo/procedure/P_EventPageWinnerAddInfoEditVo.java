package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.event.vo.request.EventPageWinnerAddInfoEditVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_EventPageWinnerAddInfoEditVo extends P_ApiVo {

    public P_EventPageWinnerAddInfoEditVo(EventPageWinnerAddInfoEditVo pageWinnerAddInfoEditVo, HttpServletRequest request) {
        setMem_no(MemberVo.getMyMemNo(request));
        setEventIdx(pageWinnerAddInfoEditVo.getEventIdx());
        setPrizeIdx(pageWinnerAddInfoEditVo.getPrizeIdx());
        setWinner_name(pageWinnerAddInfoEditVo.getWinner_name());
        setWinner_phone(pageWinnerAddInfoEditVo.getWinner_phone());
        setWinner_email(pageWinnerAddInfoEditVo.getWinner_email());
        setWinner_post_code(pageWinnerAddInfoEditVo.getWinner_post_code());
        setWinner_address_1(pageWinnerAddInfoEditVo.getWinner_address_1());
        setWinner_address_2(pageWinnerAddInfoEditVo.getWinner_address_2());
        setWinner_add_file_1(pageWinnerAddInfoEditVo.getWinner_add_file_1());
        setWinner_add_file_1_name(pageWinnerAddInfoEditVo.getWinner_add_file_1_name());
        setWinner_add_file_2(pageWinnerAddInfoEditVo.getWinner_add_file_2());
        setWinner_add_file_2_name(pageWinnerAddInfoEditVo.getWinner_add_file_2_name());
    }

    private String mem_no;                  // 요청 회원번호

    private int eventIdx;
    private int prizeIdx;

    private String winner_name;
    private String winner_social_no;
    private String winner_phone;
    private String winner_email;
    private String winner_post_code;
    private String winner_address_1;
    private String winner_address_2;
    private String winner_add_file_1;
    private String winner_add_file_1_name;
    private String winner_add_file_2;
    private String winner_add_file_2_name;
}
