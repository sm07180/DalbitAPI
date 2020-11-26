package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_MypageNoticeReplyListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MypageNoticeReplyListOutVo {

    private int replyIdx;          // 댓글 인덱스
    private int noticeNo;
    private String writerMemNo;   // 작성자 회원번호
    private String nickName;        // 작성자 닉네임
    private String userId;          // 작성자 user id
    private String memSex;          // 작성자 성별
    private ImageVo profileImg;    // 작성자 프로필 이미지
    private String contents;        // 댓글 내용
    private String writeDt;       // 작성 일자
    private long writeTs;

    public MypageNoticeReplyListOutVo(P_MypageNoticeReplyListVo target) {
        setReplyIdx(target.getReply_idx());
        setNoticeNo(target.getNotice_no());
        setWriterMemNo(target.getWriter_mem_no());
        setNickName(target.getNickName());
        setUserId(target.getUserId());
        setMemSex(target.getMemSex());
        setProfileImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setContents(target.getContents());
        setWriteDt(DalbitUtil.getUTCFormat(target.getWriteDate()));
        setWriteTs(DalbitUtil.getUTCTimeStamp(target.getWriteDate()));
    }
}
