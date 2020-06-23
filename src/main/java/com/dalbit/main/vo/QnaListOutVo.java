package com.dalbit.main.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.main.vo.procedure.P_QnaListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class QnaListOutVo {

    private int qnaIdx;
    private int qnaType;
    private String title;
    private String contents;
    private String answer;
    private int state;
    private String writeDt;
    private Long writeTs;
    private ImageVo addFile;
    private ImageVo addFile1;
    private ImageVo addFile2;
    private ImageVo addFile3;
    private String addFileName1;
    private String addFileName2;
    private String addFileName3;
    private String opDt;
    private Long opTs;
    private String email;

    public QnaListOutVo() {}
    public QnaListOutVo(P_QnaListVo target) {
        setQnaIdx(target.getQnaIdx());
        setQnaType(target.getSlctType());
        setTitle(target.getTitle());
        setContents(target.getContents());
        setAnswer(target.getAnswer());
        setState(target.getState());
        setWriteDt(DalbitUtil.getUTCFormat(target.getWriteDate()));
        setWriteTs(DalbitUtil.getUTCTimeStamp(target.getWriteDate()));
        if(!DalbitUtil.isEmpty(target.getOpDate())){
            setOpDt(DalbitUtil.getUTCFormat(target.getOpDate()));
            setOpTs(DalbitUtil.getUTCTimeStamp(target.getOpDate()));
        }
        setAddFile(new ImageVo(target.getAddFile(), DalbitUtil.getProperty("server.photo.url")));
        setAddFile1(new ImageVo(target.getAddFile1(), DalbitUtil.getProperty("server.photo.url")));
        setAddFile2(new ImageVo(target.getAddFile2(), DalbitUtil.getProperty("server.photo.url")));
        setAddFile3(new ImageVo(target.getAddFile3(), DalbitUtil.getProperty("server.photo.url")));
        setAddFileName1(target.getAddFileName1());
        setAddFileName2(target.getAddFileName2());
        setAddFileName3(target.getAddFileName3());
        setEmail(target.getEmail());
    }
}
