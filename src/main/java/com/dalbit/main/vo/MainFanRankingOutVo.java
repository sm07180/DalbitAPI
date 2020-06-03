package com.dalbit.main.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_MainFanRankingVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MainFanRankingOutVo {

    private int rank;
    private String upDown;
    private String memNo;
    private String nickNm;
    private String memId;
    private String gender;
    private ImageVo profImg;
    private int level;
    private String grade;
    private int gift;
    private int listen;
    private int fan;
    private boolean isSpecial;
    private String holder;
    private String roomNo;

    public MainFanRankingOutVo(){}
    public MainFanRankingOutVo(P_MainFanRankingVo target) {
        setRank(target.getRank());
        setUpDown(target.getUp_down());
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setMemId(target.getMemId());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setLevel(target.getLevel());
        setGrade(target.getGrade());
        setGift(target.getGiftCount());
        setListen(target.getListenCount());
        setFan(target.getFanCount());
        this.isSpecial = target.getSpecialdj_badge() == 1;
        this.holder = "https://image.dalbitlive.com/level/frame/200525/AAA/ico_frame_" + this.level + ".png";
        setRoomNo(target.getRoomNo());
    }
}
