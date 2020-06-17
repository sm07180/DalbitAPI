package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BroadInfoVo {
    private String room_no;
    private String subject_type;
    private String title;
    private String reset_title;
    private String msg_welcom;
    private String image_background;
    private int grade_background;
    private String reset_image_background;
}
