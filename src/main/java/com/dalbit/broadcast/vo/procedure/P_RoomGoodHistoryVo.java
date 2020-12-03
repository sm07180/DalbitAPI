package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.RoomGoodHistoryVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_RoomGoodHistoryVo extends P_ApiVo {


    public P_RoomGoodHistoryVo(){}
    public P_RoomGoodHistoryVo(RoomGoodHistoryVo roomGoodHistoryVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(roomGoodHistoryVo.getPage()) ? 1 : roomGoodHistoryVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(roomGoodHistoryVo.getRecords()) ? 10 : roomGoodHistoryVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(roomGoodHistoryVo.getRoomNo());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private String room_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String nickName;
    private String memSex;
    private String profileImage;
    private int auth;
    private Date join_date;
    private int enableFan;
    private int newBadge;
    private int specialBadge;
    private int liveFanRank;
    private int goodCnt;
    private int state;
    private String liveBadgeText;
    private String liveBadgeIcon;
    private String liveBadgeStartColor;
    private String liveBadgeEndColor;
    private String liveBadgeImage;
    private String liveBadgeImageSmall;

}
