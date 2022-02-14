package com.dalbit.common.vo;

import com.dalbit.clip.vo.procedure.P_ClipReplyListVo;
import com.dalbit.member.vo.procedure.P_FanboardListVo;
import com.dalbit.member.vo.procedure.P_FanboardReplyVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardVo {
    private int replyIdx;
    private int parentGroupIdx;
    private String clipMemNo;
    private String writerMemNo;
    private String nickName;
    private String memId;
    private String gender;
    private ImageVo profImg;
    private String contents;
    private int replyCnt;
    private int status = 1;
    private int viewOn = 1;
    private String writeDt;
    private long writeTs;
    private String mem_no; // writerMemNo와 같음

    public BoardVo(P_FanboardListVo target) {
        this.replyIdx = target.getBoard_idx();
        this.parentGroupIdx = target.getBoard_no();
        this.writerMemNo = target.getWriter_mem_no();
        this.nickName = target.getNickName();
        this.gender = target.getMemSex();
        this.profImg = new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url"));
        this.contents = target.getContents();
        this.replyCnt = target.getReplyCnt();
        this.status = target.getStatus();
        this.writeDt = DalbitUtil.getUTCFormat(target.getWriteDate());
        this.writeTs = DalbitUtil.getUTCTimeStamp(target.getWriteDate());
        this.memId = target.getUserId();
        this.viewOn = target.getViewOn();
        this.mem_no = target.getWriter_mem_no();
    }

    public BoardVo(P_ClipReplyListVo target){
        setReplyIdx(target.getBoard_idx());
        setClipMemNo(target.getCast_mem_no());
        setWriterMemNo(target.getWriter_mem_no());
        setMemId(target.getMemId());
        setNickName(target.getNickName());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), getGender(), DalbitUtil.getProperty("server.photo.url")));
        setContents(target.getContents());
        setWriteDt(DalbitUtil.getUTCFormat(target.getWriteDate()));
        setWriteTs(DalbitUtil.getUTCTimeStamp(target.getWriteDate()));
        this.mem_no = target.getWriter_mem_no();
    }

    public BoardVo(P_FanboardReplyVo target) {
        this.replyIdx = target.getBoard_idx();
        this.parentGroupIdx = target.getBoard_no();
        this.writerMemNo = target.getWriter_mem_no();
        this.nickName = target.getNickName();
        this.gender = target.getMemSex();
        this.profImg = new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url"));
        this.contents = target.getContents();
        this.status = target.getStatus();
        this.writeDt = DalbitUtil.getUTCFormat(target.getWriteDate());
        this.writeTs = DalbitUtil.getUTCTimeStamp(target.getWriteDate());
        this.memId = target.getUserId();
        this.viewOn = target.getViewOn();
        this.mem_no = target.getWriter_mem_no();
    }


}
