package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipAddVo;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipAddVo {

    private String mem_no;          //요청회원번호
    private String subjectType;     //클립 주제 (01~99)
    private String title;           //제목
    private int entryType;          //청취제한 (0: 전체, 1: 팬, 2: 20세이상)
    private int openType;           //공개여부(0: 비공개, 1: 공개)
    private String backgroundImage; //배경이미지
    private String fileName;        //파일명
    private String filePath;        //클립 파일
    private String filePlayTime;    //재생시간
    private String fileSize;        //파일용량
    private int os;                 //os (1: aos, 2: ios, 3: pc)


    public P_ClipAddVo(){}
    public P_ClipAddVo(ClipAddVo clipAddVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSubjectType(clipAddVo.getSubjectType());
        setTitle(clipAddVo.getTitle());
        setEntryType(clipAddVo.getEntryType());
        setOpenType(clipAddVo.getOpenType());
        setBackgroundImage(clipAddVo.getBgImg());
        setFileName(clipAddVo.getFileName());
        setFilePath(clipAddVo.getFilePath());
        setFilePlayTime(clipAddVo.getFilePlayTime());
        setFileSize(clipAddVo.getFileSize().split("\\.")[0]);
        setOs(new DeviceVo(request).getOs());
    }
    
}
