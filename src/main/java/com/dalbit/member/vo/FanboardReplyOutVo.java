package com.dalbit.member.vo;


import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_FanboardReplyVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import java.text.SimpleDateFormat;

@Getter
@Setter
public class FanboardReplyOutVo {


    private String memNo;
    private int     boardNo;
    private int     boardIdx;
    private String  writerNo;
    private String  nickNm;
    private ImageVo profImg;
    private String  contents;
    private int     status;
    private String  writeDt;
    private long    writeTs;

    public FanboardReplyOutVo(P_FanboardReplyVo target) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

        this.boardNo=target.getBoard_no();
        this.boardIdx=target.getBoard_idx();
        this.writerNo=target.getWriter_mem_no();
        this.nickNm=target.getNickName();
        this.profImg=new ImageVo(target.getProfileImage(),DalbitUtil.getProperty("server.photo.url"));
        this.contents=target.getContents();
        this.status=target.getStatus();
        this.writeDt = format.format(target.getWriteDate());
        this.writeTs = target.getWriteDate().getTime() / 1000;
    }
}
