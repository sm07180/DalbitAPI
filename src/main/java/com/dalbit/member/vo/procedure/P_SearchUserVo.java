package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.SearchUserVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_SearchUserVo {

    public P_SearchUserVo(){}
    public P_SearchUserVo(SearchUserVo searchUserVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(searchUserVo.getPage()) ? 1 : searchUserVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(searchUserVo.getRecords()) ? 10 : searchUserVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setSlct_type(searchUserVo.getUserType());
        setSearchText(searchUserVo.getSearch());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
        setSearchType(searchUserVo.getSearchType());
    }

    /* Input */
    private String mem_no;
    private Integer slct_type;
    private String searchText;
    private String searchType;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String mem_nick;
    private String mem_id;
    private String memSex;
    private String profileImage;
}
