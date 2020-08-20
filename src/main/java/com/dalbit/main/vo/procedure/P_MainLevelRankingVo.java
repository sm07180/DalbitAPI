package com.dalbit.main.vo.procedure;

import com.dalbit.main.vo.request.MainLevelRankingVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MainLevelRankingVo {

    public P_MainLevelRankingVo(){}
    public P_MainLevelRankingVo(MainLevelRankingVo mainLevelRankingVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(mainLevelRankingVo.getPage()) ? 1 : mainLevelRankingVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(mainLevelRankingVo.getRecords()) ? 10 : mainLevelRankingVo.getRecords();

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
    private String grade;
    private int fanCnt;
    private String fan_mem_no;
    private String fan_nickName;
    private String roomNo;
}
