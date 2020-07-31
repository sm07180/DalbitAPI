package com.dalbit.search.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.search.vo.request.SearchVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MemberSearchVo {

    public P_MemberSearchVo(){}
    public P_MemberSearchVo(SearchVo searchVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(searchVo.getPage()) ? 1 : searchVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(searchVo.getRecords()) ? 10 : searchVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setSearchText(searchVo.getSearch());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private String searchText;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String mem_nick;
    private String mem_id;
    private String mem_sex;
    private String image_profile;
    private String room_no;
    private int newdj_badge;
    private int specialdj_badge;
    private long fan_count;

}
