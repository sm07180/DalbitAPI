package com.dalbit.search.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.search.vo.request.SearchVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_LiveRoomSearchVo extends P_ApiVo {

    public P_LiveRoomSearchVo(){}
    public P_LiveRoomSearchVo(SearchVo searchVo, HttpServletRequest request){
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
    private int isWowza;

    /* Output */
    private String room_no;
    private String title;
    private int count_entry;
    private int count_good;
    private int badge_recomm;
    private int badge_popular;
    private int badge_newdj;
    private String mem_nick;
    private String mem_id;
    private String mem_sex;
    private String image_profile;
    private int is_wowza;
}
