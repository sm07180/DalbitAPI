package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.StarListNewVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_StarListNewVo {

    public P_StarListNewVo(){}
    public P_StarListNewVo(StarListNewVo starListNewVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(starListNewVo.getPage()) ? 1 : starListNewVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(starListNewVo.getRecords()) ? 10 : starListNewVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setTarget_mem_no(starListNewVo.getMemNo());
        setSortSlct(starListNewVo.getSortType());
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

    private String starMemo;
    private int listenTime;
    private int giftedByeol;
    private Date lastlistenDate;
    private Date regDate;

}
