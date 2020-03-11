package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ByeolVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_ByeolVo {

    public P_ByeolVo(){}
    public P_ByeolVo(ByeolVo byeolVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(byeolVo.getPage()) ? 1 : byeolVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(byeolVo.getRecords()) ? 10 : byeolVo.getRecords();

        setMem_no(new MemberVo().getMyMemNo(request));
        setSlct_type(byeolVo.getWalletType());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private Integer slct_type;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private Date updateDate;
    private int slctType;
    private String useContents;
    private int useByeol;

}
