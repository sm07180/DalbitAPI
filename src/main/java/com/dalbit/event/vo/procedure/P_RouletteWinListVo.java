package com.dalbit.event.vo.procedure;

import com.dalbit.event.vo.request.RouletteWinVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter @Setter
public class P_RouletteWinListVo {

    public P_RouletteWinListVo(){}
    public P_RouletteWinListVo(RouletteWinVo rouletteWinVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(rouletteWinVo.getPage()) ? 1 : rouletteWinVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(rouletteWinVo.getRecords()) ? 10 : rouletteWinVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setSlct_type(rouletteWinVo.getWinType());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private int slct_type;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int item_no;
    private Date win_date;
    private int new_yn;
    private String nickName;
    private String memSex;
    private String profileImage;
}
