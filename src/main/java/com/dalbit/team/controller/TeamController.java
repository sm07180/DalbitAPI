package com.dalbit.team.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.util.GsonUtil;
import com.dalbit.team.service.TeamService;
import com.dalbit.team.vo.TeamParamVo;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/team")
@Scope("prototype")
public class TeamController {
    @Autowired
    private TeamService teamService;
    @Autowired
    private GsonUtil gsonUtil;

    /**********************************************************************************************
     * @Method 설명 : 팀 등록 체크
     * @작성일   : 2022-03-31
     * @작성자   : 이승재
     * @변경이력  :
     **********************************************************************************************/
    @PostMapping("/teamChk")
    public String getTeamInsChk(TeamParamVo vo){
        int result = teamService.getTeamInsChk(vo);
        return  gsonUtil.toJson(new JsonOutputVo(Status.조회, DalbitUtil.isEmpty(result) ? null : result));
    }
}
