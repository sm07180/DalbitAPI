package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.FanRankingVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_FanRankingVo {

    public P_FanRankingVo(){}
    public P_FanRankingVo(FanRankingVo fanRankingVo){
        int pageNo = DalbitUtil.isEmpty(fanRankingVo.getPage()) ? 1 : fanRankingVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(fanRankingVo.getRecords()) ? 10 : fanRankingVo.getRecords();

        setMem_no(MemberVo.getMyMemNo());
        setStar_mem_no(fanRankingVo.getMemNo());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }


    /* Input */
    private String mem_no;
    private String star_mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String nickName;
    private String memSex;
    private String profileImage;
    private int enableFan;
}
