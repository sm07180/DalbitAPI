package com.dalbit.admin.controller;

import com.dalbit.admin.service.AdminCommonService;
import com.dalbit.admin.service.AdminMemberService;
import com.dalbit.admin.service.AdminService;
import com.dalbit.admin.vo.*;
import com.dalbit.admin.vo.procedure.*;
import com.dalbit.broadcast.service.ActionService;
import com.dalbit.broadcast.service.GuestService;
import com.dalbit.broadcast.vo.procedure.P_FreezeVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.MessageInsertVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/member")
public class AdminMemberController {

    @Autowired
    AdminMemberService adminMemberService;

    /**
     * 회원상세
     */
    @PostMapping("/detail")
    public String memberDetail(@RequestParam HashMap<String, String> paramMap) {
        String result = adminMemberService.memberDetail(paramMap);
        return result;
    }

    /**
     * 방송관리
     */
    @PostMapping("/broadcast/list")
    public String broadcastList(@RequestParam HashMap<String, String> paramMap) {
        String result = adminMemberService.broadcastList(paramMap);
        return result;
    }

    /**
     * 클립관리
     */
    @PostMapping("/clip/list")
    public String clipList(@RequestParam HashMap<String, Object> paramMap) {
        String result = adminMemberService.clipList(paramMap);
        return result;
    }

    /**
     * 클립관리
     */
    @PostMapping("/question/list")
    public String questionList(@RequestParam HashMap<String, Object> paramMap) {
        String result = adminMemberService.questionList(paramMap);
        return result;
    }
}
