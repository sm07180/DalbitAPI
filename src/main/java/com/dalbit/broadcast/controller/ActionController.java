package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.vo.P_RoomGoodVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("broad")
public class ActionController {

    @Autowired
    private MessageUtil messageUtil;
    @Autowired
    private RoomService roomService;

    /**
     * 방송방 좋아요 추가
     */
    @PostMapping("/likes")
    public String roomGood(HttpServletRequest request){

        P_RoomGoodVo adiData = P_RoomGoodVo.builder()
                .mem_no(MemberVo.getUserInfo().getMemNo())
                .room_no(DalbitUtil.convertRequestParamToString(request, "s_room_no"))
                .build();

        String result = roomService.callBroadCastRoomGood(adiData);

        return result;
    }




    /* #################### 여기까지 API명세서 기준 작업완료 ######################## */









    /**
     * 받은선물내역
     */
    @GetMapping("{brodNo}/gift")
    public String getGiftHistory(@PathVariable String brodNo){

        HashMap data = new HashMap();

        HashMap summary = new HashMap();
        summary.put("receives", 23);
        summary.put("golds", 2407);
        data.put("summary", summary);

        data.put("memNo", "M1001234");
        data.put("img", "/files/filePath/filename.png");
        data.put("nickNm", "하늘하늘이에요");
        data.put("gift", "치킨");
        data.put("combos", 3);
        data.put("golds", 30);
        data.put("date", DalbitUtil.getTimeStamp());

        HashMap paging = new HashMap();
        paging.put("total", 102);
        paging.put("recordperpage", 10);
        paging.put("page", 1);
        paging.put("prev", 0);
        paging.put("next", 2);
        paging.put("totalpage", 21);
        data.put("paging", paging);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, data)));
    }

    /**
     * 선물하기
     */
    @PostMapping("{brodNo}/gift")
    public String sendGift(@PathVariable String brodNo){

        HashMap data = new HashMap();
        data.put("brodNo", brodNo);
        data.put("giftNo", "GT005421");

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.생성, data)));
    }

    /**
     * 선물액션조회
     */
    @GetMapping("{brodNo}/gift/{giftNo}")
    public String selectGiftNo(@PathVariable String brodNo, @PathVariable String giftNo){

        HashMap data = new HashMap();
        data.put("brodNo", brodNo);
        data.put("giftNo", giftNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.생성, data)));
    }

    /**
     * 좋아요
     */
    @PostMapping("{brodNo}/likes")
    public String likes(@PathVariable String brodNo){

        HashMap data = new HashMap();
        data.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.좋아요, data)));
    }

    /**
     * 부스트
     */
    @PostMapping("{brodNo}/boost")
    public String boost(@PathVariable String brodNo){

        HashMap data = new HashMap();
        data.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.생성, data)));
    }

    /**
     * 신고하기
     */
    @PostMapping("{brodNo}/declar")
    public String declar(@PathVariable String brodNo){

        HashMap data = new HashMap();
        data.put("brodNo", brodNo);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.생성, data)));
    }
}
