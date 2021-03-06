package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_EventPageListOutputVo extends P_ApiVo {
    /* Output */
    private int eventIdx;

    private int state;
    private String title;
    private int alwaysYn;

    private String startDate;
    private String endDate;
    private String announcementDate;

    private String listImageUrl;
    private String pcLinkUrl;
    private String mobileLinkUrl;

    private int prizeWinner;
    private int winnerOpen;
    private int viewYn;
}
