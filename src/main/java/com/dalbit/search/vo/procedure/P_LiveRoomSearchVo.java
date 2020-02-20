package com.dalbit.search.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.search.vo.request.SearchVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_LiveRoomSearchVo {

    public P_LiveRoomSearchVo(){}
    public P_LiveRoomSearchVo(SearchVo searchVo){
        int pageNo = DalbitUtil.isEmpty(searchVo.getPage()) ? 1 : searchVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(searchVo.getRecords()) ? 10 : searchVo.getRecords();

        setMem_no(MemberVo.getMyMemNo());
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

}
