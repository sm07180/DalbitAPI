package com.dalbit.event.controller;

import com.dalbit.event.service.PoemService;
import com.dalbit.event.vo.PoemEventResVo;
import com.dalbit.event.vo.request.PoemEventReqVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@RequestMapping("/event/poem")
@RequiredArgsConstructor
@RestController
public class PoemController {

    private final PoemService service;

    @GetMapping
    public String getPoemList(@RequestParam String memNo, @RequestParam Integer pageNo, @RequestParam Integer pagePerCnt){
        System.out.println("memNo = " + memNo + ", pageNo = " + pageNo + ", pagePerCnt = " + pagePerCnt);
        return service.getPoemList(memNo, pageNo, pagePerCnt);
    }

    @PostMapping
    public String savePoem(HttpServletRequest request, @RequestBody PoemEventReqVo poemEventReqVo){
        poemEventReqVo.setTailMemIp(DalbitUtil.getIp(request));
        System.out.println("poemEventReqVo = " + poemEventReqVo);
        return service.savePoem(poemEventReqVo);
    }

    @DeleteMapping
    public String deletePoem(@RequestBody HashMap<String, Object> payload){
        String tailNo = (String) payload.get("tailNo");
        String tailMemNo = (String) payload.get("tailMemNo");
        System.out.println("tailNo = " + tailNo + ", tailMemNo = " + tailMemNo);
        return service.deletePoem(tailNo, tailMemNo);
    }

    @PutMapping
    public String editPoem(@RequestBody PoemEventReqVo poemEventReqVo){
        System.out.println("poemEventReqVo = " + poemEventReqVo);
        return service.editPoem(poemEventReqVo);
    }

}
