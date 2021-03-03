package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.AwardListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_AwardListVo extends P_ApiVo {
    public P_AwardListVo(){}
    public P_AwardListVo(AwardListVo awardListVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSelectYear(awardListVo.getYear());
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
    }

    /* Input */
    private String mem_no;
    private String selectYear;
    private int memLogin;

    /* Output */
    private int idx;
    private String dj_memNo;
    private String dj_nickName;
    private String dj_memSex;
    private String dj_profileImage;
    private String fan1_memNo;
    private String fan1_nickName;
    private String fan1_memSex;
    private String fan1_profileImage;
    private String fan2_memNo;
    private String fan2_nickName;
    private String fan2_memSex;
    private String fan2_profileImage;
    private String fan3_memNo;
    private String fan3_nickName;
    private String fan3_memSex;
    private String fan3_profileImage;
}
