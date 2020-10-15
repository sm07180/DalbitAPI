package com.dalbit.search.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.search.vo.procedure.P_RoomRecommandListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomRecommandListOutVo {

    private String roomNo;
    private String roomType;
    private String title;
    private String nickNm;
    private ImageVo bgImg;
    private int entryType;
    private int entryCnt;
    private String startDt;
    private long startTs;
    private Boolean isSpecial;

    public RoomRecommandListOutVo(){}
    public RoomRecommandListOutVo(P_RoomRecommandListVo target){
        this.roomNo = target.getRoomNo();
        this.roomType = target.getSubject_type();
        this.title = target.getTitle();
        this.nickNm = target.getNickName();
        this.bgImg = new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url"));
        this.entryType = target.getType_entry();
        this.entryCnt = target.getCount_entry();
        this.startDt = DalbitUtil.getUTCFormat(target.getStart_date());
        this.startTs = DalbitUtil.getUTCTimeStamp(target.getStart_date());
        this.isSpecial = target.getBadge_special() == 1 ? true : false;
    }
}
