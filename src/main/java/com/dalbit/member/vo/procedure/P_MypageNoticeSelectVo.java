package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeSelectVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Slf4j
@Getter @Setter
public class P_MypageNoticeSelectVo extends P_ApiVo {

    public P_MypageNoticeSelectVo(){}
    public P_MypageNoticeSelectVo(MypageNoticeSelectVo mypageNoticeSelectVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(mypageNoticeSelectVo.getPage()) ? 1 : mypageNoticeSelectVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(mypageNoticeSelectVo.getRecords()) ? 10 : mypageNoticeSelectVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
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
    private String imagePath;
    private int topFix;
    private Date writeDate;

    private String nickName;
    private String memSex;
    private String profileImage;

    // 2020.11.10 강다인. 댓글 추가
    private int replyCnt;
    private int readCnt;
}
