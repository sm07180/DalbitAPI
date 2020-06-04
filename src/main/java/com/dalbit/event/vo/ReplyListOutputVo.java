package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.event.vo.procedure.P_ReplyListOutputVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ReplyListOutputVo {
    /* Output */
    private int replyIdx;		//댓글 인덱스번호
    private int eventIdx;		//이벤트 인덱스번호
    private String writerNo;	//작성자 회원번호
    private String memId;		//회원아이디
    private String nickNm;		//작성자 닉네임
    private ImageVo profImg;	//작성자 프로필이미지
    private String content;	//댓글내용
    private int status;			//상태값 ( 1: 정상, 2: 삭제됨)
    private String writeDt;		//작성일자
    private long writeTs;		//작성일자 timestamp


    public ReplyListOutputVo(P_ReplyListOutputVo target) {
        this.replyIdx = target.getReply_idx();
        this.eventIdx = target.getEvent_idx();
        this.writerNo = target.getMem_no();
        this.memId = target.getUserId();
        this.nickNm = target.getNickName();
        this.profImg = new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url"));
        this.content = target.getContents();
        this.status = target.getStatus();
        this.writeDt = DalbitUtil.getUTCFormat(target.getWriteDate());
        this.writeTs = DalbitUtil.getUTCTimeStamp(target.getWriteDate());
    }
}
