package com.dalbit.main.vo.procedure;

import com.dalbit.main.vo.request.MainGoodRankingVo;
import com.dalbit.main.vo.request.MainLevelRankingVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MainGoodRankingVo {

    public P_MainGoodRankingVo(){}
    public P_MainGoodRankingVo(MainGoodRankingVo mainGoodRankingVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(mainGoodRankingVo.getPage()) ? 1 : mainGoodRankingVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(mainGoodRankingVo.getRecords()) ? 10 : mainGoodRankingVo.getRecords();

        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private int memLogin;
    private String mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int rank;
    private String nickName;
    private String memSex;
    private String profileImage;
    private int level;
    private int fanCnt;
    private int total_goodCnt;
    private int fan_goodCnt;
    private String fan_mem_no;
    private String fan_nickName;
    private String updown;
}
