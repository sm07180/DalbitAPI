package com.dalbit.member.controller;

import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.MemberStatus;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ResMessage;
import com.dalbit.common.vo.ResVO;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.ProfileBoardDetailOutVo;
import com.dalbit.member.vo.ProfileBoardDetailSelVo;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.request.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/profile")
@Scope("prototype")
public class ProfileController {

    @Autowired
    ProfileService profileService;
    @Autowired
    GsonUtil gsonUtil;

    /**
     * 정보 조회
     */
    @GetMapping("")
    public String memberInfo(@Valid ProfileVo profileVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        int memLogin = DalbitUtil.isLogin(request) ? 1 : 0;
        P_ProfileInfoVo apiData = new P_ProfileInfoVo(memLogin, MemberVo.getMyMemNo(request), profileVo.getMemNo());

        String result = profileService.callMemberInfo(apiData, request, true);

        return result;
    }


    /**
     * 회원 팬보드 댓글달기
     */
    @PostMapping("/board")
    public String fanboardAdd(@Valid FanboardAddVo fanboardAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanboardAddVo pFanboardAddVo = new P_FanboardAddVo(fanboardAddVo, request);

        String result = profileService.callMemberFanboardAdd(pFanboardAddVo);
        return result;
    }


    /**
     * 회원 팬보드 목록조회
     */
    @GetMapping("/board")
    public String fanboardList(@Valid FanboardViewVo fanboardViewVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            P_FanboardListVo fanboardListVo = new P_FanboardListVo(fanboardViewVo, request);

            String result = profileService.callMemberFanboardList(fanboardListVo);
            return result;
        }catch(Exception e){
            log.error("ProfileController.java / fanboardList () => Exception : param: {} / error: {}", gsonUtil.toJson(fanboardViewVo), e);
            HashMap fanBoardList = new HashMap();
            fanBoardList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.팬보드_댓글없음, fanBoardList));
        }
    }


    /**
     * 회원 팬보드 삭제하기
     */
    @DeleteMapping("/board")
    public String fanboardDelete(@Valid FanboardDelVo fanboardDelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanboardDeleteVo fanboardDeleteVo = new P_FanboardDeleteVo(fanboardDelVo, request);

        String result = profileService.callMemberFanboardDelete(fanboardDeleteVo);
        return result;
    }


    /**
     * 회원 팬보드 댓글수정
     */
    @PostMapping("/board/edit")
    public String fanboardEdit(@Valid FanboardEditVo fanboardEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanboardEditVo adpiData = new P_FanboardEditVo(fanboardEditVo, request);

        String result = profileService.callMemberFanboardEdit(adpiData);
        return result;
    }


    /**
     * 회원 팬보드 대댓글 조회하기
     */
    @GetMapping("/board/reply")
    public String fanboardReply(@Valid FanboardReplyVo fanboardReplyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanboardReplyVo pFanboardReplyVo = new P_FanboardReplyVo(fanboardReplyVo, request);

        String result = profileService.callMemberFanboardReply(pFanboardReplyVo);
        return result;
    }


    /**
     * 회원 팬 랭킹 조회
     */
    @GetMapping("/fan")
    public String fanRanking(@Valid FanRankingVo fanRankingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanRankingVo apiData = new P_FanRankingVo(fanRankingVo, request);

        String result = profileService.callMemberFanRanking(apiData, request);
        return result;
    }


    /**
     * 회원 레벨업 확인
     */
    @GetMapping("/levelup")
    public String levelUpCheck(HttpServletRequest request){
        P_LevelUpCheckVo apiData = new P_LevelUpCheckVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));

        String result = profileService.callMemberLevelUpCheck(apiData);
        return result;
    }


    /**
     * 회원 스타 랭킹 조회
     */
    @GetMapping("/star")
    public String starRanking(@Valid StarRankingVo starRankingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, profileService.selectStarRanking(starRankingVo, request)));
    }


    /**
     * 회원 팬 전체 리스트
     */
    @GetMapping("/fan/list")
    public String fanList(@Valid FanListVo fanListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanListVo apiData = new P_FanListVo(fanListVo, request);

        String result = profileService.callFanList(apiData);
        return result;
    }


    /**
     * NEW 회원 팬 전체 리스트
     */
    @GetMapping("/fan/list/new")
    public String fanListNew(@Valid FanListNewVo fanListNewVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanListNewVo apiData = new P_FanListNewVo(fanListNewVo, request);

        String result = profileService.callFanListNew(apiData, request);
        return result;
    }


    /**
     * 회원 팬 메모 조회
     */
    @GetMapping("/fan/memo")
    public String fanMemo(@Valid FanMemoVo fanMemoVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanMemoVo apiData = new P_FanMemoVo(fanMemoVo, request);

        String result = profileService.callFanMemo(apiData);
        return result;
    }


    /**
     * 회원 팬 메모 저장
     */
    @PostMapping("/fan/memo")
    public String fanMemoSave(@Valid FanMemoSaveVo fanMemoSaveVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanMemoSaveVo apiData = new P_FanMemoSaveVo(fanMemoSaveVo, request);

        String result = profileService.callFanMemoSave(apiData);
        return result;
    }


    /**
     * 회원 팬 리스트 편집
     */
    @PostMapping("/fan/edit")
    public String fanEdit(@Valid FanEditVo fanEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FanEditVo apiData = new P_FanEditVo(fanEditVo, request);

        String result = profileService.callFanEdit(apiData);
        return result;
    }


    /**
     * NEW 스타 리스트
     */
    @GetMapping("/star/list/new")
    public String starListNew(@Valid StarListNewVo starListNewVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_StarListNewVo apiData = new P_StarListNewVo(starListNewVo, request);

        String result = profileService.callStarListNew(apiData, request);
        return result;
    }


    /**
     * 스타 메모 조회
     */
    @GetMapping("/star/memo")
    public String starMemo(@Valid StarMemoVo starMemoVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_StarMemoVo apiData = new P_StarMemoVo(starMemoVo, request);

        String result = profileService.callStarMemo(apiData);
        return result;
    }


    /**
     * 스타 메모 저장
     */
    @PostMapping("/star/memo")
    public String starMemoSave(@Valid StarMemoSaveVo starMemoSaveVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_StarMemoSaveVo apiData = new P_StarMemoSaveVo(starMemoSaveVo, request);

        String result = profileService.callStarMemoSave(apiData);
        return result;
    }

    /**
     * 프로필 이미지 추가등록
     */
    @PostMapping("/add/img")
    public String profileImgAdd(@Valid ProfileImgAddVo profileImgAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ProfileImgAddVo apiData = new P_ProfileImgAddVo(profileImgAddVo, request);
        String result = profileService.callProfileImgAdd(apiData, request);
        return result;
    }

    /**
     * 프로필 이미지 삭제
     */
    @PostMapping("/delete/img")
    public String profileImgDelete(@Valid ProfileImgDeleteVo profileImgDeleteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ProfileImgDeleteVo apiData = new P_ProfileImgDeleteVo(profileImgDeleteVo, request);
        String result = profileService.callProfileImgDelete(apiData);
        return result;
    }

    /**
     * 프로필 이미지 대표지정
     */
    @PostMapping("/leader/img")
    public String profileImgLeader(@Valid ProfileImgLeaderVo profileImgLeaderVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ProfileImgLeaderVo apiData = new P_ProfileImgLeaderVo(profileImgLeaderVo, request);
        String result = profileService.callProfileImgLeader(apiData);
        return result;
    }

    /**********************************************************************************************
     * @Method 설명 : 프로필 수정 페이지 (프로필 사진 순서 변경하기)
     * @작성일 : 2022-04-08
     * @작성자 : 박용훈
     * @변경이력 :
     * @Parameter : { imageList: [ "/profile_0/21440390400/20220407140339284013.png", ... ] }
     * @Return :
     **********************************************************************************************/
    @PostMapping("/update/img")
    public String profileImgUpdate(@RequestBody Map param, HttpServletRequest request){
        return profileService.profileImageUpdate(param, request);
    }

    /**
     * 1일 1회 본인인증 가능 여부
     */
    @GetMapping("/certification/check")
    public ResVO callCertificationChkSel(HttpServletRequest request) {
        ResVO resVO = new ResVO();
        try {
            String memNo = MemberVo.getMyMemNo(request);
            resVO.setSuccessResVO(profileService.callCertificationChkSel(memNo));
        } catch (Exception e) {
            log.error("ProfileController / callCertificationChkSel => {}", e);
            resVO.setResVO(ResMessage.C99999.getCode(), ResMessage.C99999.getCodeNM(), null);
        }

        return resVO;
    }

    /**
     * 팬보드 상세 조회
     * @Param
     * memNo            String 프로필 주인 memNo
     * fanBoardNo       Integer  팬보드 글번호
     *
     * @Return
     * board_idx;                //BIGINT		-- 번호
     * writer_mem_no;            //BIGINT		-- 회원번호(작성자)
     * nickName;                 //VARCHAR	--닉네임(작성자)
     * userId;                   //VARCHAR	--아이디(작성자)
     * memSex;                   //VARCHAR	-- 성별(작성자)
     * profileImage;             //VARCHAR	-- 프로필(작성자)
     * STATUS;                   //BIGINT		-- 상태
     * viewOn;                   //BIGINT		-- 1:공개 0:비공개
     * writeDate;                //DATETIME	-- 수정일자
     * ins_date;                 //DATETIME	-- 등록일자
     * rcv_like_cnt;             //BIGINT		-- 좋아요수
     * rcv_like_cancel_cnt;      //BIGINT		-- 취소 좋아요수
     * like_yn;                  //CHAR		-- 좋아요 확인[y,n]
     * */
    @GetMapping("/board/detail")
    public String boardDetailSel(@Valid ProfileBoardDetailSelVo vo, HttpServletRequest request){
        try {
            ProfileBoardDetailOutVo result = profileService.boardDetailSel(vo, request);

            if(result != null) {
                return gsonUtil.toJson(new JsonOutputVo(MemberStatus.팬보드상세조회성공, result));
            } else {
                return gsonUtil.toJson(new JsonOutputVo(MemberStatus.팬보드상세조회정보없음));
            }
        } catch(Exception e) {
            log.error("ProfileController / boardDetailSel => {}", e);
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.팬보드상세조회실패));
        }
    }

}

