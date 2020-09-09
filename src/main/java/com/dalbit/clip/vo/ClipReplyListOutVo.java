package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipReplyListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClipReplyListOutVo {

    private int replyIdx;           //댓글 번호
    private String clipMemNo;       //클립 회원번호
    private String writerMemNo;     //작성자 회원번호
    private String memId;           //작성자 아이디
    private String nickName;        //작성자 닉네임
    private String gender;          //작성자 성별
    private ImageVo profImg;        //프로필이미지
    private String contents;        //댓글내용
    private String writeDt;         //작성일자
    private long writeTs;           //작성일자

    public ClipReplyListOutVo(P_ClipReplyListVo target){
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
    }
}
