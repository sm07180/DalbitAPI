package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.StoryVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_StoryVo {
    /* Input */
    private String mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String room_no;
    private String title;
    private String start_date;
    private String image_background;
    private long storyCnt;

    public P_StoryVo(){}
    public P_StoryVo(StoryVo storyVo, HttpServletRequest request){
        this.mem_no = MemberVo.getMyMemNo(request);
        this.pageNo = storyVo.getPage();
        this.pageCnt = storyVo.getRecords();
    }
}
