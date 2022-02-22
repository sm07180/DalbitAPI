package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShareEventInputVo {
    /* 삭제 */
    private String tailNo;
    private String tailMemNo;

    /* 등록 */
    // private String tailMemNo;
    private String tailMemId;
    private String tailMemSex;
    private String tailMemIp;
    private String tailConts;
    private String tailLoginMedia;

    /* 수정 */
    // #{tailNo}, #{tailMemNo}, #{tailMemId}, #{tailMemSex}, #{tailMemIp}, #{tailConts}, #{tailLoginMedia}
}
