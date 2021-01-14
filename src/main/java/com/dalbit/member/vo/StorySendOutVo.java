package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_StorySendVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter  @ToString
public class StorySendOutVo {
    private int storyIdx;
    private String roomNo;
    private String title;
    private String djMemNo;
    private String djNickNm;
    private ImageVo djProfImg;
    private String contents;
    private String writeDt;
    private long writeTs;

    public StorySendOutVo(){}

    public StorySendOutVo(P_StorySendVo pStorySendVo, String photoSvr){
        this.storyIdx = pStorySendVo.getStoryIdx();
        this.roomNo = pStorySendVo.getRoomNo();
        this.title = pStorySendVo.getTitle();
        this.djMemNo = pStorySendVo.getDjMemNo();
        this.djNickNm = pStorySendVo.getDjNickNm();
        this.djProfImg = new ImageVo(pStorySendVo.getDjProfile(), pStorySendVo.getDjGender(), photoSvr);
        this.contents = pStorySendVo.getContents();
        this.writeDt = DalbitUtil.getUTCFormat(pStorySendVo.getWriteDt());
        this.writeTs = DalbitUtil.getUTCTimeStamp(pStorySendVo.getWriteDt());
    }
}
