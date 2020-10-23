package com.dalbit.event.vo.procedure;

import com.dalbit.event.vo.request.RouletteApplyVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter
@Setter
public class P_RouletteApplyListVo {

    public P_RouletteApplyListVo(){}
    public P_RouletteApplyListVo(RouletteApplyVo rouletteApplyVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(rouletteApplyVo.getPage()) ? 1 : rouletteApplyVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(rouletteApplyVo.getRecords()) ? 10 : rouletteApplyVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int itemNo;
    private Date applyDate;
    private String phoneNum;

}
