package com.dalbit.event.controller;

import com.dalbit.event.service.ShareEventService;
import com.dalbit.event.vo.ShareEventInputVo;
import com.dalbit.event.vo.ShareEventVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.IPUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/event/share")
@Slf4j
@RequiredArgsConstructor
public class ShareEventController {
    private final ShareEventService shareEventService;
    private final IPUtil ipUtil;

    @GetMapping("/tail/list")
    public String shareTailList(@RequestBody ShareEventVo shareEventVo, HttpServletRequest request) {
        String result = "";
        try {
            shareEventVo.setMemNo(MemberVo.getMyMemNo(request));
            result = shareEventService.shareTailList(shareEventVo);
        } catch (Exception e) {
            log.error("ShareEventController Error => ", e);
        }
        return result;
    }

    @DeleteMapping("/tail/del")
    public String shareTailDel(@RequestBody ShareEventInputVo shareEventInputVo) {
        return shareEventService.shareTailDel(shareEventInputVo);
    }

    @PostMapping("/tail/ins")
    public String shareTailIns(@RequestBody ShareEventInputVo shareEventInputVo, HttpServletRequest request) {
        String clientIP = ipUtil.getClientIP(request);
        shareEventInputVo.setTailMemIp(clientIP);
        return shareEventService.shareTailIns(shareEventInputVo);
    }

    @PutMapping("/tail/upd")
    public String shareTailUpd(@RequestBody ShareEventInputVo shareEventInputVo) {
        return shareEventService.shareTailUpd(shareEventInputVo);
    }
}
