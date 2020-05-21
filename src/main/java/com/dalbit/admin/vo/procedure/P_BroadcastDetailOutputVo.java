package com.dalbit.admin.vo.procedure;

import com.dalbit.common.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_BroadcastDetailOutputVo extends BaseVo {

    private String backgroundImage;
    private String welcomeMsg;
    private String subjectType;
    private String title;
    private String dj_mem_no;
    private String dj_userId;
    private String dj_nickName;
    private String dj_memSex;
    private String opMemoCnt;
    private String entryType;
    private String freezeMsg;
    private String forcedQuit;
    private String broadcastState;
    private String micState;
    private String guestState;
    private String guestCnt;
    private String guest_mem_no;
    private String guest_userId;
    private String guest_nickName;
    private String managerCnt;
    private String forcedLeaveCnt;
    private String exitTryCount;
    private String osType;
    private String startDate;
    private String endDate;
    private String airTime;
    private String lastOpDate;
    private String lastOpName;

}
