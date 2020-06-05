package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;

@Getter
@Setter
@ToString
public class LiveChatInputVo {

    /*  input  */
    private String room_no;
    private String chatIdx;
    private int pageNo;

}
