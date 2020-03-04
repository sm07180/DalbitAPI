package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.DalVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class P_DalVo {

    public P_DalVo(){}
    public P_DalVo(DalVo dalVo){
        int pageNo = DalbitUtil.isEmpty(dalVo.getPage()) ? 1 : dalVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(dalVo.getRecords()) ? 10 : dalVo.getRecords();

        setMem_no(MemberVo.getMyMemNo());
        setSlct_type(dalVo.getWalletType());
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
    private Integer useDal;

}