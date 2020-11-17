package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RouletteInfoVo {
    private String slct_type;
    private String set;
    private String item_no;
    private String image_url;
    private String bg_image_url;
    private String pin_image_url;
    private String start_image_url;
    private String item_name;
    private String item_win_msg;
}
