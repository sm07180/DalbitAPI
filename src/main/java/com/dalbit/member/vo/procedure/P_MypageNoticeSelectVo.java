package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeSelectVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;


@Slf4j
@Getter @Setter
public class P_MypageNoticeSelectVo {

    public P_MypageNoticeSelectVo(){}
    public P_MypageNoticeSelectVo(MypageNoticeSelectVo mypageNoticeSelectVo){
        int pageNo = DalbitUtil.isEmpty(mypageNoticeSelectVo.getPage()) ? 1 : mypageNoticeSelectVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(mypageNoticeSelectVo.getRecords()) ? 10 : mypageNoticeSelectVo.getRecords();

        setMem_no(MemberVo.getMyMemNo());
        setTarget_mem_no(mypageNoticeSelectVo.getMemNo());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private String target_mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private Long noticeIdx;
    private String title;
    private String contents;
    private int topFix;
    private Date writeDate;


}
