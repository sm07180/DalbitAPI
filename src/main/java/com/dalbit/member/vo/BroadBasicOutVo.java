package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BroadBasicOutVo {

    private String mem_no;
    private int roomType;         //방주제
    private String title;            //방제목
    private ImageVo bgImg;            //방배경이미지
    private String WelcomMsg;         //환영메세지
    private int isEntry;          //입장제한
    private int isAge;          //입장제한

    public BroadBasicOutVo(P_BroadBasicVo target) {
//        this.mem_no=target.getMem_no();
        this.roomType=target.getSubject_type();
        this.title=target.getTitle();
        this.bgImg= new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
        this.WelcomMsg=target.getMsg_welcom();
        this.isEntry=target.getRestrict_entry();
        this.isEntry=target.getRestrict_age();
    }
}
