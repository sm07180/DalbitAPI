package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipPlayListVo;
import com.dalbit.clip.vo.procedure.P_ClipUploadListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClipUploadListOutVo {

    private String clipNo;              //클립 번호
    private String subjectType;         //클립 주제
    private String title;               //클립 제목
    private ImageVo bgImg;              //클립 배경이미지
    private int playCnt;                //재생 수
    private int goodCnt;                //좋아요 수
    private int byeolCnt;               //받은별 수
    private String memNo;               //클립 등록 회원번호
    private String nickName;            //닉네임
    private int replyCnt;               //댓글 수

    public ClipUploadListOutVo(P_ClipUploadListVo target){
        setClipNo(target.getCast_no());
        setSubjectType(target.getSubject_type());
        setTitle(target.getTitle());
        setBgImg(new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
        setPlayCnt(target.getPlayCnt());
        setGoodCnt(target.getGoodCnt());
        setByeolCnt(target.getGiftCnt());
        setMemNo(target.getMem_no());
        setNickName(target.getNickName());
        setReplyCnt(target.getReplyCnt());
    }
}
