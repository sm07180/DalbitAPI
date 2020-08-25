package com.dalbit.search.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.search.vo.procedure.P_LiveRoomSearchVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomSearchOutVo {

    private String roomNo;
    private String title;
    private int entryCnt;
    private int likeCnt;
    private Boolean isRecomm;
    private Boolean isPop;
    private Boolean isNew;
    private String memNo;
    private String nickNm;
    private String memId;
    /*private String gender;*/
    private ImageVo profImg;
    private int isWowza;

    public RoomSearchOutVo(P_LiveRoomSearchVo target) {
        setRoomNo(target.getRoom_no());
        setTitle(target.getTitle());
        setEntryCnt(target.getCount_entry());
        setLikeCnt(target.getCount_good());
        setIsRecomm(target.getBadge_recomm() == 1 ? true : false);
        setIsPop(target.getBadge_popular() == 1 ? true : false);
        setIsNew(target.getBadge_newdj() == 1 ? true : false);
        setMemNo(target.getMem_no());
        setNickNm(target.getMem_nick());
        setMemId(target.getMem_id());
        /*setGender(target.getMem_sex());*/
        setProfImg(new ImageVo(target.getImage_profile(), target.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
        setIsWowza(target.getIs_wowza());
    }
}
