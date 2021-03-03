package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.request.AwardHonorListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_AwardHonorListVo extends P_ApiVo {
    public P_AwardHonorListVo(){}
    public P_AwardHonorListVo(AwardHonorListVo awardHonorListVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlctType(awardHonorListVo.getSlctType());
        setSelectYear(awardHonorListVo.getSelectYear());
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setDj_mem_no(awardHonorListVo.getMemNo());
    }

    /* Input */
    private String mem_no;
    private int slctType;
    private String selectYear;
    private int memLogin;
    private String dj_mem_no;

    /* Output */
    private String mem_nick;
    private String memSex;
    private String profileImage;
    private int listenPoint;
    private int goodPoint;
    private String fanTitle;
    private Date memJoinDate;

}
