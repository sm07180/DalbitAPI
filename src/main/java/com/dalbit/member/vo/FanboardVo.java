package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_FanboardListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FanboardVo {

    private int replyIdx;
    private int parentGroupIdx;
    private String writerMemNo;
    private String nickName;
    private String memId;
    private String gender;
    private ImageVo profImg;
    private String contents;
    private int replyCnt;
    private int status;
    private int viewOn;
    private String writeDt;
    private long writeTs;


    public FanboardVo(P_FanboardListVo target) {
        this.replyIdx = target.getBoard_idx();
        this.parentGroupIdx = target.getBoard_no();
        this.writerMemNo = target.getWriter_mem_no();
        this.nickName = target.getNickName();
        this.profImg = new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url"));
        this.contents = target.getContents();
        this.replyCnt = target.getReplyCnt();
        this.status = target.getStatus();
        this.writeDt = DalbitUtil.getUTCFormat(target.getWriteDate());
        this.writeTs = DalbitUtil.getUTCTimeStamp(target.getWriteDate());
        this.memId = target.getUserId();
        this.viewOn = target.getViewOn();
    }

}


