package com.dalbit.member.service;

import com.dalbit.admin.service.AdminService;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.service.BadgeService;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.ProfileDao;
import com.dalbit.member.vo.*;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.request.SpecialDjHistoryVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.JwtUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Slf4j
@Service
public class ProfileService {

    @Autowired
    ProfileDao profileDao;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    CommonService commonService;
    @Autowired
    SocketService socketService;
    @Autowired
    CommonDao commonDao;
    @Autowired
    MypageService mypageService;
    @Autowired
    MemberService memberService;
    @Autowired
    BadgeService badgeService;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AdminService adminService;
    @Autowired
    RestService restService;

    public ProcedureVo getProfile(P_ProfileInfoVo pProfileInfo){

        ProcedureVo procedureVo = new ProcedureVo(pProfileInfo);
        profileDao.callMemberInfo(procedureVo);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");
        return procedureVo;
    }


    /**
     * 정보 조회
     */
    public String callMemberInfo(P_ProfileInfoVo pProfileInfo, HttpServletRequest request) {
        ProcedureVo procedureVo = getProfile(pProfileInfo);

        String result;
        if(procedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {
            P_ProfileInfoVo profileInfo = new Gson().fromJson(procedureVo.getExt(), P_ProfileInfoVo.class);

            //팬랭킹 1,2,3 조회 프로시저 분리
            P_FanRankVo pFanRankVo = new P_FanRankVo();
            pFanRankVo.setMem_no(pProfileInfo.getTarget_mem_no());
            List fanRankList = memberService.fanRank3(pFanRankVo);

            ProfileInfoOutVo profileInfoOutVo = new ProfileInfoOutVo(profileInfo, pProfileInfo.getTarget_mem_no(), pProfileInfo.getMem_no(), fanRankList, adminService.isAdmin(request));

            if(badgeService.setBadgeInfo(pProfileInfo.getTarget_mem_no(), -1)){
                log.error("NULL ====> callMemberInfo -1 : getTarget_mem_no {}", pProfileInfo.getTarget_mem_no());
            }
            profileInfoOutVo.setLiveBadgeList(badgeService.getCommonBadge());
            profileInfoOutVo.setCommonBadgeList(badgeService.getCommonBadge());
//            profileInfoOutVo.setFanBadgeList(new ArrayList());

            //과거 스페셜DJ 선정 여부
            if(profileInfoOutVo.getIsSpecial()){
                profileInfoOutVo.setWasSpecial(false);
            }else{
                SpecialDjHistoryVo specialDjHistoryVo = new SpecialDjHistoryVo();
                specialDjHistoryVo.setMemNo(pProfileInfo.getTarget_mem_no());
                profileInfoOutVo.setWasSpecial(memberService.getSpecialCnt(specialDjHistoryVo) > 0);
            }

            //HashMap myInfo = socketService.getMyInfo(MemberVo.getMyMemNo(request));
            profileInfoOutVo.setProfMsg(DalbitUtil.replaceMaskString(commonService.banWordSelect(), profileInfoOutVo.getProfMsg()));
            profileInfoOutVo.setBirth(DalbitUtil.getBirth(profileInfo.getBirthYear(), profileInfo.getBirthMonth(), profileInfo.getBirthDay()));
            profileInfoOutVo.setCount(mypageService.getMemberBoardCount(pProfileInfo));


            HashMap imgListMap = callProfImgList(pProfileInfo.getTarget_mem_no(), request);
            profileInfoOutVo.setProfImgList((List) imgListMap.get("list"));

            //stringStringListOperations.put(redisProfileKey, profileInfoOutVo.getMemNo(), gsonUtil.toJson(profileInfoOutVo));
            //stringStringListOperations.put(redisProfileKey, profileInfoOutVo.getMemNo(), gsonUtil.toJson(profileInfoOutVo)); => 추가되지않고 수정 됨

            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_성공, profileInfoOutVo));

        }else if(procedureVo.getRet().equals(Status.회원정보보기_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_회원아님));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_대상아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_대상아님));
        }else if(procedureVo.getRet().equals(Status.회원정보보기_차단회원불가.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_차단회원불가));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원정보보기_실패));
        }
        return result;
    }


    /**
     * 팬보드 댓글 달기
     */
    public String callMemberFanboardAdd(P_FanboardAddVo pFanboardAddVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanboardAddVo);
        profileDao.callMemberFanboardAdd(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1);

        String result;
        if(Status.팬보드_댓글달기성공.getMessageCode().equals(procedureVo.getRet())){

            HashMap socketMap = new HashMap();
            //TODO - 레벨업 유무 소켓추가 추후 확인
            //socketMap.put("isLevelUp", DalbitUtil.getIntMap(resultMap, "levelUp") == 1);
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기성공, returnMap));
        }else if (Status.팬보드_댓글달기실패_스타회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_스타회원번호_회원아님));
        }else if (Status.팬보드_댓글달기실패_작성자회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_작성자회원번호_회원아님));
        }else if (Status.팬보드_댓글달기실패_잘못된댓글그룹번호.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_잘못된댓글그룹번호));
        }else if (Status.팬보드_댓글달기실패_depth값_오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_depth값_오류));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글달기실패_등록오류));
        }

        return result;
    }


    /**
     * 팬보드 목록조회
     */
    public String callMemberFanboardList(P_FanboardListVo pFanboardListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanboardListVo);
        List<P_FanboardListVo> fanboardVoList = profileDao.callMemberFanboardList(procedureVo);

        HashMap fanBoardList = new HashMap();
        //fanBoardList.put("count", mypageService.getMemberBoardCount(pFanboardListVo));
        if(DalbitUtil.isEmpty(fanboardVoList)){
            fanBoardList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글없음, fanBoardList));
        }

        List<BoardVo> outVoList = new ArrayList<>();
        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(pFanboardListVo.getStar_mem_no());
        String systemBanWord = commonService.banWordSelect();
        String banWord = commonService.broadcastBanWordSelect(banWordVo);
        for (int i=0; i<fanboardVoList.size(); i++){
            //사이트+방송방 금지어 조회 댓글 마스킹 처리
            if(!DalbitUtil.isEmpty(banWord)){
                fanboardVoList.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, fanboardVoList.get(i).getContents()));
            }else if(!DalbitUtil.isEmpty(systemBanWord)){
                fanboardVoList.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord, fanboardVoList.get(i).getContents()));
            }
            outVoList.add(new BoardVo(fanboardVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);

        fanBoardList.put("list", procedureOutputVo.getOutputBox());
        fanBoardList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드조회성공, fanBoardList));
        } else if(Status.팬보드_요청회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_요청회원번호_회원아님));
        } else if(Status.팬보드_스타회원번호_회원아님.getMessageCode().equals((procedureVo.getRet()))) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_스타회원번호_회원아님));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_조회오류));
        }

        return result;
    }


    /**
     * 팬보드 삭제하기
     */
    public String callMemberFanboardDelete(P_FanboardDeleteVo p_fanboardDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(p_fanboardDeleteVo);
        profileDao.callMemberFanboardDelete(procedureVo);

        String result;
        if(Status.팬보드_댓글삭제성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status. 팬보드_댓글삭제성공));
        } else if(Status.팬보드_댓글삭제실패_스타회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_스타회원번호_회원아님));
        } else if(Status.팬보드_댓글삭제실패_삭제자회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_삭제자회원번호_회원아님));
        } else if(Status.팬보드_댓글삭제실패_댓글인덱스번호_잘못된번호.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_요청인덱스번호_스타회원번호가다름));
        } else if(Status.팬보드_댓글삭제실패_요청인덱스번호_스타회원번호가다름.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_요청인덱스번호_스타회원번호가다름));
        } else if(Status.팬보드_댓글삭제실패_이미삭제됨.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_이미삭제됨));
        } else if(Status.팬보드_댓글삭제실패_삭제권한없음.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제실패_삭제권한없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글삭제오류));
        }

        return result;
    }


    /**
     * 팬보드 대댓글 조회하기
     */
    public String callMemberFanboardReply(P_FanboardReplyVo pFanboardReplyVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanboardReplyVo);
        List<P_FanboardReplyVo> fanboardVoReplyList = profileDao.callMemberFanboardReply(procedureVo);

        HashMap fanboardReplyList = new HashMap();
        if(DalbitUtil.isEmpty(fanboardVoReplyList)){
            fanboardReplyList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.팬보드_대댓글조회실패_대댓글없음, fanboardReplyList));
        }

        List<BoardVo> outVoList = new ArrayList<>();
        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(pFanboardReplyVo.getStar_mem_no());
        String systemBanWord = commonService.banWordSelect();
        String banWord = commonService.broadcastBanWordSelect(banWordVo);
        for (int i=0; i<fanboardVoReplyList.size(); i++){
            //사이트+방송방 금지어 조회 대댓글 마스킹 처리
            if(!DalbitUtil.isEmpty(banWord)){
                fanboardVoReplyList.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord+"|"+banWord, fanboardVoReplyList.get(i).getContents()));
            }else if(!DalbitUtil.isEmpty(systemBanWord)){
                fanboardVoReplyList.get(i).setContents(DalbitUtil.replaceMaskString(systemBanWord, fanboardVoReplyList.get(i).getContents()));
            }
            outVoList.add(new BoardVo(fanboardVoReplyList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        fanboardReplyList.put("list", procedureOutputVo.getOutputBox());

        log.info("프로시저 응답 코드: {}", procedureOutputVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureOutputVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_대댓글조회성공, fanboardReplyList));
        } else if(Status.팬보드_대댓글조회실패_요청회원번호_회원아님.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson((new JsonOutputVo(Status.팬보드_대댓글조회실패_요청회원번호_회원아님)));
        } else if(Status.팬보드_대댓글조회실패_스타회원번호_회원아님.getMessageCode().equals(procedureOutputVo.getRet())) {
            result = gsonUtil.toJson((new JsonOutputVo(Status.팬보드_대댓글조회실패_스타회원번호_회원아님)));
        } else {
            result = gsonUtil.toJson((new JsonOutputVo(Status.팬보드_대댓글조회오류)));
        }

        return result;
    }


    /**
     * 회원 팬 랭킹 조회
     */
    public String callMemberFanRanking(P_FanRankingVo pFanRankingVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pFanRankingVo);
        List<P_FanRankingVo> fanRankingVoList = profileDao.callMemberFanRanking(procedureVo);

        HashMap fanRankingList = new HashMap();
        if(DalbitUtil.isEmpty(fanRankingVoList)){
            fanRankingList.put("list", new ArrayList<>());
            fanRankingList.put("totalCnt", 0);
            fanRankingList.put("paging", new PagingVo(0, pFanRankingVo.getPageNo(), pFanRankingVo.getPageCnt()));
            DeviceVo deviceVo = new DeviceVo(request);
            if(2 == deviceVo.getOs()){
                HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
                fanRankingList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));
            }
            if(2 == deviceVo.getOs() && !DalbitUtil.versionCompare(deviceVo.getAppVersion(), "1.3.3")){ //IOS crash 대응 20.11.10 이재은
                return gsonUtil.toJson(new JsonOutputVo(Status.팬랭킹조회_실패, fanRankingList));
            }else{
                return gsonUtil.toJson(new JsonOutputVo(Status.팬랭킹조회_팬없음, fanRankingList));
            }
        }

        List<FanRankingOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<fanRankingVoList.size(); i++){
            outVoList.add(new FanRankingOutVo(fanRankingVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        fanRankingList.put("list", procedureOutputVo.getOutputBox());
        fanRankingList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬랭킹조회_성공, fanRankingList));
        } else if (procedureVo.getRet().equals(Status.팬랭킹조회_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬랭킹조회_요청회원_회원아님));
        } else if (procedureVo.getRet().equals(Status.팬랭킹조회_대상회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬랭킹조회_대상회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬랭킹조회_실패));
        }
        return result;
    }


    /**
     *  회원 레벨 업 확인
     */
    public String callMemberLevelUpCheck(P_LevelUpCheckVo pLevelUpCheckVo) {
        ProcedureVo procedureVo = new ProcedureVo(pLevelUpCheckVo);
        profileDao.callMemberLevelUpCheck(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("oldLevel", DalbitUtil.getIntMap(resultMap, "oldLevel"));
        returnMap.put("newLevel", DalbitUtil.getIntMap(resultMap, "newLevel"));
        returnMap.put("oldGrade", DalbitUtil.getStringMap(resultMap, "oldGrade"));
        returnMap.put("newGrade", DalbitUtil.getStringMap(resultMap, "newGrade"));

        String result;
        if(procedureVo.getRet().equals(Status.레벨업확인_성공.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.레벨업확인_성공, returnMap));
        }else if(procedureVo.getRet().equals(Status.레벨업확인_요청회원번호_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.레벨업확인_요청회원번호_회원아님));
        }else if(procedureVo.getRet().equals(Status.레벨업확인_레벨업_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.레벨업확인_레벨업_없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.레벨업확인_실패));
        }
        return result;
    }


    /**
     * 회원 팬보드 댓글수정
     */
    public String callMemberFanboardEdit(P_FanboardEditVo pFanboardEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanboardEditVo);
        profileDao.callMemberFanboardEdit(procedureVo);

        String result;
        if(Status.팬보드_댓글수정성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글수정성공));
        }else if (Status.팬보드_댓글수정실패_스타회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글수정실패_스타회원번호_회원아님));
        }else if (Status.팬보드_댓글수정실패_수정자회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글수정실패_수정자회원번호_회원아님));
        }else if (Status.팬보드_댓글수정실패_잘못된댓글인덱스번호.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글수정실패_잘못된댓글인덱스번호));
        }else if (Status.팬보드_댓글수정실패_댓글인덱스번호_스타회원번호다름.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글수정실패_댓글인덱스번호_스타회원번호다름));
        }else if (Status.팬보드_댓글수정실패_삭제댓글_수정불가.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글수정실패_삭제댓글_수정불가));
        }else if (Status.팬보드_댓글수정실패_수정권한없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글수정실패_수정권한없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬보드_댓글수정실패));
        }

        return result;
    }


    /**
     * 회원 스타 랭킹 조회
     */
    public List<StarRankingVo> selectStarRanking(com.dalbit.member.vo.request.StarRankingVo starRankingVo, HttpServletRequest request){
        if(!DalbitUtil.isLogin(request)){
            return new ArrayList();
        }
        P_StarRankingVo param = new P_StarRankingVo(starRankingVo, MemberVo.getMyMemNo(request));
        List<P_StarRankingVo> list = profileDao.selectStarRanking(param);
        List<StarRankingVo> returnList = new ArrayList<>();
        if(!DalbitUtil.isEmpty(list)){
            for(P_StarRankingVo data : list){
                returnList.add(new StarRankingVo(data, DalbitUtil.getProperty("server.photo.url")));
            }
        }
        return returnList;
    }


    /**
     * 회원 팬 전체 리스트
     */
    public String callFanList(P_FanListVo pFanListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanListVo);
        List<P_FanListVo> fanListVoList = profileDao.callFanList(procedureVo);

        HashMap fanList = new HashMap();
        if(DalbitUtil.isEmpty(fanListVoList)){
            fanList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.팬조회_팬없음, fanList));
        }

        List<FanListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<fanListVoList.size(); i++){
            outVoList.add(new FanListOutVo(fanListVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        fanList.put("list", procedureOutputVo.getOutputBox());
        fanList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬조회_성공, fanList));
        } else if (procedureVo.getRet().equals(Status.팬조회_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬조회_요청회원_회원아님));
        } else if (procedureVo.getRet().equals(Status.팬조회_대상회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬조회_대상회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬조회_실패));
        }
        return result;
    }


    /**
     * NEW 회원 팬 전체 리스트
     */
    public String callFanListNew(P_FanListNewVo pFanListNewVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pFanListNewVo);
        List<P_FanListNewVo> fanListNewVoList = profileDao.callFanListNew(procedureVo);

        HashMap fanList = new HashMap();
        if(DalbitUtil.isEmpty(fanListNewVoList)){
            fanList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.팬조회_팬없음, fanList));
        }

        List<FanListNewOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<fanListNewVoList.size(); i++){
            outVoList.add(new FanListNewOutVo(fanListNewVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        fanList.put("list", procedureOutputVo.getOutputBox());
        fanList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬조회_성공, fanList));
        } else if (procedureVo.getRet().equals(Status.팬조회_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬조회_요청회원_회원아님));
        } else if (procedureVo.getRet().equals(Status.팬조회_대상회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬조회_대상회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬조회_실패));
        }
        return result;
    }


    /**
     * 회원 팬 메모 조회
     */
    public String callFanMemo(P_FanMemoVo pFanMemoVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanMemoVo);
        profileDao.callFanMemo(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("fanMemo", DalbitUtil.getStringMap(resultMap, "fanMemo"));

        String result;
        if(Status.팬메모조회_성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬메모조회_성공, returnMap));
        } else if (procedureVo.getRet().equals(Status.팬메모조회_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬메모조회_요청회원_회원아님));
        } else if (procedureVo.getRet().equals(Status.팬메모조회_대상회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬메모조회_대상회원_회원아님));
        } else if (procedureVo.getRet().equals(Status.팬메모조회_대상회원_팬아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬메모조회_대상회원_팬아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬메모조회_실패));
        }
        return result;
    }


    /**
     * 회원 팬 메모 저장
     */
    public String callFanMemoSave(P_FanMemoSaveVo pFanMemoSaveVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanMemoSaveVo);
        profileDao.callFanMemoSave(procedureVo);

        String result;
        if(Status.팬메모저장_성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬메모저장_성공));
        } else if (procedureVo.getRet().equals(Status.팬메모저장_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬메모저장_요청회원_회원아님));
        } else if (procedureVo.getRet().equals(Status.팬메모저장_대상회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬메모저장_대상회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬메모저장_실패));
        }
        return result;
    }


    /**
     * 회원 팬 리스트 편집
     */
    public String callFanEdit(P_FanEditVo pFanEditVo) {
        ProcedureVo procedureVo = new ProcedureVo(pFanEditVo);
        profileDao.callFanEdit(procedureVo);

        String result;
        if(Status.팬리스트편집_성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬리스트편집_성공));
        } else if (procedureVo.getRet().equals(Status.팬리스트편집_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬리스트편집_요청회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.팬리스트편집_실패));
        }
        return result;
    }


    /**
     * NEW 스타 리스트
     */
    public String callStarListNew(P_StarListNewVo pStarListNewVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pStarListNewVo);
        List<P_StarListNewVo> starListNewVoList = profileDao.callStarListNew(procedureVo);

        HashMap starList = new HashMap();
        if(DalbitUtil.isEmpty(starListNewVoList)){
            starList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.스타리스트조회_없음, starList));
        }

        List<StarListNewOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<starListNewVoList.size(); i++){
            outVoList.add(new StarListNewOutVo(starListNewVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);
        starList.put("list", procedureOutputVo.getOutputBox());
        starList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureOutputVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타리스트조회_성공, starList));
        } else if (procedureVo.getRet().equals(Status.스타리스트조회_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타리스트조회_요청회원_회원아님));
        } else if (procedureVo.getRet().equals(Status.스타리스트조회_대상회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타리스트조회_대상회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타리스트조회_실패));
        }
        return result;
    }


    /**
     * 스타 메모 조회
     */
    public String callStarMemo(P_StarMemoVo pStarMemoVo) {
        ProcedureVo procedureVo = new ProcedureVo(pStarMemoVo);
        profileDao.callStarMemo(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        HashMap returnMap = new HashMap();
        returnMap.put("starMemo", DalbitUtil.getStringMap(resultMap, "starMemo"));

        String result;
        if(Status.스타메모조회_성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타메모조회_성공, returnMap));
        } else if (procedureVo.getRet().equals(Status.스타메모조회_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타메모조회_요청회원_회원아님));
        } else if (procedureVo.getRet().equals(Status.스타메모조회_대상회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타메모조회_대상회원_회원아님));
        } else if (procedureVo.getRet().equals(Status.스타메모조회_대상회원_팬아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타메모조회_대상회원_팬아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타메모조회_실패));
        }
        return result;
    }


    /**
     * 스타 팬 메모 저장
     */
    public String callStarMemoSave(P_StarMemoSaveVo pStarMemoSaveVo) {
        ProcedureVo procedureVo = new ProcedureVo(pStarMemoSaveVo);
        profileDao.callStarMemoSave(procedureVo);

        String result;
        if(Status.스타메모저장_성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타메모저장_성공));
        } else if (procedureVo.getRet().equals(Status.스타메모저장_요청회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타메모저장_요청회원_회원아님));
        } else if (procedureVo.getRet().equals(Status.스타메모저장_대상회원_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타메모저장_대상회원_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.스타메모저장_실패));
        }
        return result;
    }


    /**
     * 프로필 이미지 추가등록
     */
    public String callProfileImgAdd(P_ProfileImgAddVo pProfileImgAddVo, HttpServletRequest request) throws GlobalException {

        String profImg = DalbitUtil.replacePath(pProfileImgAddVo.getProfileImage());
        pProfileImgAddVo.setProfileImage(profImg);
        ProcedureVo procedureVo = new ProcedureVo(pProfileImgAddVo);
        profileDao.callProfileImgAdd(procedureVo);

        String result;
        if(Status.프로필이미지_추가등록_성공.getMessageCode().equals(procedureVo.getRet())){
            restService.imgDone(DalbitUtil.replaceDonePath(profImg), request);
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지_추가등록_성공));
        } else if (procedureVo.getRet().equals(Status.프로필이미지_추가등록_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지_추가등록_회원아님));
        } else if (procedureVo.getRet().equals(Status.프로필이미지_추가등록_이미지없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지_추가등록_이미지없음));
        } else if (procedureVo.getRet().equals(Status.프로필이미지_추가등록_10개초과.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지_추가등록_10개초과));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지_추가등록_실패));
        }
        return result;
    }


    /**
     * 프로필 이미지 삭제
     */
    public String callProfileImgDelete(P_ProfileImgDeleteVo pProfileImgDeleteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pProfileImgDeleteVo);
        profileDao.callProfileImgDelete(procedureVo);

        String result;
        if(Status.프로필이미지_삭제_성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지_삭제_성공));
        } else if (procedureVo.getRet().equals(Status.프로필이미지_삭제_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지_삭제_회원아님));
        } else if (procedureVo.getRet().equals(Status.프로필이미지_삭제_이미지없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지_삭제_이미지없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지_삭제_실패));
        }
        return result;
    }


    /**
     * 프로필 이미지 대표지정
     */
    public String callProfileImgLeader(P_ProfileImgLeaderVo pProfileImgLeaderVo) {
        ProcedureVo procedureVo = new ProcedureVo(pProfileImgLeaderVo);
        profileDao.callProfileImgLeader(procedureVo);

        String result;
        if(Status.프로필이미지_대표지정_성공.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지_대표지정_성공));
        } else if (procedureVo.getRet().equals(Status.프로필이미지_대표지정_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지_대표지정_회원아님));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.프로필이미지_대표지정_실패));
        }
        return result;
    }

    public HashMap callProfImgList(String memNo, HttpServletRequest request){
        P_ProfileImgListVo pProfileImgListVo = new P_ProfileImgListVo(memNo);
        ProcedureVo procedureVo = new ProcedureVo(pProfileImgListVo);
        List<P_ProfileImgListVo> profImgListVo = profileDao.callProfImgList(procedureVo);

        HashMap imgListMap = new HashMap();
        imgListMap.put("list", new ArrayList<>());
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            List<ProfileImgListOutVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(profImgListVo)){
                for (int i=0; i<profImgListVo.size(); i++){
                    outVoList.add(new ProfileImgListOutVo(profImgListVo.get(i), request));
                }
            }
            imgListMap.put("list", outVoList);
        }
        return imgListMap;
    }

    /**
     * 1일 1회 본인인증 가능 여부 (new 프로시져)
     */
    public String callCertificationChkSel(String memNo) {
        return profileDao.callCertificationChkSel(memNo);
    }


    /**
     * 팬보드 상세 조회
     *
     * @Param
     * fanboardNo    INT		-- 팬보드번호
     * ,memNo 		BIGINT		-- 회원번호
     * ,viewMemNo 	BIGINT		-- 회원번호(접속자)
     *
     * @Return
     * board_idx;                  //BIGINT		-- 번호
     * writer_mem_no;              //BIGINT		-- 회원번호(작성자)
     * nickName;                 //VARCHAR	--닉네임(작성자)
     * userId;                   //VARCHAR	--아이디(작성자)
     * memSex;                   //VARCHAR	-- 성별(작성자)
     * profileImage;             //VARCHAR	-- 프로필(작성자)
     * STATUS;                     //BIGINT		-- 상태
     * viewOn;                     //BIGINT		-- 1:공개 0:비공개
     * writeDate;                //DATETIME	-- 수정일자
     * ins_date;                 //DATETIME	-- 등록일자
     * rcv_like_cnt;               //BIGINT		-- 좋아요수
     * rcv_like_cancel_cnt;        //BIGINT		-- 취소 좋아요수
     * like_yn;                  //CHAR		-- 좋아요 확인[y,n]
     * */
    public ProfileBoardDetailOutVo boardDetailSel(ProfileBoardDetailSelVo vo, HttpServletRequest request){
        HashMap map = new HashMap();
        map.put("fanboardNo", vo.getFanBoardNo());   // 글번호
        map.put("memNo", Long.parseLong(vo.getMemNo()));       // 프로필 주인
        map.put("viewMemNo", Long.parseLong(MemberVo.getMyMemNo(request)) );   //로그인한 유저

        ProfileBoardDetailOutVo resultVo = profileDao.pMemberFanboardSel(map);

        if(resultVo != null) {
            resultVo.setProfImg(new ImageVo(resultVo.getProfileImage(), resultVo.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        }

        return resultVo;
    }
}
