package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.ClipRankCombineListVo;
import com.dalbit.clip.vo.request.ClipRankListVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.main.vo.request.MainRankingPageVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipRankListVo extends P_ApiVo {
    public P_ClipRankListVo(){}
    public P_ClipRankListVo(ClipRankListVo clipRankListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(clipRankListVo.getPage()) ? 1 : clipRankListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(clipRankListVo.getRecords()) ? 10 : clipRankListVo.getRecords();

        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setSlct_type(clipRankListVo.getRankType());
        setRankingDate(clipRankListVo.getRankingDate());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    public P_ClipRankListVo(ClipRankCombineListVo clipRankCombineListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(clipRankCombineListVo.getPage()) ? 1 : clipRankCombineListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(clipRankCombineListVo.getRecords()) ? 10 : clipRankCombineListVo.getRecords();

        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setSlct_type(clipRankCombineListVo.getRankType());
        setRankingDate(clipRankCombineListVo.getRankingDate());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private int memLogin;
    private String mem_no;
    private int slct_type;
    private String rankingDate;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int rank;
    private String cast_no;
    private String up_down;
    private String subjectType;
    private String subjectName;
    private String title;
    private String backgroundImage;
    private String fileName;
    private String filePath;
    private String filePlay;
    private String fileSize;
    private String nickName;
    private String memId;
    private String memSex;
    private String profileImage;
    private int clipPoint;
    private int giftPoint;
    private int goodPoint;
    private int listenPoint;
}
