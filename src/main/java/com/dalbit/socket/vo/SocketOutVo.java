package com.dalbit.socket.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;

@Getter @Setter @Scope("prototype")
public class SocketOutVo {
    private String mem_nick;
    private String mem_sex;
    private String image_profile;
    private int auth;
    private String control;
    private int isFan;
    private String fanBadgeText;
    private String fanBadgeIcon;
    private String fanBadgeStartColor;
    private String fanBadgeEndColor;
    private int newdj_badge;
    private int new_badge;
}
