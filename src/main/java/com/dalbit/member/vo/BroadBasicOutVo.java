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

    private int roomType;         //방주제
    private String title;         //방제목
    private ImageVo bgImg;        //방배경이미지
    private String welcomMsg;     //환영메세지
    private int entryType;        //입장제한


    public BroadBasicOutVo(P_BroadBasicVo target) {
        this.roomType=target.getSubject_type();
        this.title=target.getTitle();
        this.bgImg= new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
        this.welcomMsg=target.getMsg_welcom();
        this.entryType=target.getType_entry();
    }
}
