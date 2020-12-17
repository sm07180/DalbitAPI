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
        setFilePlay(target.getFilePlay());
//        setFileSize(target.getFileSize());
        setSubjectType(target.getSubjectType());
        setSubjectName(target.getSubjectName());
        setTitle(target.getTitle());
        setMemNo(target.getMemNo());
        setNickNm(target.getMemNick());
        setGender(target.getMemSex());
        setIsLeader(target.getLeaderYn() == 0 ? false : true);
    }

    private String clipNo;               //  클립번호
    private ImageVo bgImg;            // 클립이미지
    private String fileName;            // 파일이름
//        private String filePath;            // 파일경로
    private String filePlay;            // 재생시간
    //    private String fileSize;            // 파일크기
    private String subjectType;         // 주제
    private String subjectName;         // 주제명
    private String title;                // 클립제목
    private String memNo;                // 회원번호
    private String nickNm;              // 닉네임
    private String gender;              // 성별
    private Boolean isLeader;           // 대표 여부 (0:일반 1:대표)
}
