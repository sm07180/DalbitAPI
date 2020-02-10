package com.dalbit.member.vo;

import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_BroadBasicVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@ToString
public class BroadBasicOutVo {

    @Autowired
    CommonService commonService;

    private String roomType;         //방주제
    private String title;         //방제목
    private ImageVo bgImg;        //방배경이미지
    private String welcomMsg;     //환영메세지
    private int entryType;        //입장제한


    public BroadBasicOutVo(P_BroadBasicVo target) {
        this.roomType = target.getSubject_type() == 0 ? String.valueOf(target.getSubject_type()) : commonService.getCodeList("roomType").get(0).getCd();
        this.title = target.getTitle();
        if(target.getImage_background() != null){
            this.bgImg = new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
        }else{
            this.bgImg = new ImageVo();
        }
        this.welcomMsg = target.getMsg_welcom();
        this.entryType = target.getType_entry();
    }
}
