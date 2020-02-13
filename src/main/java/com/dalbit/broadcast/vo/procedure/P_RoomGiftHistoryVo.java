package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.RoomGiftHistoryVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class P_RoomGiftHistoryVo {

    public P_RoomGiftHistoryVo(){}
    public P_RoomGiftHistoryVo(RoomGiftHistoryVo roomGiftHistoryVo){

        int pageNo = DalbitUtil.isEmpty(roomGiftHistoryVo.getPage()) ? 1 : roomGiftHistoryVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(roomGiftHistoryVo.getRecords()) ? 5 : roomGiftHistoryVo.getRecords();

        setMem_no(MemberVo.getMyMemNo());
        setRoom_no(roomGiftHistoryVo.getRoomNo());
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
    private int item_no;
    private String item_name;
    private int gold;
    private int secret;
    private Date giftDate;

}
