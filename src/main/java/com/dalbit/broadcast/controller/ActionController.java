package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.ActionService;
import com.dalbit.broadcast.vo.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@RequestMapping("broad")
public class ActionController {

    @Autowired
    private MessageUtil messageUtil;
    @Autowired
    private ActionService actionService;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    RestService restService;
    @Value("${server.img.url}")
    private String IMG_URL;

    /**
     * 방송방 좋아요 추가
     */
    @PostMapping("/likes")
    public String roomGood(HttpServletRequest request){

        P_RoomGoodVo apiData = new P_RoomGoodVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request, "roomNo"));

        String result = actionService.callBroadCastRoomGood(apiData);

        return result;
    }


    /**
     * 방송방 공유링크 확인
     */
    @GetMapping("/link")
    public String roomShareLink(HttpServletRequest request){
        P_RoomShareLinkVo apiData = new P_RoomShareLinkVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setLinkCode(DalbitUtil.convertRequestParamToString(request, "link"));

        String result = actionService.callBroadCastShareLink(apiData);

        return result;
    }

    /**
     * 방송방 선물하기
     */
    @PostMapping("/gift")
    public String roomGift(HttpServletRequest request){
        P_RoomGiftVo apiData = new P_RoomGiftVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request, "roomNo"));
        apiData.setGifted_mem_no(DalbitUtil.convertRequestParamToString(request, "memNo"));
        apiData.setItem_no(DalbitUtil.convertRequestParamToString(request, "itemNo"));
        apiData.setItem_cnt(DalbitUtil.convertRequestParamToInteger(request, "itemCnt"));

        String result = actionService.callBroadCastRoomGift(apiData);

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
