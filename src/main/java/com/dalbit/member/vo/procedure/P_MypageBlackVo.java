package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageBlackVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_MypageBlackVo extends P_ApiVo {

    /* Input */
    private String mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private Date regDate;
    private String mem_nick;
    private String mem_id;
    private String memSex;
    private String profileImage;

    public P_MypageBlackVo(){}
    public P_MypageBlackVo(MypageBlackVo mypageBlackVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(mypageBlackVo.getPage()) ? 1 : mypageBlackVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(mypageBlackVo.getRecords()) ? 10 : mypageBlackVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }
}
