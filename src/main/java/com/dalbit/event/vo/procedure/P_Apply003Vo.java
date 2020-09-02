package com.dalbit.event.vo.procedure;

import com.dalbit.event.vo.KnowhowEventInputVo;
import com.dalbit.event.vo.request.ApplyVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_Apply003Vo extends P_ApplyVo {

    private int slct_device;

    private String device1;
    private String device2;
    private String title;
    private String contents;

    private String image_url;
    private String image_url2;
    private String image_url3;

    public P_Apply003Vo(ApplyVo applyVo, KnowhowEventInputVo knowhowEventInputVo, HttpServletRequest request) {
        super(applyVo, request);
        this.slct_device = knowhowEventInputVo.getSlct_device();
        this.device1 = knowhowEventInputVo.getDevice1();
        this.device2 = knowhowEventInputVo.getDevice2();
        this.title = knowhowEventInputVo.getTitle();
        this.contents = knowhowEventInputVo.getContents();
        this.image_url = knowhowEventInputVo.getImage_url();
        this.image_url2 = knowhowEventInputVo.getImage_url2();
        this.image_url3 = knowhowEventInputVo.getImage_url3();
    }
}
