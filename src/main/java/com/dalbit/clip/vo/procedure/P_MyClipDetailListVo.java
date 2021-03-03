package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.MyClipDetailVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MyClipDetailListVo extends P_ApiVo {

    public P_MyClipDetailListVo(){}
    public P_MyClipDetailListVo(MyClipDetailVo myClipDetailVo, HttpServletRequest request){
        int page = DalbitUtil.isEmpty(myClipDetailVo.getPage()) ? 1 : myClipDetailVo.getPage();
        int records = DalbitUtil.isEmpty(myClipDetailVo.getRecords()) ? 50 : myClipDetailVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setSlctType(myClipDetailVo.getMyClipType());
        setPageNo(page);
        setPageCnt(records);
    }

    /* Input */
    private String mem_no;
    private int slctType;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String cast_no;
    private int typeOpen;
    private String subjectType;
    private String subjectName;
    private String backgroundImage;
    private String fileName;
    private String filePlay;
    private String memSex;
    private String memNick;
    private int countPlay;
    private int countGood;
    private int countByeol;
    private int countReply;

    private String profileImage;
    private String title;

}
