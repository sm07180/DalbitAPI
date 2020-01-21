package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_BroadBasicVo {

    private String mem_no;
    private int subject_type;								   //방주제
    private String title;                                      //방제목
    private String image_background;                            //방배경이미지
    private String msg_welcom;                                  //환영메세지
    private int restrict_entry;                                 //입장제한
    private int restrict_age;                                   //입장제한 나이

}
