package com.dalbit.common.vo;

import com.dalbit.common.vo.procedure.P_KingFanRankListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class P_KingFanRankListOutVo {

    private int rank;
    private String memNo;
    private String nickNm;
    private String gender;
    private String age;
    private ImageVo profImg;
    private List liveBadgeList;

    /* 뱃지 정책 변경에 따른 데이터 20.11.25 이재은 */
    private BadgeFrameVo badgeFrame = new BadgeFrameVo();

    public P_KingFanRankListOutVo(){}
    public P_KingFanRankListOutVo(P_KingFanRankListVo target, int i){
        setRank(i);
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setGender(target.getMemSex());
        setAge(target.getAge());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setLiveBadgeList(new ArrayList());
    }
    public P_KingFanRankListOutVo(P_KingFanRankListVo target, int i, List liveBadgeList){
        setRank(i);
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setGender(target.getMemSex());
        setAge(target.getAge());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setLiveBadgeList(liveBadgeList);
    }

    public P_KingFanRankListOutVo(P_KingFanRankListVo target, int i, BadgeFrameVo badgeFrame){
        setRank(i);
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setGender(target.getMemSex());
        setAge(target.getAge());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setBadgeFrame(badgeFrame);
        setLiveBadgeList(new ArrayList());
    }
}
