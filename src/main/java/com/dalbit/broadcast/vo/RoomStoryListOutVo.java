package com.dalbit.broadcast.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class RoomStoryListOutVo {

    private String memNo;
//    private String roomNo;
    private String contents;

    private int storyIdx;						//댓글 인덱스번호
    private String writerNo;                    //작성자 회원번호
    private String nickNm;                      //작성자 닉네임
    private ImageVo profImg;                    //작성자 프로필이미지
    private String writeDt;                     //작성일자
    private Long writeTs;                       //작성일자 timestamp

    public RoomStoryListOutVo(P_RoomStoryListVo target, String mem_no){
        this.memNo = mem_no;
        this.storyIdx = target.getStory_idx();
        this.writerNo = target.getWriter_mem_no();
        this.nickNm = target.getNickName();
        this.profImg = new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url"));
        this.contents = target.getContents();
        this.writeDt = DalbitUtil.getUTCFormat(target.getWriteDate());
        this.writeTs = DalbitUtil.getUTCTimeStamp(target.getWriteDate());
    }
}
