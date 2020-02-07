package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_FanboardListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.text.SimpleDateFormat;

@Getter
@Setter
@ToString
public class FanboardVo {

    private int boardIdx;
    private int boardNo;
    private String writerNo;
    private String nickNm;

    private ImageVo profImg;
    private String contents;
    private int replyCnt;
    private int status;
    private String writeDt;
    private long writeTs;

    public FanboardVo(P_FanboardListVo target) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        this.boardIdx = target.getBoard_idx();
        this.boardNo = target.getBoard_no();
        this.writerNo = target.getWriter_mem_no();
        this.nickNm = target.getNickName();
        this.profImg = new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url"));
        this.contents = target.getContents();
        this.replyCnt = target.getReplyCnt();
        this.status = target.getStatus();
        this.writeDt = format.format(target.getWriteDate());
        this.writeTs = target.getWriteDate().getTime() / 1000;
    }

}


