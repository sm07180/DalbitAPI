package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipPlayListVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClipPlayListOutVo {

    private String clipNo;              //클립 번호
    private String subjectType;         //클립 주제
    private String title;               //클립 제목
    private ImageVo bgImg;              //클립 배경이미지
    private String filePlayTime;        //재생시간
    private String memNo;               //클립 등록 회원번호
    private String nickName;            //닉네임
    private String gender;              //성별
    private int playCnt;                //재생 수
    private int goodCnt;                //좋아요 수
    private int byeolCnt;               //받은별 수
    private String insDt;               //추가일자
    private long insTs;

    public ClipPlayListOutVo(P_ClipPlayListVo target){
        setClipNo(target.getCast_no());
        setSubjectType(target.getSubject_type());
        setTitle(target.getTitle());
        setBgImg(new ImageVo(target.getImage_background(), DalbitUtil.getProperty("server.photo.url")));
        setFilePlayTime(target.getFilePlayTime());
        setMemNo(target.getMem_no());
        setNickName(target.getNickName());
        setGender(target.getMemSex());
        setPlayCnt(target.getPlayCnt());
        setGoodCnt(target.getGoodCnt());
        setByeolCnt(target.getGiftCnt());
        setInsDt(DalbitUtil.getUTCFormat(target.getIns_date()));
        setInsTs(DalbitUtil.getUTCTimeStamp(target.getIns_date()));
    }
}
