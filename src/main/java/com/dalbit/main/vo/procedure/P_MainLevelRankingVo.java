package com.dalbit.main.vo.procedure;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.request.MainDjRankingVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MainLevelRankingVo {

    public P_MainLevelRankingVo(){}
    public P_MainLevelRankingVo(HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
    }

    /* Input */
    private int memLogin;
    private String mem_no;

    /* Output */
    private int rank;
    private String nickName;
    private String memSex;
    private String profileImage;
    private int level;
    private String grade;
    private int fanCnt;
    private String fan_mem_no;
    private String fan_nickName;
    private String roomNo;
}
