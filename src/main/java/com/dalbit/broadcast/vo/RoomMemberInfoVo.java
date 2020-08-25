package com.dalbit.broadcast.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class RoomMemberInfoVo {
    private long remainTime = 0;
    private int likes = 0;
    private int rank = 0;
    private int auth = 0;
    private String ctrlRole;
    private boolean isFan;
    private boolean isLike;
    private boolean hashStory;
    private boolean useBoost;
    private List fanRank = new ArrayList();
    private String kingMemNo;
    private String kingNickNm;
    private ImageVo kingProfImg;
    private List fanBadgeList = new ArrayList();

    public void isFan(boolean isFan){
        this.isFan = isFan;
    }

    public void isLike(boolean isLike){
        this.isLike = isLike;
    }

    public void setKingMember(String memNo, String nickNm, ImageVo profImg){
        this.kingMemNo = memNo;
        this.kingNickNm = nickNm;
        this.kingProfImg = profImg;
    };
}
