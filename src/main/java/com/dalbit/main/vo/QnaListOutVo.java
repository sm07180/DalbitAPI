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
        setAddFile(new ImageVo(target.getAddFile(), DalbitUtil.getProperty("server.photo.url")));
    }
}
