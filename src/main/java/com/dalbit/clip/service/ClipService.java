package com.dalbit.clip.service;

import com.dalbit.clip.dao.ClipDao;
import com.dalbit.clip.vo.*;
import com.dalbit.clip.vo.procedure.*;
import com.dalbit.clip.vo.request.ClipGiftRankTop3Vo;
import com.dalbit.clip.vo.request.ClipMainSubjectTop3Vo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.main.service.MainService;
import com.dalbit.main.vo.BannerVo;
import com.dalbit.member.dao.MemberDao;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.IPUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Slf4j
@Service
public class ClipService {

    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    ClipDao clipDao;
    @Autowired
    RestService restService;
    @Autowired
    CommonService commonService;
    @Autowired
    MypageService mypageService;
    @Autowired
    MemberDao memberDao;
    @Autowired
    CommonDao commonDao;
    @Autowired
    IPUtil ipUtil;
    @Autowired
    MainService mainService;

    @Value("${item.direct.code}")
    private String[] ITEM_DIRECT_CODE;
    @Value("${item.direct.min}")
    private int[] ITEM_DIRECT_MIN;
    @Value("${item.direct.max}")
    private int[] ITEM_DIRECT_MAX;
    @Value("${item.direct.code.main}")
    private String ITEM_DIRECT_CODE_MAIN;

    /**
     * 클립 등록(업로드)
     */
    public String clipAdd(P_ClipAddVo pClipAddVo, HttpServletRequest request) throws GlobalException {

        //클립 업로드 불가 상태 체크
        if(!ipUtil.isInnerIP(ipUtil.getClientIP(request))){
            var codeVo = commonService.selectCodeDefine(new CodeVo(Code.시스템설정_클립막기.getCode(), Code.시스템설정_클립막기.getDesc()));
            if(!DalbitUtil.isEmpty(codeVo)){
                if(codeVo.getValue().equals("Y")){
                    return gsonUtil.toJson(new JsonOutputVo(Status.설정_클립업로드_참여불가상태));
                }
            }
        }

        //차단관리 테이블 확인
        int adminBlockCnt = memberDao.selectAdminBlock(new BlockVo(new DeviceVo(request), MemberVo.getMyMemNo(request)));
        if(0 < adminBlockCnt){
            return gsonUtil.toJson(new JsonOutputVo(Status.차단_이용제한));
        }


        String systemBanWord = commonService.banWordSelect();

        // 부적절한문자열 체크 ( "\r", "\n", "\t")
        if(DalbitUtil.isCheckSlash(pClipAddVo.getTitle())){
            return gsonUtil.toJson(new JsonOutputVo(Status.부적절한문자열));
        }

        //금지어 체크(제목)
        if(DalbitUtil.isStringMatchCheck(systemBanWord, pClipAddVo.getTitle())){
            return gsonUtil.toJson(new JsonOutputVo(Status.클립등록제목금지));
        }

        //클립재생시간 10분 체크
        int min = Integer.parseInt(pClipAddVo.getFilePlayTime().split(":")[0])*60;
        int sec = Integer.parseInt(pClipAddVo.getFilePlayTime().split(":")[1]);
        if(min + sec > 600){
            return gsonUtil.toJson(new JsonOutputVo(Status.클립재생시간_10분초과));
        }

        String bgImg = pClipAddVo.getBackgroundImage();
        Boolean isDone = false;
        if(DalbitUtil.isEmpty(bgImg)){
            int random = Integer.parseInt(DalbitUtil.randomClipBgValue());
            bgImg = Code.클립_배경_디폴트_PREFIX.getCode()+"/"+Code.클립배경이미지_파일명_PREFIX.getCode()+"200910_"+random+".jpg";
        }else{
            if(bgImg.startsWith(Code.클립_배경_임시_PREFIX.getCode())){
                isDone = true;
            }
            bgImg = DalbitUtil.replacePath(bgImg);
        }

        pClipAddVo.setFilePath(DalbitUtil.replacePath(pClipAddVo.getFilePath()));  //클립 파일
        pClipAddVo.setBackgroundImage(bgImg);

        ProcedureVo procedureVo = new ProcedureVo(pClipAddVo);
        clipDao.callClipAdd(procedureVo);

        String result="";
        if(Status.클립등록_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("clipNo", DalbitUtil.getStringMap(resultMap, "cast_no"));
            if(isDone){
                restService.imgDone(DalbitUtil.replaceDonePath(pClipAddVo.getBackgroundImage()), request);
            }
            restService.imgDone(DalbitUtil.replaceDonePath(pClipAddVo.getFilePath()), request);
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립등록_성공, returnMap));
        }else if(Status.클립등록_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립등록_회원아님));
        }else if(Status.클립등록_20세제한.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립등록_20세제한));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립등록_실패));
        }
        return result;
    }


    /**
     * 클립 수정
     */
    public String clipEdit(P_ClipEditVo pClipEditVo, HttpServletRequest request) throws GlobalException {
        String systemBanWord = commonService.banWordSelect();

        // 부적절한문자열 체크 ( "\r", "\n", "\t")
        if(DalbitUtil.isCheckSlash(pClipEditVo.getTitle())){
            return gsonUtil.toJson(new JsonOutputVo(Status.부적절한문자열));
        }

        //금지어 체크(제목)
        if(DalbitUtil.isStringMatchCheck(systemBanWord, pClipEditVo.getTitle())){
            return gsonUtil.toJson(new JsonOutputVo(Status.클립등록제목금지));
        }

        String bgImg = pClipEditVo.getBackgroundImage();
        Boolean isDone = false;
        if(DalbitUtil.isEmpty(bgImg)){
            int random = Integer.parseInt(DalbitUtil.randomBgValue());
            bgImg = Code.클립_배경_디폴트_PREFIX.getCode()+"/"+Code.클립배경이미지_파일명_PREFIX.getCode()+"200821_"+random+".jpg";
        }else{
            if(bgImg.startsWith(Code.클립_배경_임시_PREFIX.getCode())){
                isDone = true;
            }
            bgImg = DalbitUtil.replacePath(bgImg);
        }

        pClipEditVo.setBackgroundImage(bgImg);

        ProcedureVo procedureVo = new ProcedureVo(pClipEditVo);
        clipDao.callClipEdit(procedureVo);

        String result="";
        if(Status.클립수정_성공.getMessageCode().equals(procedureVo.getRet())) {
            if(isDone){
                restService.imgDone(DalbitUtil.replaceDonePath(pClipEditVo.getBackgroundImage()), request);
            }

            P_ClipPlayVo pClipPlayVo = new P_ClipPlayVo();
            pClipPlayVo.setCast_no(pClipEditVo.getCast_no());
            pClipPlayVo.setMem_no(pClipEditVo.getMem_no());
            pClipPlayVo.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
            result = clipPlay(pClipPlayVo, "edit", request);

        }else if(Status.클립수정_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립수정_회원아님));
        }else if(Status.클립수정_클립없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립수정_클립없음));
        }else if(Status.클립수정_회원번호_클립회원번호아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립수정_회원번호_클립회원번호아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립수정_실패));
        }
        return result;
    }


    /**
     * 클립 리스트
     */
    public String clipList(P_ClipListVo pClipListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipListVo);
        List<P_ClipListVo> clipListVo = clipDao.callClipList(procedureVo);

        HashMap clipList = new HashMap();
        if(DalbitUtil.isEmpty(clipListVo)){
            clipList.put("list", new ArrayList<>());
            clipList.put("totalCnt", 0);
            clipList.put("paging", new PagingVo(0, pClipListVo.getPageNo(), pClipListVo.getPageCnt()));
            return gsonUtil.toJson(new JsonOutputVo(Status.클립리스트_조회_없음, clipList));
        }
        List<ClipListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<clipListVo.size(); i++){
            outVoList.add(new ClipListOutVo(clipListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        clipList.put("list", procedureOutputVo.getOutputBox());
        clipList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립리스트_조회_성공, clipList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립리스트_조회_실패));
        }
        return result;
    }


    /**
     * 클립 플레이
     */
    public String clipPlay(P_ClipPlayVo pClipPlayVo, String state, HttpServletRequest request) {

        //클립 플레이 불가 상태 체크
        if(!ipUtil.isInnerIP(ipUtil.getClientIP(request))){
            var codeVo = commonService.selectCodeDefine(new CodeVo(Code.시스템설정_클립막기.getCode(), Code.시스템설정_클립막기.getDesc()));
            if(!DalbitUtil.isEmpty(codeVo)){
                if(codeVo.getValue().equals("Y")){
                    return gsonUtil.toJson(new JsonOutputVo(Status.설정_클립업로드_참여불가상태));
                }
            }
        }

        //차단관리 테이블 확인
        int adminBlockCnt = memberDao.selectAdminBlock(new BlockVo(new DeviceVo(request), MemberVo.getMyMemNo(request)));
        if(0 < adminBlockCnt){
            return gsonUtil.toJson(new JsonOutputVo(Status.차단_이용제한));
        }

        //비회원 플레이 불가
        if(!DalbitUtil.isLogin(request)){
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인필요));
        }

        ProcedureVo procedureVo = new ProcedureVo(pClipPlayVo);
        clipDao.callClipPlay(procedureVo);
        String result="";
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("subjectName", "");
        returnMap.put("clipNo", DalbitUtil.getStringMap(resultMap, "cast_no"));
        returnMap.put("clipMemNo", DalbitUtil.getStringMap(resultMap, "cast_mem_no"));
        returnMap.put("subjectType", DalbitUtil.getStringMap(resultMap, "subject_type"));
        List<CodeVo> codeVoList = commonService.selectClipTypeCodeList(new CodeVo(Code.클립주제.getCode()));
        for (int i=0; i < codeVoList.size(); i++){
            if(DalbitUtil.getStringMap(resultMap, "subject_type").equals(codeVoList.get(i).getValue())){
                returnMap.put("subjectName", codeVoList.get(i).getCdNm());
            }
        }
        returnMap.put("title", DalbitUtil.getStringMap(resultMap, "title"));
        returnMap.put("bgImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "image_background"), DalbitUtil.getProperty("server.photo.url")));
        returnMap.put("fileName", DalbitUtil.getStringMap(resultMap, "file_name"));
        returnMap.put("file", new ClipFileVo(DalbitUtil.getStringMap(resultMap, "file_path"), DalbitUtil.getProperty("server.photo.url")));
        returnMap.put("filePlay", DalbitUtil.getStringMap(resultMap, "file_play"));
        returnMap.put("codeLink", DalbitUtil.getStringMap(resultMap, "code_link"));
        returnMap.put("nickName", DalbitUtil.getStringMap(resultMap, "mem_nick"));
        returnMap.put("isGood", DalbitUtil.getIntMap(resultMap, "good") == 1);
        returnMap.put("playCnt", DalbitUtil.getIntMap(resultMap, "playCnt"));
        returnMap.put("goodCnt", DalbitUtil.getIntMap(resultMap, "goodCnt"));
        returnMap.put("byeolCnt", DalbitUtil.getIntMap(resultMap, "giftCnt"));
        returnMap.put("entryType", DalbitUtil.getIntMap(resultMap, "entryType"));
        returnMap.put("openType", DalbitUtil.getIntMap(resultMap, "openType"));
        returnMap.put("isFan", (DalbitUtil.isLogin(request) && DalbitUtil.getIntMap(resultMap, "enableFan") == 0));
        returnMap.put("replyCnt", DalbitUtil.getIntMap(resultMap, "boardCnt"));
        returnMap.put("regCnt", DalbitUtil.getIntMap(resultMap, "regCnt"));
        returnMap.put("isSpecial", DalbitUtil.getIntMap(resultMap, "specialBadge") > 0);
        returnMap.put("badgeSpecial", DalbitUtil.getIntMap(resultMap, "specialBadge"));
        returnMap.put("level", DalbitUtil.getIntMap(resultMap, "level"));
        returnMap.put("grade", DalbitUtil.getStringMap(resultMap, "grade"));
        returnMap.put("gender", DalbitUtil.getStringMap(resultMap, "memSex"));
        returnMap.put("profImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), DalbitUtil.getStringMap(resultMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
        returnMap.put("holder", StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", DalbitUtil.getIntMap(resultMap, "level") + ""));
        returnMap.put("playIdx", DalbitUtil.getIntMap(resultMap, "playIdx"));

        DeviceVo deviceVo = new DeviceVo(request);
        boolean playlistOpen = true;
        returnMap.put("playlistOpen", playlistOpen);
        returnMap.put("replyOpen", true);
        returnMap.put("playCntOpen", DalbitUtil.getStringMap(resultMap, "cast_mem_no").equals(pClipPlayVo.getMem_no()));
        returnMap.put("goodCntOpen", true);
        returnMap.put("byeolCntOpen", true);
        returnMap.put("replyCntOpen", true);

        //ios 심사중이면 false;
        boolean eventOpen = false; //2020.12.23 김자운 주임 요청
        if(deviceVo.getOs() == 2) {
            var iosCodeVo = commonService.selectCodeDefine(new CodeVo(Code.IOS심사중여부.getCode(), Code.IOS심사중여부.getDesc()));
            if (!DalbitUtil.isEmpty(iosCodeVo)) {
                if("Y".equals(iosCodeVo.getValue())) {
                    eventOpen = false;
                }
            }
        }
        returnMap.put("eventOpen", eventOpen);
        returnMap.put("eventUrl", DalbitUtil.getProperty("server.mobile.url") + "/event/clip_gift_event?webview=new");
        returnMap.put("coverTitle", DalbitUtil.getStringMap(resultMap, "coverTitle"));
        returnMap.put("coverSinger", DalbitUtil.getStringMap(resultMap, "coverSinger"));

        //텍스트 배너 설정
        List<HashMap> banners = new ArrayList<>();
        List<BannerVo> bannerList = mainService.selectBanner("16", request);
        if(!DalbitUtil.isEmpty(bannerList)){
            for(BannerVo bannerVo : bannerList){
                if(!DalbitUtil.isEmpty(bannerVo.getContents())){
                    HashMap<String, String> bannerItem = new HashMap<>();
                    bannerItem.put("text", bannerVo.getContents());
                    bannerItem.put("link", bannerVo.getLinkUrl());
                    banners.add(bannerItem);
                }
            }
        }
        returnMap.put("banners", banners);

        ClipGiftRankTop3Vo clipGiftRankTop3Vo = new ClipGiftRankTop3Vo();
        clipGiftRankTop3Vo.setClipNo(pClipPlayVo.getCast_no());
        P_ClipGiftRankTop3Vo pClipGiftRankTop3Vo = new P_ClipGiftRankTop3Vo(clipGiftRankTop3Vo);
        returnMap.put("list", clipGiftRankTop3PlayCall(pClipGiftRankTop3Vo).get("list"));
        if(Status.클립플레이_성공.getMessageCode().equals(procedureVo.getRet())) {
            if("edit".equals(state)) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.클립수정_성공, returnMap));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(Status.클립플레이_성공, returnMap));
            }
        }else if(Status.클립플레이_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립플레이_회원아님, returnMap));
        }else if(Status.클립플레이_클립번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립플레이_클립번호없음, returnMap));
        }else if(Status.클립플레이_팬아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립플레이_팬아님, returnMap));
        }else if(Status.클립플레이_20세미만.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립플레이_20세미만, returnMap));
        }else if(Status.클립플레이_차단회원재생불가.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립플레이_차단회원재생불가, returnMap));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립플레이_실패, returnMap));
        }
        return result;
    }


    /**
     * 클립 재생목록
     */
    public String clipPlayList(P_ClipPlayListVo pClipPlayListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipPlayListVo);
        List<P_ClipPlayListVo> clipPlayListVo = clipDao.callClipPlayList(procedureVo);

        HashMap clipPlayList = new HashMap();
        if(DalbitUtil.isEmpty(clipPlayListVo)){
            clipPlayList.put("list", new ArrayList<>());
            clipPlayList.put("totalCnt", 0);
            clipPlayList.put("paging", new PagingVo(0, pClipPlayListVo.getPageNo(), pClipPlayListVo.getPageCnt()));
            return gsonUtil.toJson(new JsonOutputVo(Status.클립_재생목록_조회_없음, clipPlayList));
        }
        List<ClipPlayListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<clipPlayListVo.size(); i++){
            outVoList.add(new ClipPlayListOutVo(clipPlayListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        clipPlayList.put("list", procedureOutputVo.getOutputBox());
        clipPlayList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_재생목록_조회_성공, clipPlayList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_재생목록_조회_실패));
        }
        return result;
    }


    /**
     * 클립 선물하기
     */
    public String clipGift(P_ClipGiftVo pClipGiftVo) {
        String item_code = pClipGiftVo.getItem_code();
        boolean isDirect = false;
        int directItemCnt = 1;
        if(ITEM_DIRECT_CODE_MAIN.equals(pClipGiftVo.getItem_code())){ //직접선물하기 일경우 체크
            isDirect = true;
            for(int i = 0; i <  ITEM_DIRECT_MIN.length; i++){
                if (ITEM_DIRECT_MIN[i] <= pClipGiftVo.getItem_cnt() && (ITEM_DIRECT_MAX[i] >= pClipGiftVo.getItem_cnt() || ITEM_DIRECT_MAX[i] == -1)) {
                    item_code = ITEM_DIRECT_CODE[i];
                    if(ITEM_DIRECT_MAX[i] == -1){
                        directItemCnt = 2;
                    }
                    break;
                }
            }
        }
        ProcedureVo procedureVo = new ProcedureVo(pClipGiftVo);
        clipDao.callClipGift(procedureVo);

        String result="";
        if(Status.클립_선물하기_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            ItemDetailVo item = commonDao.selectItem(item_code);
            returnMap.put("level", DalbitUtil.getIntMap(resultMap, "level"));
            returnMap.put("grade", DalbitUtil.getStringMap(resultMap, "grade"));
            returnMap.put("exp", DalbitUtil.getIntMap(resultMap, "exp"));
            returnMap.put("expBegin", DalbitUtil.getIntMap(resultMap, "expBegin"));
            returnMap.put("expNext", DalbitUtil.getIntMap(resultMap, "expNext"));
            returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1);
            returnMap.put("dalCnt", DalbitUtil.getIntMap(resultMap, "ruby"));
            returnMap.put("byeolCnt", DalbitUtil.getIntMap(resultMap, "gold"));
            returnMap.put("giftCnt", DalbitUtil.getIntMap(resultMap, "giftCnt"));
            returnMap.put("nickNm", DalbitUtil.getStringMap(resultMap, "nickName"));
            returnMap.put("profImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), DalbitUtil.getStringMap(resultMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
            returnMap.put("itemCnt", isDirect ? 1 : pClipGiftVo.getItem_cnt());
            returnMap.put("itemNo", item_code);
            returnMap.put("itemType", isDirect ? "direct" : "items");
//            returnMap.put("repeatCnt", isDirect ? directItemCnt : (DalbitUtil.isEmpty(item.getSoundFileUrl()) ? pClipGiftVo.getItem_cnt() : pClipGiftVo.getItem_cnt() * 2));
            returnMap.put("repeatCnt", isDirect ? directItemCnt : pClipGiftVo.getItem_cnt());
            ClipGiftRankTop3Vo clipGiftRankTop3Vo = new ClipGiftRankTop3Vo();
            clipGiftRankTop3Vo.setClipNo(pClipGiftVo.getCast_no());
            P_ClipGiftRankTop3Vo pClipGiftRankTop3Vo = new P_ClipGiftRankTop3Vo(clipGiftRankTop3Vo);
            returnMap.put("list", clipGiftRankTop3PlayCall(pClipGiftRankTop3Vo).get("list"));

            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물하기_성공, returnMap));
        }else if(Status.클립_선물하기_요청회원_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물하기_요청회원_회원아님));
        }else if(Status.클립_선물하기_대상회원_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물하기_대상회원_회원아님));
        }else if(Status.클립_선물하기_클립번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물하기_클립번호없음));
        }else if(Status.클립_선물하기_클립번호_대상회원번호_불일치.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물하기_클립번호_대상회원번호_불일치));
        }else if(Status.클립_선물하기_본인불가.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물하기_본인불가));
        }else if(Status.클립_선물하기_아이템코드없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물하기_아이템코드없음));
        }else if(Status.클립_선물하기_달부족.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물하기_달부족));
        }else if(Status.이전작업대기중.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.이전작업대기중));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물하기_실패));
        }

        return result;
    }


    /**
     * 클립 좋아요
     */
    public String clipGood(P_ClipGoodVo pClipGoodVo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipGoodVo);
        clipDao.callClipGood(procedureVo);

        String result="";
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("goodCnt", DalbitUtil.getIntMap(resultMap, "good_cnt"));
        //Status status = pClipGoodVo.getGood() == 1 ? Status.클립_좋아요_성공 : Status.클립_좋아요_해제_성공;
        returnMap.put("isGood", pClipGoodVo.getGood() == 1);
        if(Status.클립_좋아요_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_좋아요_성공, returnMap));
        }else if(Status.클립_좋아요_요청회원_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_좋아요_요청회원_회원아님));
        }else if(Status.클립_좋아요_클립번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_좋아요_클립번호없음));
        }else if (Status.클립_좋아요_이미좋아요누름.getMessageCode().equals(procedureVo.getRet())) {
            returnMap.put("isGood", true);
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_좋아요_이미좋아요누름, returnMap));
        }else if(Status.클립_좋아요_차단회원.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_좋아요_차단회원));
        }else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_좋아요_실패));
        }

        return result;
    }


    /**
     * 클립 선물랭킹 TOP3
     */
    public String clipGiftRankTop3(P_ClipGiftRankTop3Vo pClipGiftRank3Vo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipGiftRank3Vo);
        List<P_ClipGiftRankTop3Vo> clipGiftRankTop3VoList = clipDao.callClipGiftRankTop3List(procedureVo);

        HashMap clipGiftRankTop3List = new HashMap();
        if(DalbitUtil.isEmpty(clipGiftRankTop3VoList)){
            clipGiftRankTop3List.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.클립_선물랭킹_TOP3_조회_없음, clipGiftRankTop3List));
        }
        List<ClipGiftRank3ListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<clipGiftRankTop3VoList.size(); i++){
            outVoList.add(new ClipGiftRank3ListOutVo(clipGiftRankTop3VoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        clipGiftRankTop3List.put("list", procedureOutputVo.getOutputBox());

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물랭킹_TOP3_조회_성공, clipGiftRankTop3List));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물랭킹_TOP3_조회_실패));
        }
        return result;
    }


    /**
     * 클립 선물랭킹 리스트
     */
    public String clipRankList(P_ClipGiftRankListVo pClipGiftRankListVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pClipGiftRankListVo);
        List<P_ClipGiftRankListVo> clipGiftRankListVo = clipDao.callClipGiftRankList(procedureVo);

        HashMap clipGiftRankList = new HashMap();
        if(DalbitUtil.isEmpty(clipGiftRankListVo)){
            clipGiftRankList.put("list", new ArrayList<>());
            clipGiftRankList.put("giftCnt", 0);
            clipGiftRankList.put("byeolCnt", 0);
            clipGiftRankList.put("totalCnt", Integer.valueOf(procedureVo.getRet()));
            DeviceVo deviceVo = new DeviceVo(request);
            if(deviceVo.getOs() == 2){
                clipGiftRankList.put("paging", new PagingVo(Integer.valueOf(procedureVo.getRet()), pClipGiftRankListVo.getPageNo(), pClipGiftRankListVo.getPageCnt()));
            }else{
                clipGiftRankList.put("paging", new PagingVo(0, pClipGiftRankListVo.getPageNo(), pClipGiftRankListVo.getPageCnt()));
            }
            return gsonUtil.toJson(new JsonOutputVo(Status.클립_선물랭킹_조회_없음, clipGiftRankList));
        }

        List<ClipGiftRankListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<clipGiftRankListVo.size(); i++){
            outVoList.add(new ClipGiftRankListOutVo(clipGiftRankListVo.get(i)));
        }
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        clipGiftRankList.put("list", outVoList);
        clipGiftRankList.put("giftCnt", DalbitUtil.getIntMap(resultMap, "giftCnt"));
        clipGiftRankList.put("byeolCnt", DalbitUtil.getIntMap(resultMap, "giftedByeol"));
        clipGiftRankList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물랭킹_조회_성공, clipGiftRankList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_선물랭킹_조회_실패));
        }
        return result;
    }


    /**
     * 클립 받은선물내역
     */
    public String clipGiftList(P_ClipGiftListVo pClipGiftListVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pClipGiftListVo);
        List<P_ClipGiftListVo> clipGiftListVo = clipDao.callClipGiftList(procedureVo);

        HashMap clipGiftList = new HashMap();
        if(DalbitUtil.isEmpty(clipGiftListVo)){
            ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo);
            clipGiftList.put("list", new ArrayList<>());
            clipGiftList.put("giftCnt", 0);
            clipGiftList.put("byeolCnt", 0);
            clipGiftList.put("totalCnt", Integer.valueOf(procedureOutputVo.getRet()));
            DeviceVo deviceVo = new DeviceVo(request);
            if(deviceVo.getOs() == 2){
                clipGiftList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pClipGiftListVo.getPageNo(), pClipGiftListVo.getPageCnt()));
            }else{
                clipGiftList.put("paging", new PagingVo(0, pClipGiftListVo.getPageNo(), pClipGiftListVo.getPageCnt()));
            }
            return gsonUtil.toJson(new JsonOutputVo(Status.클립_받은선물내역_조회_없음, clipGiftList));
        }
        List<ClipGiftListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<clipGiftListVo.size(); i++){
            outVoList.add(new ClipGiftListOutVo(clipGiftListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        clipGiftList.put("list", procedureOutputVo.getOutputBox());
        clipGiftList.put("giftCnt", DalbitUtil.getIntMap(resultMap, "giftCnt"));
        clipGiftList.put("byeolCnt", DalbitUtil.getIntMap(resultMap, "giftedByeol"));
        clipGiftList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_받은선물내역_조회_성공, clipGiftList));
        }else if(Status.클립_받은선물내역_조회_요청회원_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_받은선물내역_조회_요청회원_회원아님));
        }else if(Status.클립_받은선물내역_조회_클립없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_받은선물내역_조회_클립없음));
        }else if(Status.클립_받은선물내역_조회_요청회원번호_클립회원번호아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_받은선물내역_조회_요청회원번호_클립회원번호아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_받은선물내역_조회_실패));
        }
        return result;

    }


    /**
     * 클립 삭제
     */
    public String clipDelete(P_ClipDeleteVo pClipDeleteVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pClipDeleteVo);
        clipDao.callClipDelete(procedureVo);

        String result="";
        if(Status.클립삭제_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립삭제_성공));
        }else if(Status.클립삭제_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립삭제_회원아님));
        }else if(Status.클립삭제_클립없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립삭제_클립없음));
        }else if(Status.클립삭제_회원번호_클립회원번호아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립삭제_회원번호_클립회원번호아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립삭제_실패));
        }

        return result;
    }


    /**
     * 클립 댓글 리스트
     */
    public String clipReplyList(P_ClipReplyListVo pClipPlayListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipPlayListVo);
        List<P_ClipReplyListVo> clipReplyListVo = clipDao.callClipReplyList(procedureVo);

        HashMap clipReplyList = new HashMap();
        if(DalbitUtil.isEmpty(clipReplyListVo)){
            clipReplyList.put("list", new ArrayList<>());
            clipReplyList.put("totalCnt", 0);
            clipReplyList.put("paging", new PagingVo(0, pClipPlayListVo.getPageNo(), pClipPlayListVo.getPageCnt()));
            return gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글목록_조회_없음, clipReplyList));
        }
        List<BoardVo> outVoList = new ArrayList<>();
        for (int i=0; i<clipReplyListVo.size(); i++){
            outVoList.add(new BoardVo(clipReplyListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        clipReplyList.put("list", procedureOutputVo.getOutputBox());
        clipReplyList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글목록_조회_성공, clipReplyList));
        }else if(Status.클립_댓글목록_조회_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글목록_조회_회원아님));
        }else if(Status.클립_댓글목록_조회_클립없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글목록_조회_클립없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글목록_조회_실패));
        }
        return result;
    }


    /**
     * 클립 댓글 등록
     */
    public String clipReplyAdd(P_ClipReplyAddVo pClipReplyAddVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pClipReplyAddVo);
        clipDao.callClipReplyAdd(procedureVo);

        String result="";
        if(Status.클립_댓글등록_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap clipReplyList = new HashMap();
            P_ClipPlayVo pClipPlayVo = new P_ClipPlayVo(pClipReplyAddVo, request);
            clipReplyList.put("clipPlayInfo", clipPlayInfo(pClipPlayVo, request));
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글등록_성공, clipReplyList));
        }else if(Status.클립_댓글등록_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글등록_회원아님));
        }else if(Status.클립_댓글등록_클립없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글등록_클립없음));
        }else if(Status.클립_댓글등록_차단회원.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글등록_차단회원));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글등록_실패));
        }
        return result;
    }


    /**
     * 클립 댓글 수정
     */
    public String clipReplyEdit(P_ClipReplyEditVo pClipReplyEditVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pClipReplyEditVo);
        clipDao.callClipReplyEdit(procedureVo);

        String result="";
        if(Status.클립_댓글수정_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap clipReplyList = new HashMap();
            P_ClipPlayVo pClipPlayVo = new P_ClipPlayVo(pClipReplyEditVo, request);
            clipReplyList.put("clipPlayInfo", clipPlayInfo(pClipPlayVo, request));
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글수정_성공, clipReplyList));
        }else if(Status.클립_댓글수정_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글수정_회원아님));
        }else if(Status.클립_댓글수정_클립없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글수정_클립없음));
        }else if(Status.클립_댓글수정_댓글번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글수정_댓글번호없음));
        }else if(Status.클립_댓글수정_작성자아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글수정_작성자아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글수정_실패));
        }
        return result;
    }


    /**
     * 클립 댓글 삭제
     */
    public String clipReplyDelete(P_ClipReplyDeleteVo pClipReplyDeleteVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pClipReplyDeleteVo);
        clipDao.callClipReplyDelete(procedureVo);

        String result="";
        if(Status.클립_댓글삭제_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap clipReplyList = new HashMap();
            P_ClipPlayVo pClipPlayVo = new P_ClipPlayVo(pClipReplyDeleteVo, request);
            clipReplyList.put("clipPlayInfo", clipPlayInfo(pClipPlayVo, request));
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글삭제_성공, clipReplyList));
        }else if(Status.클립_댓글삭제_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글삭제_회원아님));
        }else if(Status.클립_댓글삭제_클립없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글삭제_클립없음));
        }else if(Status.클립_댓글삭제_댓글번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글삭제_댓글번호없음));
        }else if(Status.클립_댓글삭제_작성자아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글삭제_작성자아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_댓글삭제_실패));
        }
        return result;
    }


    /**
     * 마이페이지 클립 업로드 리스트
     */
    public String clipUploadList(P_ClipUploadListVo pClipUploadListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipUploadListVo);
        List<P_ClipUploadListVo> clipUploadListVo = clipDao.callClipUploadList(procedureVo);

        HashMap clipUploadList = new HashMap();
        //clipUploadList.put("count", mypageService.getMemberBoardCount(pClipUploadListVo));
        if(DalbitUtil.isEmpty(clipUploadListVo)){
            clipUploadList.put("list", new ArrayList<>());
            clipUploadList.put("totalCnt", 0);
            clipUploadList.put("paging", new PagingVo(0, pClipUploadListVo.getPageNo(), pClipUploadListVo.getPageCnt()));
            return gsonUtil.toJson(new JsonOutputVo(Status.클립_업로드목록_조회_없음, clipUploadList));
        }
        List<ClipUploadListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<clipUploadListVo.size(); i++){
            outVoList.add(new ClipUploadListOutVo(clipUploadListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        clipUploadList.put("list", procedureOutputVo.getOutputBox());
        clipUploadList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_업로드목록_조회_성공, clipUploadList));
        }else if(Status.클립_업로드목록_조회_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_업로드목록_조회_회원아님));
        }else if(Status.클립_업로드목록_조회_스타회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_업로드목록_조회_스타회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_업로드목록_조회_실패));
        }
        return result;
    }


    /**
     * 마이페이지 클립 청취내역
     */
    public String clipListenList(P_ClipListenListVo pClipListenListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipListenListVo);
        List<P_ClipListenListVo> clipListenListVo = clipDao.callClipListenList(procedureVo);

        HashMap clipListenList = new HashMap();
        if(DalbitUtil.isEmpty(clipListenListVo)){
            clipListenList.put("list", new ArrayList<>());
            clipListenList.put("totalCnt", 0);
            clipListenList.put("paging", new PagingVo(0, pClipListenListVo.getPageNo(), pClipListenListVo.getPageCnt()));
            return gsonUtil.toJson(new JsonOutputVo(Status.클립_청취내역_조회_없음, clipListenList));
        }
        List<ClipListenListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<clipListenListVo.size(); i++){
            outVoList.add(new ClipListenListOutVo(clipListenListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        clipListenList.put("list", procedureOutputVo.getOutputBox());
        clipListenList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_청취내역_조회_성공, clipListenList));
        }else if(Status.클립_청취내역_조회_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_청취내역_조회_회원아님));
        }else if(Status.클립_청취내역_조회_스타회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_청취내역_조회_스타회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_청취내역_조회_실패));
        }
        return result;
    }


    /**
     * 클립 재생목록 편집
     */
    public String clipPlayListEdit(P_ClipPlayListEditVo pClipPlayListEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipPlayListEditVo);
        clipDao.callClipPlayListEdit(procedureVo);

        String result="";
        if(Status.클립_재생목록편집_성공.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_재생목록편집_성공));
        }else if(Status.클립_재생목록편집_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_재생목록편집_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_재생목록편집_실패));
        }
        return result;
    }


    /**
     * 클립 메인 인기리스트
     */
    public String clipMainPopList(P_ClipMainPopListVo pClipMainPopListVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pClipMainPopListVo);
        List<P_ClipMainPopListVo> clipMainPopListVo = clipDao.callClipMainPopList(procedureVo);

        HashMap clipMainPopList = new HashMap();
        if(DalbitUtil.isEmpty(clipMainPopListVo)){
            clipMainPopList.put("list", new ArrayList<>());
            clipMainPopList.put("totalCnt", 0);
            return gsonUtil.toJson(new JsonOutputVo(Status.클립_메인_인기리스트_조회_없음, clipMainPopList));
        }

        List<ClipMainPopListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<clipMainPopListVo.size(); i++){
            outVoList.add(new ClipMainPopListOutVo(clipMainPopListVo.get(i)));
        }

        boolean playlist = false;
        if(!DalbitUtil.isEmpty(request.getParameter("playlist"))){
            playlist = ("true".equals(request.getParameter("playlist").toLowerCase()) || Integer.parseInt(request.getParameter("playlist")) == 1);
        }

        //리스트 셔플
        if(!playlist){
            Collections.shuffle(outVoList);
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        clipMainPopList.put("list", procedureOutputVo.getOutputBox());
        clipMainPopList.put("type", DalbitUtil.getIntMap(resultMap, "type"));
        clipMainPopList.put("totalCnt", procedureOutputVo.getRet());
        clipMainPopList.put("checkDate", DalbitUtil.getStringMap(resultMap, "checkDate"));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_메인_인기리스트_조회_성공, clipMainPopList));
        }else if(Status.클립_메인_인기리스트_조회_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_메인_인기리스트_조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_메인_인기리스트_조회_실패));
        }
        return result;
    }


    /**
     * 클립 메인 최신리스트
     */
    public String clipMainLatestList(P_ClipMainLatestListVo pClipMainLatestListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipMainLatestListVo);
        List<P_ClipMainLatestListVo> clipMainLatestListVo = clipDao.callClipMainLatestList(procedureVo);

        HashMap clipMainLatestList = new HashMap();
        if(DalbitUtil.isEmpty(clipMainLatestListVo)){
            clipMainLatestList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.클립_메인_최신리스트_조회_없음, clipMainLatestList));
        }
        List<ClipMainLatestListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<clipMainLatestListVo.size(); i++){
            outVoList.add(new ClipMainLatestListOutVo(clipMainLatestListVo.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        clipMainLatestList.put("list", procedureOutputVo.getOutputBox());
        //clipMainPopList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_메인_최신리스트_조회_성공, clipMainLatestList));
        }else if(Status.클립_메인_인기리스트_조회_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_메인_최신리스트_조회_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_메인_최신리스트_조회_실패));
        }
        return result;
    }


    /**
     * 클립 메인 주제별 TOP3 리스트
     */
    public String clipMainSubjectTop3List(HttpServletRequest request) {

        List<CodeVo> codeVoList = commonService.selectClipTypeCodeList(new CodeVo(Code.클립주제.getCode()));
        if(DalbitUtil.isEmpty(codeVoList)){
            return gsonUtil.toJson(new JsonOutputVo(Status.클립_메인_주제별TOP3_조회_실패));
        }
        String result = "";
        HashMap clipMainSubjectTop3List = new HashMap();
        ClipMainSubjectTop3Vo clipMainSubjectTop3Vo = new ClipMainSubjectTop3Vo();
        for (int i = 0; i < codeVoList.size(); i++){
            if(DalbitUtil.isEmpty(request.getParameter("subjectType")) || request.getParameter("subjectType").equals("00")){
                clipMainSubjectTop3Vo.setSubjectType(codeVoList.get(i).getValue());
            }else{
                clipMainSubjectTop3Vo.setSubjectType(request.getParameter("subjectType"));
            }

            P_ClipMainSubjectTop3ListVo apiData = new P_ClipMainSubjectTop3ListVo(clipMainSubjectTop3Vo, request);
            ProcedureVo procedureVo = new ProcedureVo(apiData);
            List<P_ClipMainSubjectTop3ListVo> clipMainSubjectTop3ListVo = clipDao.callClipMainSubjectTop3List(procedureVo);
            List<ClipMainSubjectTop3ListOutVo> outVoList = new ArrayList<>();
            if(Status.클립_메인_인기리스트_조회_회원아님.getMessageCode().equals(procedureVo.getRet())) {
                return gsonUtil.toJson(new JsonOutputVo(Status.클립_메인_주제별TOP3_조회_회원아님, clipMainSubjectTop3List));
            }else if(Integer.parseInt(procedureVo.getRet()) > 0){
                for (int j=0; j<clipMainSubjectTop3ListVo.size(); j++){
                    outVoList.add(new ClipMainSubjectTop3ListOutVo(clipMainSubjectTop3ListVo.get(j)));
                }

                if(DalbitUtil.isEmpty(request.getParameter("subjectType")) || request.getParameter("subjectType").equals("00")){
                    clipMainSubjectTop3List.put(codeVoList.get(i).getValue(), outVoList);
                }else{
                    clipMainSubjectTop3List.put("list", outVoList);
                }
            }
        }

        if(clipMainSubjectTop3List.isEmpty()){
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_메인_주제별TOP3_조회_없음, clipMainSubjectTop3List));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립_메인_주제별TOP3_조회_성공, clipMainSubjectTop3List));
        }

        return result;
    }


    /**
     * 클립 신고하기
     */
    public String callClipDeclar(P_ClipDeclarVo pClipDeclarVo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipDeclarVo);
        clipDao.callClipDeclar(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.클립신고_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립신고_성공));
        }else if(procedureVo.getRet().equals(Status.클립신고_요청회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립신고_요청회원번호_정상아님));
        }else if(procedureVo.getRet().equals(Status.클립신고_신고회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립신고_신고회원번호_정상아님));
        }else if(procedureVo.getRet().equals(Status.클립신고_이미_신고상태.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립신고_이미_신고상태));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립신고_실패));
        }
        return result;
    }

    public HashMap clipGiftRankTop3PlayCall(P_ClipGiftRankTop3Vo pClipGiftRank3Vo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipGiftRank3Vo);
        List<P_ClipGiftRankTop3Vo> clipGiftRankTop3VoList = clipDao.callClipGiftRankTop3List(procedureVo);

        HashMap clipGiftRankTop3List = new HashMap();
        if(DalbitUtil.isEmpty(clipGiftRankTop3VoList)){
            clipGiftRankTop3List.put("list", new ArrayList<>());
            return clipGiftRankTop3List;
        }
        List<ClipGiftRank3ListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<clipGiftRankTop3VoList.size(); i++){
            outVoList.add(new ClipGiftRank3ListOutVo(clipGiftRankTop3VoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        clipGiftRankTop3List.put("list", procedureOutputVo.getOutputBox());

        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            return clipGiftRankTop3List;
        }else{
            return clipGiftRankTop3List;
        }
    }


    /**
     * 클립 공유
     */
    public HashMap clipShare(P_ClipPlayVo apiData, HttpServletRequest request) throws GlobalException{
        HashMap result = new HashMap();
        HashMap clip = clipDao.callClipShare(apiData.getCast_no());
        if(!DalbitUtil.isEmpty(clip)){
            Map<String, Object> firebaseMap = restService.makeClipFirebaseDynamicLink(DalbitUtil.getStringMap(clip, "cast_no"), DalbitUtil.getStringMap(clip, "mem_nick"), new ImageVo(DalbitUtil.getStringMap(clip, "image_background"), DalbitUtil.getProperty("server.photo.url")).getUrl(), DalbitUtil.getStringMap(clip, "title"), request);
            if(!DalbitUtil.isEmpty(firebaseMap) && !DalbitUtil.isEmpty(firebaseMap.get("shortLink"))){
                HashMap data = new HashMap();
                data.put("clipNo", DalbitUtil.getStringMap(clip, "cast_no"));
                data.put("title", DalbitUtil.getStringMap(clip, "title"));
                data.put("shareLink", (String)firebaseMap.get("shortLink"));
                result.put("status", Status.클립공유_성공);
                result.put("data", data);
            }else{
                result.put("status", Status.클립공유_동적링크실패);
            }
        }else{
            result.put("status", Status.클립공유_데이터없음);
        }
        return result;
    }

    /**
     * 클립 플레이 정보
     */
    public HashMap clipPlayInfo(P_ClipPlayVo pClipPlayVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pClipPlayVo);
        clipDao.callClipPlay(procedureVo);

        HashMap returnMap = new HashMap();
        returnMap.put("subjectName", "");
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        returnMap.put("clipNo", DalbitUtil.getStringMap(resultMap, "cast_no"));
        returnMap.put("clipMemNo", DalbitUtil.getStringMap(resultMap, "cast_mem_no"));
        returnMap.put("subjectType", DalbitUtil.getStringMap(resultMap, "subject_type"));
        List<CodeVo> codeVoList = commonService.selectClipTypeCodeList(new CodeVo(Code.클립주제.getCode()));
        for (int i=0; i < codeVoList.size(); i++){
            if(DalbitUtil.getStringMap(resultMap, "subject_type").equals(codeVoList.get(i).getValue())){
                returnMap.put("subjectName", codeVoList.get(i).getCdNm());
            }
        }
        returnMap.put("title", DalbitUtil.getStringMap(resultMap, "title"));
        returnMap.put("bgImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "image_background"), DalbitUtil.getProperty("server.photo.url")));
        returnMap.put("fileName", DalbitUtil.getStringMap(resultMap, "file_name"));
        returnMap.put("file", new ClipFileVo(DalbitUtil.getStringMap(resultMap, "file_path"), DalbitUtil.getProperty("server.photo.url")));
        returnMap.put("filePlay", DalbitUtil.getStringMap(resultMap, "file_play"));
        returnMap.put("codeLink", DalbitUtil.getStringMap(resultMap, "code_link"));
        returnMap.put("nickName", DalbitUtil.getStringMap(resultMap, "mem_nick"));
        returnMap.put("isGood", DalbitUtil.getIntMap(resultMap, "good") == 1);
        returnMap.put("playCnt", DalbitUtil.getIntMap(resultMap, "playCnt"));
        returnMap.put("goodCnt", DalbitUtil.getIntMap(resultMap, "goodCnt"));
        returnMap.put("byeolCnt", DalbitUtil.getIntMap(resultMap, "giftCnt"));
        returnMap.put("entryType", DalbitUtil.getIntMap(resultMap, "entryType"));
        returnMap.put("openType", DalbitUtil.getIntMap(resultMap, "openType"));
        returnMap.put("isFan", (DalbitUtil.isLogin(request) && DalbitUtil.getIntMap(resultMap, "enableFan") == 0));
        returnMap.put("replyCnt", DalbitUtil.getIntMap(resultMap, "boardCnt"));
        returnMap.put("regCnt", DalbitUtil.getIntMap(resultMap, "regCnt"));
        returnMap.put("isSpecial", DalbitUtil.getIntMap(resultMap, "specialBadge") > 0);
        returnMap.put("badgeSpecial", DalbitUtil.getIntMap(resultMap, "specialBadge"));
        returnMap.put("level", DalbitUtil.getIntMap(resultMap, "level"));
        returnMap.put("grade", DalbitUtil.getStringMap(resultMap, "grade"));
        returnMap.put("gender", DalbitUtil.getStringMap(resultMap, "memSex"));
        returnMap.put("profImg", new ImageVo(DalbitUtil.getStringMap(resultMap, "profileImage"), DalbitUtil.getStringMap(resultMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
        returnMap.put("holder", StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", DalbitUtil.getIntMap(resultMap, "level") + ""));
        returnMap.put("playIdx", DalbitUtil.getIntMap(resultMap, "playIdx"));

        DeviceVo deviceVo = new DeviceVo(request);
        boolean playlistOpen = false;
        if(
            !"real".equals(DalbitUtil.getActiveProfile())
            ||
            (
                "real".equals(DalbitUtil.getActiveProfile())
                && (
                    (deviceVo.getOs() == 1 && Integer.parseInt(deviceVo.getAppBuild()) > 38)
                    ||
                    (deviceVo.getOs() == 2 && DalbitUtil.versionCompare(deviceVo.getAppVersion(), "1.3.2"))
                    ||
                    (deviceVo.getOs() == 3)
                )

            )
        ){
            playlistOpen = true;
        }
        returnMap.put("playlistOpen", playlistOpen);
        returnMap.put("replyOpen", true);
        returnMap.put("playCntOpen", DalbitUtil.getStringMap(resultMap, "cast_mem_no").equals(pClipPlayVo.getMem_no()));
        returnMap.put("goodCntOpen", true);
        returnMap.put("byeolCntOpen", true);
        returnMap.put("replyCntOpen", true);

        //ios 심사중이면 false;
        boolean eventOpen = false; //2020.12.23 김자운 주임 요청
        //DeviceVo deviceVo = new DeviceVo(request);
        if(deviceVo.getOs() == 2) {
            var iosCodeVo = commonService.selectCodeDefine(new CodeVo(Code.IOS심사중여부.getCode(), Code.IOS심사중여부.getDesc()));
            if (!DalbitUtil.isEmpty(iosCodeVo)) {
                if("Y".equals(iosCodeVo.getValue())) {
                    eventOpen = false;
                }
            }
        }
        returnMap.put("eventOpen", eventOpen);
        returnMap.put("eventUrl", DalbitUtil.getProperty("server.mobile.url") + "/event/clip_gift_event?webview=new");
        returnMap.put("coverTitle", DalbitUtil.getStringMap(resultMap, "coverTitle"));
        returnMap.put("coverSinger", DalbitUtil.getStringMap(resultMap, "coverSinger"));

        ClipGiftRankTop3Vo clipGiftRankTop3Vo = new ClipGiftRankTop3Vo();
        clipGiftRankTop3Vo.setClipNo(pClipPlayVo.getCast_no());
        P_ClipGiftRankTop3Vo pClipGiftRankTop3Vo = new P_ClipGiftRankTop3Vo(clipGiftRankTop3Vo);
        returnMap.put("list", clipGiftRankTop3PlayCall(pClipGiftRankTop3Vo).get("list"));

        return returnMap;
    }


    /**
     * 내 클립 현황
     */
    public String callMyClip(P_MyClipVo pMyClipVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMyClipVo);
        clipDao.callMyClip(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.내클립조회_성공.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("regCnt", DalbitUtil.getIntMap(resultMap, "regCnt"));
            returnMap.put("playCnt", DalbitUtil.getIntMap(resultMap, "playCnt"));
            returnMap.put("goodCnt", DalbitUtil.getIntMap(resultMap, "goodCnt"));
            returnMap.put("byeolCnt", DalbitUtil.getIntMap(resultMap, "byeolCnt"));
            returnMap.put("isReg", DalbitUtil.getIntMap(resultMap, "regYn") == 1);

            result = gsonUtil.toJson(new JsonOutputVo(Status.내클립조회_성공, returnMap));
        }else if(procedureVo.getRet().equals(Status.내클립조회_요청회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.내클립조회_요청회원번호_정상아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.내클립조회_실패));
        }
        return result;
    }


    /**
     * 클립 재생 확인
     */
    public String callPlayConfirm(P_PlayConfirmVo pPlayConfirmVo) {
        ProcedureVo procedureVo = new ProcedureVo(pPlayConfirmVo);
        clipDao.callPlayConfirm(procedureVo);

        String result;
        if(procedureVo.getRet().equals(Status.재생확인_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.재생확인_성공));
        }else if(procedureVo.getRet().equals(Status.재생확인_요청회원번호_정상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.재생확인_요청회원번호_정상아님));
        }else if(procedureVo.getRet().equals(Status.재생확인_클립번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.재생확인_클립번호없음));
        }else if(procedureVo.getRet().equals(Status.재생확인_번호없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.재생확인_번호없음));
        }else if(procedureVo.getRet().equals(Status.재생확인_클립번호_재생확인번호_불일치.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.재생확인_클립번호_재생확인번호_불일치));
        }else if(procedureVo.getRet().equals(Status.재생확인_이미확인.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.재생확인_이미확인));
        }else if(procedureVo.getRet().equals(Status.재생확인_60초미만.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.재생확인_60초미만));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.재생확인_실패));
        }
        return result;
    }

    /**
     * 주간 클립 테이블
     */
    public String callClipRecommendList(P_ClipRecommendListInputVo pClipRecommendListInputVo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipRecommendListInputVo);
        List<P_ClipRecommendListOutputVo> listVo = clipDao.callClipRecommendList(procedureVo);
        ProcedureVo procedureVo1 = new ProcedureVo();
        List<P_ClipRecommendLeaderListVo> leaderListVo = clipDao.callClipRecommendLeaderList(procedureVo1);
        HashMap resultMap = new HashMap();
        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            List<ClipRecommendListOuputVo> outVoList = new ArrayList<>();
            List<ClipRecommendLeaderListVo> leaderList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(listVo)) {
                for(int i=0; i<listVo.size(); i++) {
                    outVoList.add(new ClipRecommendListOuputVo(listVo.get(i)));
                }
            }
            String minDate="";
            if(!DalbitUtil.isEmpty(leaderListVo) && Integer.parseInt(procedureVo1.getRet()) > 0) {
                for(int i=0; i<leaderListVo.size(); i++) {
                    leaderList.add(new ClipRecommendLeaderListVo(leaderListVo.get(i)));
                    minDate = leaderList.get(i).getRecDate();
                }
            }

            P_ClipRecommendInfoVo infoVo = new Gson().fromJson(procedureVo.getExt(), P_ClipRecommendInfoVo.class);
            ClipRecommendInfoVo recommendInfo = new ClipRecommendInfoVo(infoVo);
            //P_ClipRecommendLeaderExtVo minDateVo = new Gson().fromJson(procedureVo1.getExt(), P_ClipRecommendLeaderExtVo.class);
            //String minDate = minDateVo.getMinRecDate().replace(" 00:00:00", "");
            Collections.reverse(leaderList);
            resultMap.put("recommendInfo", recommendInfo);
            resultMap.put("list", outVoList);
            resultMap.put("leaderList", leaderList);
            resultMap.put("minRecDate", minDate);

            result = gsonUtil.toJson(new JsonOutputVo(Status.달대리추천클립조회_성공, resultMap));
        } else if(procedureVo.getRet().equals(Status.달대리추천클립조회_회원아님.getMessageCode())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.달대리추천클립조회_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.달대리추천클립조회_실패));
        }
        return result;
    }


    /**
     * 클립 랭킹 조회
     */
    public String callClipRank(P_ClipRankListVo pClipRankListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pClipRankListVo);
        List<P_ClipRankListVo> clipRankingListVo = clipDao.callClipRank(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap clipRankingInfo = new HashMap();
            List<ClipRankListOutVo> outVoList = new ArrayList<>();

            if(!DalbitUtil.isEmpty(clipRankingListVo)){
                for (int i=0; i<clipRankingListVo.size(); i++){
                    outVoList.add(new ClipRankListOutVo(clipRankingListVo.get(i)));
                }
            }
            ClipRankMyInfoVo clipRankMyInfoVo = new Gson().fromJson(procedureVo.getExt(), ClipRankMyInfoVo.class);

            clipRankingInfo.put("myRank", clipRankMyInfoVo.getMyRank());
            clipRankingInfo.put("myUpDown", clipRankMyInfoVo.getMyUpDown());
            clipRankingInfo.put("myClipPoint", clipRankMyInfoVo.getMyClipPoint());
            clipRankingInfo.put("myGiftPoint", clipRankMyInfoVo.getMyGiftPoint());
            clipRankingInfo.put("myGoodPoint", clipRankMyInfoVo.getMyGoodPoint());
            clipRankingInfo.put("myListenPoint", clipRankMyInfoVo.getMyListenPoint());
            clipRankingInfo.put("subjectType", clipRankMyInfoVo.getSubjectType());
            clipRankingInfo.put("subjectName", clipRankMyInfoVo.getSubjectName());
            clipRankingInfo.put("title", clipRankMyInfoVo.getTitle());
            clipRankingInfo.put("bgImg", new ImageVo(clipRankMyInfoVo.getBackgroundImage(), DalbitUtil.getProperty("server.photo.url")));
            clipRankingInfo.put("myClipNo", clipRankMyInfoVo.getMyClipNo());
            clipRankingInfo.put("myGender", clipRankMyInfoVo.getMyGender());
            clipRankingInfo.put("myProfImg", new ImageVo(clipRankMyInfoVo.getMyProfileImage(), clipRankMyInfoVo.getMyGender(), DalbitUtil.getProperty("server.photo.url")));
            clipRankingInfo.put("list", outVoList);
            clipRankingInfo.put("paging", new PagingVo(Integer.valueOf(procedureVo.getRet()), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

            result = gsonUtil.toJson(new JsonOutputVo(Status.클립랭킹조회_성공, clipRankingInfo));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.클립랭킹조회_실패));
        }
        return result;
    }

    /**
     * 내 클립 현황 (상세) 조회
     */
    public String callMyClipDetail(P_MyClipDetailListVo pMyClipDetailListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMyClipDetailListVo);
        List<P_MyClipDetailListVo> myClipDetailList = clipDao.callMyClipDetail(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap myClipDetail = new HashMap();
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            List<MyClipDetailListOutVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(myClipDetailList)) {
                for(int i=0; i<myClipDetailList.size(); i++) {
                    outVoList.add(new MyClipDetailListOutVo(myClipDetailList.get(i)));
                }
            }
            myClipDetail.put("list", outVoList);
            myClipDetail.put("paging", new PagingVo(Integer.valueOf(procedureVo.getRet()), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));
            result = gsonUtil.toJson(new JsonOutputVo(Status.내클립현황상세조회_성공, myClipDetail));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.내클립현황상세조회_실패));
        }
        return result;
    }

    /**
     * 내 클립 현황 상세 조회 시 공개/비공개 설정
     */
    public String callMyClipDetailEdit(P_MyClipDetailEditVo pMyClipDetailEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMyClipDetailEditVo);
        clipDao.callMyClipDetailEdit(procedureVo);

        String result;
        if(Status.내클립현황_클립공개여부수정_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("openType", DalbitUtil.getIntMap(resultMap, "openType"));
            result = gsonUtil.toJson(new JsonOutputVo(Status.내클립현황_클립공개여부수정_성공, returnMap));
        } else if(Status.내클립현황_클립공개여부수정_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.내클립현황_클립공개여부수정_회원아님));
        } else if(Status.내클립현황_클립공개여부수정_클립없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.내클립현황_클립공개여부수정_클립없음));
        } else if(Status.내클립현황_클립공개여부수정_수정권한없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.내클립현황_클립공개여부수정_수정권한없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.내클립현황_클립공개여부수정_실패));
        }

        return result;
    }

    /**
     * 클립랭킹 어제(지난주) + 오늘(이번주) - native
     */
    public String clipRankingBindList(ClipRankCombineListVo clipRankCombineListVo, HttpServletRequest request) {
        String result = "";

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();

            P_ClipRankListVo apiData;
            ProcedureVo procedureVo;
            List<P_ClipRankListVo> clipRankingList = new ArrayList<>();
            List<P_ClipRankListVo> clipRankingList2 = new ArrayList<>();

            P_ClipRankListVo apiData2;
            ProcedureVo procedureVo2;

            String callType = clipRankCombineListVo.getCallType();
            if(StringUtils.equals(callType, "0")) { // 어제 + 오늘
                // 오늘
                String today = simpleDateFormat.format(now);
                clipRankCombineListVo.setRankingDate(today);
                apiData = new P_ClipRankListVo(clipRankCombineListVo, request);
                procedureVo = new ProcedureVo(apiData);
                clipRankingList = clipDao.callClipRank(procedureVo);

                // 어제
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                cal.add(Calendar.DATE, -1);
                String yesterday = simpleDateFormat.format(cal.getTime());
                clipRankCombineListVo.setRankingDate(yesterday);
                clipRankCombineListVo.setRecords(3);

                apiData2 = new P_ClipRankListVo(clipRankCombineListVo, request);
                procedureVo2 = new ProcedureVo(apiData2);
                clipRankingList2 = clipDao.callClipRank(procedureVo2);

                // 같은 클립이 있으면 삭제
                for(P_ClipRankListVo vo : clipRankingList2) {
                    for(int i=0; i < clipRankingList.size(); i++) {
                        if(StringUtils.equals(vo.getCast_no(), clipRankingList.get(i).getCast_no())) {
                            clipRankingList.remove(i);
                            break;
                        }
                    }
                }

                clipRankingList2.addAll(clipRankingList); // 어제 + 오늘
            }else if(StringUtils.equals(callType, "1")) { // 오늘
                String today = simpleDateFormat.format(now);
                clipRankCombineListVo.setRankingDate(today);
                apiData = new P_ClipRankListVo(clipRankCombineListVo, request);
                procedureVo = new ProcedureVo(apiData);

                clipRankingList = clipDao.callClipRank(procedureVo);
                clipRankingList2.addAll(clipRankingList);
            }else if(StringUtils.equals(callType, "2")) { // 저번주 + 이번주
                // 이번주(월요일 기준)
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                String monday = simpleDateFormat.format(c.getTime());
                clipRankCombineListVo.setRankingDate(monday);
                apiData = new P_ClipRankListVo(clipRankCombineListVo, request);
                procedureVo = new ProcedureVo(apiData);

                clipRankingList = clipDao.callClipRank(procedureVo);

                // 저번주
                c.add(Calendar.DATE, -7);
                String lastWeek = simpleDateFormat.format(c.getTime());
                clipRankCombineListVo.setRankingDate(lastWeek);
                clipRankCombineListVo.setRecords(3);

                apiData2 = new P_ClipRankListVo(clipRankCombineListVo, request);
                procedureVo2 = new ProcedureVo(apiData2);
                clipRankingList2 = clipDao.callClipRank(procedureVo2);

                // 같은 클립이 있으면 삭제
                for(P_ClipRankListVo vo : clipRankingList2) {
                    for(int i=0; i < clipRankingList.size(); i++) {
                        if(StringUtils.equals(vo.getCast_no(), clipRankingList.get(i).getCast_no())) {
                            clipRankingList.remove(i);
                            break;
                        }
                    }
                }

                clipRankingList2.addAll(clipRankingList); // 저번주 + 이번주
            }else { // 이번주
                // 이번주(월요일 기준)
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
                String monday = simpleDateFormat.format(c.getTime());
                clipRankCombineListVo.setRankingDate(monday);
                apiData = new P_ClipRankListVo(clipRankCombineListVo, request);
                procedureVo = new ProcedureVo(apiData);

                clipRankingList = clipDao.callClipRank(procedureVo);
                clipRankingList2.addAll(clipRankingList);
            }

            if(Integer.parseInt(procedureVo.getRet()) > -1) {
                HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
                HashMap clipRankingInfo = new HashMap();
                List<ClipRankListOutVo> outVoList = new ArrayList<>();

                if(!DalbitUtil.isEmpty(clipRankingList2)){
                    for (int i=0; i<clipRankingList2.size(); i++){
                        outVoList.add(new ClipRankListOutVo(clipRankingList2.get(i)));
                    }
                }
                ClipRankMyInfoVo clipRankMyInfoVo = new Gson().fromJson(procedureVo.getExt(), ClipRankMyInfoVo.class);

                clipRankingInfo.put("myRank", clipRankMyInfoVo.getMyRank());
                clipRankingInfo.put("myUpDown", clipRankMyInfoVo.getMyUpDown());
                clipRankingInfo.put("myClipPoint", clipRankMyInfoVo.getMyClipPoint());
                clipRankingInfo.put("myGiftPoint", clipRankMyInfoVo.getMyGiftPoint());
                clipRankingInfo.put("myGoodPoint", clipRankMyInfoVo.getMyGoodPoint());
                clipRankingInfo.put("myListenPoint", clipRankMyInfoVo.getMyListenPoint());
                clipRankingInfo.put("subjectType", clipRankMyInfoVo.getSubjectType());
                clipRankingInfo.put("subjectName", clipRankMyInfoVo.getSubjectName());
                clipRankingInfo.put("title", clipRankMyInfoVo.getTitle());
                clipRankingInfo.put("bgImg", new ImageVo(clipRankMyInfoVo.getBackgroundImage(), DalbitUtil.getProperty("server.photo.url")));
                clipRankingInfo.put("myClipNo", clipRankMyInfoVo.getMyClipNo());
                clipRankingInfo.put("myGender", clipRankMyInfoVo.getMyGender());
                clipRankingInfo.put("myProfImg", new ImageVo(clipRankMyInfoVo.getMyProfileImage(), clipRankMyInfoVo.getMyGender(), DalbitUtil.getProperty("server.photo.url")));
                clipRankingInfo.put("list", outVoList);
                clipRankingInfo.put("paging", new PagingVo(Integer.valueOf(procedureVo.getRet()), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

                result = gsonUtil.toJson(new JsonOutputVo(Status.클립랭킹조회_성공, clipRankingInfo));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.클립랭킹조회_실패));
            }
        } catch (Exception e) {
            log.error("ClipService / clipRankingBindList", e);
            result = gsonUtil.toJson(new JsonOutputVo(Status.비즈니스로직오류));
        }

        return result;
    }

    private String getMonday() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        return simpleDateFormat.format(c.getTime());
    }
}
