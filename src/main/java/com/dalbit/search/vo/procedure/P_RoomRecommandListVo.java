package com.dalbit.search.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.search.vo.request.RoomRecommandListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_RoomRecommandListVo extends P_ApiVo {

    public P_RoomRecommandListVo(){}
    public P_RoomRecommandListVo(RoomRecommandListVo roomRecommandListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(roomRecommandListVo.getPage()) ? 1 : roomRecommandListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(roomRecommandListVo.getRecords()) ? 20 : roomRecommandListVo.getRecords();

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
    private int type_entry;
    private int count_entry;
    private Date start_date;
    private int badge_special;
    private String nickName;
    private int imageType;
    private String bj_profileImage;
    private String bj_memSex;
}
