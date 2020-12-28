package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.FanListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_FanListVo extends P_ApiVo {

    public P_FanListVo(){}
    public P_FanListVo(FanListVo fanListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(fanListVo.getPage()) ? 1 : fanListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(fanListVo.getRecords()) ? 10 : fanListVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setTarget_mem_no(fanListVo.getMemNo());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }


    /* Input */
    private String mem_no;
    private String target_mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String nickName;
    private String memSex;
    private String profileImage;
    private int enableFan;
}
