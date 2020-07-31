package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.FanListNewVo;
import com.dalbit.member.vo.request.FanListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_FanListNewVo {

    public P_FanListNewVo(){}
    public P_FanListNewVo(FanListNewVo fanListNewVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(fanListNewVo.getPage()) ? 1 : fanListNewVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(fanListNewVo.getRecords()) ? 10 : fanListNewVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setTarget_mem_no(fanListNewVo.getMemNo());
        setSortSlct(fanListNewVo.getSortType());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }


    /* Input */
    private String mem_no;
    private String target_mem_no;
    private Integer sortSlct;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String nickName;
    private String memSex;
    private String profileImage;
    private int enableFan;

    private String fanMemo;
    private int listenTime;
    private int giftedByeol;
    private Date lastlistenDate;
    private Date regDate;

}
