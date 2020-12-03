package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ChangeItemListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ChangeItemListVo extends P_ApiVo {

    public P_ChangeItemListVo(){}
    public P_ChangeItemListVo(ChangeItemListVo changeItemListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(changeItemListVo.getPage()) ? 1 : changeItemListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(changeItemListVo.getRecords()) ? 10 : changeItemListVo.getRecords();
        setMem_no(MemberVo.getMyMemNo(request));
        setOs(new DeviceVo(request).getOs());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private int os;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String itemCode;
    private String itemName;
    private String itemImage;
    private String itemThumbnail;
    private int dalCnt;
    private int byeolCnt;
    private String desc;

}
