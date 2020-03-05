package com.dalbit.member.controller;

import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.request.*;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;


    /**
     * 정보 조회
     */
    @GetMapping("")
    public String memberInfo(@Valid ProfileVo profileVo, BindingResult bindingResult) throws GlobalException {

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult);

        int memLogin = DalbitUtil.isLogin() ? 1 : 0;
        P_ProfileInfoVo apiData = new P_ProfileInfoVo(memLogin, MemberVo.getMyMemNo(), profileVo.getMemNo());

        String result = profileService.callMemberInfo(apiData);

        return result;
    }


    /**
     * 회원 팬보드 댓글달기
     */
    @PostMapping("/board")
    public String fanboardAdd(@Valid FanboardAddVo fanboardAddVo, BindingResult bindingResult) throws GlobalException{

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult);

        P_FanboardAddVo pFanboardAddVo = new P_FanboardAddVo();
        pFanboardAddVo.setStar_mem_no(fanboardAddVo.getMemNo());
        pFanboardAddVo.setWriter_mem_no(MemberVo.getMyMemNo());
        pFanboardAddVo.setDepth(fanboardAddVo.getDepth());
        pFanboardAddVo.setBoard_no(fanboardAddVo.getBoardNo());
        pFanboardAddVo.setContents(fanboardAddVo.getContent());

        String result = profileService.callMemberFanboardAdd(pFanboardAddVo);

        return result;
    }


    /**
     * 회원 팬보드 목록조회
     */
    @GetMapping("/board")
    public String fanboardList(@Valid FanboardViewVo fanboardViewVo, BindingResult bindingResult) throws GlobalException{

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult);

        int pageNo = DalbitUtil.isEmpty(fanboardViewVo.getPage()) ? 1 : fanboardViewVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(fanboardViewVo.getRecords()) ? 10 : fanboardViewVo.getRecords();

        P_FanboardListVo fanboardListVo = new P_FanboardListVo();
        fanboardListVo.setMem_no(MemberVo.getMyMemNo());
        fanboardListVo.setStar_mem_no(fanboardViewVo.getMemNo());
        fanboardListVo.setPageNo(pageNo);
        fanboardListVo.setPageCnt(pageCnt);

        String result = profileService.callMemberFanboardList(fanboardListVo);

        return result;
    }


    /**
     * 회원 팬보드 삭제하기
     */
    @DeleteMapping("/board")
    public String fanboardDelete(@Valid FanboardDelVo fanboardDelVo, BindingResult bindingResult) throws GlobalException{

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult);

        P_FanboardDeleteVo fanboardDeleteVo = new P_FanboardDeleteVo();
        fanboardDeleteVo.setStar_mem_no(fanboardDelVo.getMemNo());
        fanboardDeleteVo.setDelete_mem_no(MemberVo.getMyMemNo());
        fanboardDeleteVo.setBoard_idx(fanboardDelVo.getBoardIdx());

        String result = profileService.callMemberFanboardDelete(fanboardDeleteVo);
        return result;
    }


    /**
     * 회원 팬보드 대댓글 조회하기
     */
    @GetMapping("/board/reply")
    public String fanboardReply(@Valid FanboardReplyVo fanboardReplyVo, BindingResult bindingResult) throws GlobalException{

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult);

        P_FanboardReplyVo pFanboardReplyVo = new P_FanboardReplyVo();
        pFanboardReplyVo.setMem_no(MemberVo.getMyMemNo());
        pFanboardReplyVo.setStar_mem_no(fanboardReplyVo.getMemNo());
        pFanboardReplyVo.setBoard_no(fanboardReplyVo.getBoardNo());

        String result = profileService.callMemberFanboardReply(pFanboardReplyVo);
        return result;

    }


    /**
     * 회원 팬 랭킹 조회
     */
    @GetMapping("/fan")
    public String fanRanking(@Valid FanRankingVo fanRankingVo, BindingResult bindingResult) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_FanRankingVo apiData = new P_FanRankingVo(fanRankingVo);

        String result = profileService.callMemberFanRanking(apiData);
        return result;
    }

    /**
     * 회원 레벨업 확인
     */
    @GetMapping("/levelup")
    public String levelUpCheck(){
        P_LevelUpCheckVo apiData = new P_LevelUpCheckVo();
        apiData.setMem_no(MemberVo.getMyMemNo());

        String result = profileService.callMemberLevelUpCheck(apiData);

        return result;
    }
}
