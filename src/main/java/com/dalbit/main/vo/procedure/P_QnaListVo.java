package com.dalbit.main.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.main.vo.request.QnaListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_QnaListVo extends P_ApiVo {

    public P_QnaListVo(){}
    public P_QnaListVo(QnaListVo qnaListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(qnaListVo.getPage()) ? 1 : qnaListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(qnaListVo.getRecords()) ? 10 : qnaListVo.getRecords();
        setMem_no(MemberVo.getMyMemNo(request));
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int qnaIdx;
    private int slctType;
    private String title;
    private String contents;
    private int state;
    private String answer;
    private Date writeDate;
    private Date opDate;
    private String addFile;
    private String addFile1;
    private String addFile2;
    private String addFile3;
    private String addFileName1;
    private String addFileName2;
    private String addFileName3;
    private String email;
    private String nickName;
    private String phone;


}
