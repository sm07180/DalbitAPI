package com.dalbit.main.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.main.vo.request.MainMyDjVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MainMyDjVo extends P_ApiVo {

    public P_MainMyDjVo(){}
    public P_MainMyDjVo(MainMyDjVo mainMyDjVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(mainMyDjVo.getPage()) ? 1 : mainMyDjVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(mainMyDjVo.getRecords()) ? 10 : mainMyDjVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String roomNo;
    private String subject_type;
    private String title;
    private String image_background;
    private String msg_welcom;
    private int type_entry;
    private String notice;
    private int state;
    private String code_link;
    private int count_entry;
    private int count_good;
    private int count_gold;
    private Date start_date;
    private int badge_recomm;
    private int badge_popular;
    private int badge_newdj;
    private int airtime;
    private String bj_mem_no;
    private String bj_nickName;
    private String bj_memSex;
    private int bj_birthYear;
    private String bj_profileImage;

}
