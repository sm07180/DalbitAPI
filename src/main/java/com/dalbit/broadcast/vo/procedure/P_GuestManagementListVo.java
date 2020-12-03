package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.GuestManagementVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_GuestManagementListVo extends P_ApiVo {

    public P_GuestManagementListVo(){}
    public P_GuestManagementListVo(GuestManagementVo guestManagementVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(guestManagementVo.getPage()) ? 1 : guestManagementVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(guestManagementVo.getRecords()) ? 30 : guestManagementVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(guestManagementVo.getRoomNo());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private String room_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String nickname;
    private String memSex;
    private String profileImage;
    private int guestState;
    private Date guestStartDate;
    private int guest_propose;
    private int isFan;
    private int os;

}
