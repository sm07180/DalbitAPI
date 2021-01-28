package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipRecommendListOutputVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClipRecommendListOuputVo {

    public ClipRecommendListOuputVo() {}

    public ClipRecommendListOuputVo(P_ClipRecommendListOutputVo target) {
        setClipNo(target.getCastNo());
        setBgImg(new ImageVo(target.getBackgroundImage(), DalbitUtil.getProperty("server.photo.url")));
        setFileName(target.getFileName());
//        setFilePath(target.getFilePath());
        setFilePlayTime(target.getFilePlay());
//        setFileSize(target.getFileSize());
        setSubjectType(target.getSubjectType());
        setSubjectName(target.getSubjectName());
        setTitle(target.getTitle());
        setMemNo(target.getMemNo());
        setGender(target.getMemSex());
        setNickName(target.getMemNick());
        setIsLeader(target.getLeaderYn() == 0 ? false : true);
        setByeolCnt(target.getByeolCnt());
        setGoodCnt(target.getGoodCnt());
        setReplyCnt(target.getReplyCnt());
    }

    private String clipNo;               //  클립번호
    private ImageVo bgImg;               // 클립이미지
    private String fileName;             // 파일이름
    //    private String filePath;            // 파일경로
    private String filePlayTime;             // 재생시간
    //    private String fileSize;            // 파일크기
    private String subjectType;          // 주제
    private String subjectName;          // 주제명
    private String title;                // 클립제목
    private String memNo;                // 회원번호
    private String gender;               // 성별
    private String nickName;               // 닉네임
    private Boolean isLeader;            // 대표 여부 (0:일반 1:대표)
    private int byeolCnt;                // 받은 별 수
    private int goodCnt;                 //  받은 좋아요 수
    private int replyCnt;                //  받은 댓글 수
}
