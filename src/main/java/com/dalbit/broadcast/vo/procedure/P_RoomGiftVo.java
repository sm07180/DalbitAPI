package com.dalbit.broadcast.vo.procedure;


import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class P_RoomGiftVo extends P_ApiVo {
    private String mem_no;          //요청 회원번호
    private String room_no;         //해당 방 번호
    private String gifted_mem_no;   //선물받을 회원 번호
    private String item_code;       //선물할 아이템 번호
    private Integer item_cnt;       //아이템 개수
    private String secret;          //몰래보내기 여부

    private String ttsText = "";    // tts 음성 내용
    private String actorId = "";    // 배우 id
    private String ttsYn = "";      // tts 아이템 yn
    private String storyText = "";  // 사연 플러스 글내용
}
