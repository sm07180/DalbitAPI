package com.dalbit.main.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Data;

@Data
public class MainSwiperVO {
    private Integer idx;
    private String thumbsUrl;
    private String linkUrl;
    private String linkType;
    private String title;
    private String bannerUrl;
    private Integer popup_type;
    private Integer is_cookie;
    private String contents;
    private Integer is_title_view;
    private Integer is_button_view;
    private String buttonNm;
    private String mem_no = "0";
    private String room_no;
    private String mem_nick;
    private String mem_sex;
    private String image_profile;
    private ImageVo imageProfile;
    private String subject_type;
    private String image_background;
    private String is_wowza;
    private Integer badgeSpecial;
    private Integer isConDj;
}
