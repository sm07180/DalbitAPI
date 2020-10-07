package com.dalbit.main.vo.procedure;

import com.dalbit.main.vo.request.SpecialHistoryVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.math.NumberUtils;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_SpecialHistoryVo {
    private String year;
    private String month;
    private String mem_no;

    public P_SpecialHistoryVo(){}
    public P_SpecialHistoryVo(SpecialHistoryVo specialHistoryVo, HttpServletRequest request){
        this.year = specialHistoryVo.getYy();
        this.month = specialHistoryVo.getMm();
        this.mem_no = MemberVo.getMyMemNo(request);
        if(!NumberUtils.isDigits(this.year)){
            this.year = null;
        }
        if(!NumberUtils.isDigits(this.month)){
            this.month = null;
        }
    }

    private String mem_nick;
    private String mem_sex;
    private String image_profile;
    private int level;
    private int special_cnt;
    private int good_cnt;
    private int listener_cnt;
    private int minute_broadcast;
    private String room_no;
    private int isNew;
}
