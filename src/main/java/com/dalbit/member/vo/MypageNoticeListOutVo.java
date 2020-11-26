package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_MypageNoticeSelectVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MypageNoticeListOutVo {

    private Long noticeIdx;
    private String title;
    private String contents;
    private boolean isTop;
    private String writeDt;
    private Long writeTs;
    private String nickNm;
    private ImageVo profImg;
    private String imagePath;

    // 2020.11.10 강다인. 댓글 추가
    private int replyCnt;

    public MypageNoticeListOutVo(P_MypageNoticeSelectVo target) {
        setNoticeIdx(target.getNoticeIdx());
        setTitle(target.getTitle());
        setContents(target.getContents());
        setTop(target.getTopFix() == 1 ? true : false);
        setWriteDt(DalbitUtil.getUTCFormat(target.getWriteDate()));
        setWriteTs(DalbitUtil.getUTCTimeStamp(target.getWriteDate()));
        setNickNm(target.getNickName());
        setImagePath(target.getImagePath());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setReplyCnt(target.getReplyCnt());
    }
}
