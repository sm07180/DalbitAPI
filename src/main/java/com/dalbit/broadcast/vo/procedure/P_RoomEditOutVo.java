package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_RoomEditOutVo extends P_ApiVo {

    private String roomNo;
    private String subject_type;
    private String title;
    private String image_background;
    private String msg_welcom;

}
