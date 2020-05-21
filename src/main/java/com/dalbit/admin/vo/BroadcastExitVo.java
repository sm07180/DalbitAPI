package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BroadcastExitVo {

    public BroadcastExitVo(){

    }

    public BroadcastExitVo(String roomNo, String startDate){
        setRoom_no(roomNo);
        setStart_date(startDate);
    }

    private String room_no;
    private String start_date;
}
