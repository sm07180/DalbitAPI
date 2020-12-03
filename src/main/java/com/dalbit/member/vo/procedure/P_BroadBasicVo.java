package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_BroadBasicVo extends P_ApiVo {

    private String mem_no;
    private String subject_type;			    //방주제
    private String title;                   //방제목
    private String image_background;        //방배경이미지
    private String msg_welcom;              //환영메세지
    private int type_entry;                 //입장제한

}
