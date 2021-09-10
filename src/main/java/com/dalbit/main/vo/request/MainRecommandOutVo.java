package com.dalbit.main.vo.request;

import com.dalbit.common.vo.BadgeVo;
import com.dalbit.common.vo.FanBadgeVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_MainRecommandVo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class MainRecommandOutVo {

    /* Input */
    /*private String mem_no;*/

    /* OutPut */
    private String memNo;
    private String nickNm;
    private String gender;
    private ImageVo profImg;
    private String roomNo;
    private String roomType;
    private String title;
    private String bannerUrl;
    private String mediaType;
    private int listeners;
    private int likes;
    public boolean isSpecial;
    public int badgeSpecial;
    public boolean isAdmin = false;
    public boolean isNew = false;
    public boolean isShining = false;
    public boolean isConDj = false;
    private int isWowza = 0;
    private List<BadgeVo> liveBadgeList = new ArrayList<>();
    private List<BadgeVo> commonBadgeList = new ArrayList<>();
}
