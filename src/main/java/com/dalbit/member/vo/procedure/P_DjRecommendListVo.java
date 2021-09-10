package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.DjRecommendListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_DjRecommendListVo extends P_ApiVo {
    public P_DjRecommendListVo(){}
    public P_DjRecommendListVo(DjRecommendListVo djRecommendListVo, HttpServletRequest request){
        //int pageNo = DalbitUtil.isEmpty(djRecommendListVo.getPage()) ? 1 : djRecommendListVo.getPage();
        //int pageCnt = DalbitUtil.isEmpty(djRecommendListVo.getRecords()) ? 20 : djRecommendListVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setAgeList(djRecommendListVo.getAgeList());
        setSlctList(djRecommendListVo.getGender());
        //setPageNo(pageNo);
        //setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private String ageList;
    private String slctList;
    //private int pageNo;
    //private int pageCnt;

    /* Output */
    private String mem_sex;
    private String mem_nick;
    private String profileImage;
    private String roomNo;
    private int age_type;
    private String age_desc;
    private String title;
    private String desc;
    private int enableFan;

    private String dj_keyword;

}
