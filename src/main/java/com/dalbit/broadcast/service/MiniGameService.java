package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.MiniGameDao;
import com.dalbit.broadcast.vo.MiniGameListOutVo;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.MiniGameWinListVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.BanWordVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
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
public class MiniGameService {

    @Autowired
    MiniGameDao miniGameDao;

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    SocketService socketService;

    @Autowired
    CommonService commonService;


    /**
     * 미니게임 리스트 조회
     */
    public String callMiniGameList(P_MiniGameListVo pMiniGameListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMiniGameListVo);
        List<P_MiniGameListVo> miniGameListVo = miniGameDao.callMiniGameList(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap miniGameList = new HashMap();
            List<MiniGameListOutVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(miniGameListVo)){
                for (int i=0; i<miniGameListVo.size(); i++){
                    outVoList.add(new MiniGameListOutVo(miniGameListVo.get(i)));
                }

            }
            miniGameList.put("list", outVoList);
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임_조회_성공, miniGameList));
        } else if (procedureVo.getRet().equals(Status.미니게임_조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임_조회_회원아님));
        } else if (procedureVo.getRet().equals(Status.미니게임_조회_해당방없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임_조회_해당방없음));
        } else if (procedureVo.getRet().equals(Status.미니게임_조회_방이종료됨.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임_조회_방이종료됨));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임_조회_실패));
        }
        return result;
    }


    /**
     * 미니게임 등록
     */
    public String callMiniGameAdd(P_MiniGameAddVo pMiniGameAddVo, HttpServletRequest request) {

        String result="";
        String systemBanWord = commonService.banWordSelect();
        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(pMiniGameAddVo.getMem_no());
        String banWord = commonService.broadcastBanWordSelect(banWordVo);
        //사이트+방송방 금지어 체크(옵션항목)
        if(DalbitUtil.isStringMatchCheck(systemBanWord+"|"+banWord, pMiniGameAddVo.getOptList())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임생성_옵션금지));
            return result;
        }

        ProcedureVo procedureVo = new ProcedureVo(pMiniGameAddVo);
        miniGameDao.callMiniGameAdd(procedureVo);

        if(Status.미니게임등록_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("rouletteNo", DalbitUtil.getIntMap(resultMap, "roulette_no"));
            returnMap.put("gameNo", pMiniGameAddVo.getGame_no());
            returnMap.put("isFree", DalbitUtil.getIntMap(resultMap, "payYn") == 0);
            returnMap.put("payAmt", DalbitUtil.getIntMap(resultMap, "payAmt"));
            returnMap.put("optCnt", DalbitUtil.getIntMap(resultMap, "optCnt"));

            List optList = new ArrayList();
            for(int i=1; i<=DalbitUtil.getIntMap(resultMap, "optCnt"); i++) {
                optList.add(DalbitUtil.getStringMap(resultMap, "opt"+i));
            }

            returnMap.put("optList", optList);
            returnMap.put("versionIdx", DalbitUtil.getIntMap(resultMap, "versionIdx"));

            try{
                socketService.miniGameSend(pMiniGameAddVo.getRoom_no(), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), "add");
            }catch(Exception e){
                log.info("Socket Service MiniGameAdd Exception {}", e);
            }

            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임등록_성공, returnMap));
        }else if(Status.미니게임등록_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임등록_회원아님));
        }else if(Status.미니게임등록_방번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임등록_방번호없음));
        }else if(Status.미니게임등록_종료된방.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임등록_종료된방));
        }else if(Status.미니게임등록_방장아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임등록_방장아님));
        }else if(Status.미니게임등록_이미등록.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임등록_이미등록));
        }else if(Status.미니게임등록_금액설정오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임등록_금액설정오류));
        }else if(Status.미니게임등록_옵션개수오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임등록_옵션개수오류));
        }else if(Status.미니게임등록_옵션리스트오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임등록_옵션리스트오류));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임등록_실패));
        }
        return result;
    }


    /**
     * 미니게임 수정
     */
    public String callMiniGameEdit(P_MiniGameEditVo pMiniGameEditVo, HttpServletRequest request) {

        String result="";
        String systemBanWord = commonService.banWordSelect();
        BanWordVo banWordVo = new BanWordVo();
        banWordVo.setMemNo(pMiniGameEditVo.getMem_no());
        String banWord = commonService.broadcastBanWordSelect(banWordVo);
        //사이트+방송방 금지어 체크(옵션항목)
        if(DalbitUtil.isStringMatchCheck(systemBanWord+"|"+banWord, pMiniGameEditVo.getOptList())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임생성_옵션금지));
            return result;
        }

        ProcedureVo procedureVo = new ProcedureVo(pMiniGameEditVo);
        miniGameDao.callMiniGameEdit(procedureVo);

        if(Status.미니게임수정_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("rouletteNo", DalbitUtil.getIntMap(resultMap, "roulette_no"));
            returnMap.put("isFree", DalbitUtil.getIntMap(resultMap, "payYn") == 0);
            returnMap.put("gameNo", pMiniGameEditVo.getGame_no());
            returnMap.put("payAmt", DalbitUtil.getIntMap(resultMap, "payAmt"));
            returnMap.put("optCnt", DalbitUtil.getIntMap(resultMap, "optCnt"));
            List optionList = new ArrayList();
            for(int i=1; i<=DalbitUtil.getIntMap(resultMap, "optCnt"); i++) {
                optionList.add(DalbitUtil.getStringMap(resultMap, "opt"+i));
            }
            returnMap.put("optList", optionList);
            returnMap.put("versionIdx", DalbitUtil.getIntMap(resultMap, "versionIdx"));
            Status status = Status.미니게임수정_성공;
            if(pMiniGameEditVo.getGame_no() == 1){
                returnMap.put("msg", "DJ가 룰렛 설정을 변경하였습니다.");
                status = Status.미니게임수정_룰렛;
            }else{
                returnMap.put("msg", "DJ가 미니게임 설정을 변경하였습니다.");
            }

            try{
                socketService.miniGameSend(pMiniGameEditVo.getRoom_no(), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), "edit");
            }catch(Exception e){
                log.info("Socket Service MiniGameEdit Exception {}", e);
            }

            result = gsonUtil.toJson(new JsonOutputVo(status, returnMap));
        }else if(Status.미니게임수정_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임수정_회원아님));
        }else if(Status.미니게임수정_방번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임수정_방번호없음));
        }else if(Status.미니게임수정_종료된방.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임수정_종료된방));
        }else if(Status.미니게임수정_방장아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임수정_방장아님));
        }else if(Status.미니게임수정_없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임수정_없음));
        }else if(Status.미니게임수정_금액설정오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임수정_금액설정오류));
        }else if(Status.미니게임수정_옵션개수오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임수정_옵션개수오류));
        }else if(Status.미니게임수정_옵션리스트오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임수정_옵션리스트오류));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임수정_실패));
        }
        return result;
    }


    /**
     * 미니게임 불러오기
     */
    public String callMiniGameSelect(P_MiniGameVo pMiniGameVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMiniGameVo);
        miniGameDao.callMiniGameSelect(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("rouletteNo", DalbitUtil.getIntMap(resultMap, "roulette_no"));
            returnMap.put("gameNo", DalbitUtil.getIntMap(resultMap, "gameNo"));
            returnMap.put("isFree", DalbitUtil.getIntMap(resultMap, "payYn") == 0);
            returnMap.put("payAmt", DalbitUtil.getIntMap(resultMap, "payAmt"));
            returnMap.put("optCnt", DalbitUtil.getIntMap(resultMap, "optCnt"));

            List optList = new ArrayList();
            for(int i=1; i<=DalbitUtil.getIntMap(resultMap, "optCnt"); i++) {
                optList.add(DalbitUtil.getStringMap(resultMap, "opt"+i));
            }

            returnMap.put("optList", optList);
            returnMap.put("versionIdx", DalbitUtil.getIntMap(resultMap, "versionIdx"));

            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_성공, returnMap));
        } else if (procedureVo.getRet().equals(Status.미니게임조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_회원아님));
        } else if (procedureVo.getRet().equals(Status.미니게임조회_해당방없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_해당방없음));
        } else if (procedureVo.getRet().equals(Status.미니게임조회_방이종료됨.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_방이종료됨));
        } else if (procedureVo.getRet().equals(Status.미니게임조회_무료상태.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_무료상태));
        } else if (procedureVo.getRet().equals(Status.미니게임조회_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_실패));
        }
        return result;
    }


    /**
     * 미니게임 시작
     */
    public String callMiniGameStart(P_MiniGameStartVo pMiniGameStartVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pMiniGameStartVo);
        miniGameDao.callMiniGameStart(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("isFree", DalbitUtil.getIntMap(resultMap, "payYn") == 0);
            returnMap.put("rouletteNo", pMiniGameStartVo.getRoulette_no());
            returnMap.put("gameNo", pMiniGameStartVo.getGame_no());
            returnMap.put("payAmt", DalbitUtil.getIntMap(resultMap, "payAmt"));
            returnMap.put("optCnt", DalbitUtil.getIntMap(resultMap, "optCnt"));

            List optList = new ArrayList();
            for(int i=1; i<=DalbitUtil.getIntMap(resultMap, "optCnt"); i++) {
                optList.add(DalbitUtil.getStringMap(resultMap, "opt"+i));
            }

            returnMap.put("optList", optList);
            returnMap.put("versionIdx", DalbitUtil.getIntMap(resultMap, "versionIdx"));
            returnMap.put("winNo", DalbitUtil.getIntMap(resultMap, "winNo"));
            returnMap.put("winOpt", DalbitUtil.getStringMap(resultMap, "winOpt"));
            returnMap.put("nickName", DalbitUtil.getStringMap(resultMap, "nickName"));

            try{
                socketService.miniGameStart(pMiniGameStartVo.getRoom_no(), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
            }catch(Exception e){
                log.info("Socket Service MiniGameStart Exception {}", e);
            }

            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임시작_성공, returnMap));
        } else if (procedureVo.getRet().equals(Status.미니게임시작_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임시작_회원아님));
        } else if (procedureVo.getRet().equals(Status.미니게임시작_해당방없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임시작_해당방없음));
        } else if (procedureVo.getRet().equals(Status.미니게임시작_방이종료됨.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임시작_방이종료됨));
        } else if (procedureVo.getRet().equals(Status.미니게임시작_청취자아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임시작_청취자아님));
        } else if (procedureVo.getRet().equals(Status.미니게임시작_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임시작_없음));
        } else if (procedureVo.getRet().equals(Status.미니게임시작_무료상태.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임시작_무료상태));
        } else if (procedureVo.getRet().equals(Status.미니게임시작_수정버전오류.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임시작_수정버전오류));
        } else if (procedureVo.getRet().equals(Status.미니게임시작_보유달부족.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임시작_보유달부족));
        } else if (procedureVo.getRet().equals(Status.미니게임시작_진행중.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임시작_진행중));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임시작_실패));
        }
        return result;
    }

    /**
     * 미니게임 종료
     */
    public String callMiniGameEnd(P_MiniGameEndVo pMiniGameEndVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pMiniGameEndVo);
        miniGameDao.callMiniGameEnd(procedureVo);

        String result="";
        if(Status.미니게임종료_성공.getMessageCode().equals(procedureVo.getRet())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("isFree", DalbitUtil.getIntMap(resultMap, "payYn") == 0);
            returnMap.put("gameNo", DalbitUtil.getIntMap(resultMap, "gameNo"));

            try{
                socketService.miniGameEnd(pMiniGameEndVo.getRoom_no(), returnMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request));
            }catch(Exception e){
                log.info("Socket Service MiniGameEnd Exception {}", e);
            }

            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임종료_성공, returnMap));
        }else if(Status.미니게임종료_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임종료_회원아님));
        }else if(Status.미니게임종료_방번호없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임종료_방번호없음));
        }else if(Status.미니게임종료_종료된방.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임종료_종료된방));
        }else if(Status.미니게임종료_방장아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임종료_방장아님));
        }else if(Status.미니게임종료_없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임종료_없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임종료_실패));
        }
        return result;
    }



    public HashMap getMiniGame(P_MiniGameVo pMiniGameVo){
        ProcedureVo procedureVo = new ProcedureVo(pMiniGameVo);
        miniGameDao.callMiniGameSelect(procedureVo);

        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap returnMap = new HashMap();
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            returnMap.put("rouletteNo", DalbitUtil.getIntMap(resultMap, "roulette_no"));
            returnMap.put("gameNo", DalbitUtil.getIntMap(resultMap, "gameNo"));
            returnMap.put("isFree", DalbitUtil.getIntMap(resultMap, "payYn") == 0);
            returnMap.put("payAmt", DalbitUtil.getIntMap(resultMap, "payAmt"));
            returnMap.put("optCnt", DalbitUtil.getIntMap(resultMap, "optCnt"));

            List optList = new ArrayList();
            for(int i=1; i<=DalbitUtil.getIntMap(resultMap, "optCnt"); i++) {
                optList.add(DalbitUtil.getStringMap(resultMap, "opt"+i));
            }

            returnMap.put("optList", optList);
            returnMap.put("versionIdx", DalbitUtil.getIntMap(resultMap, "versionIdx"));
            return returnMap;
        }else{
            return null;
        }
    }

    /**
     * 룰렛 당첨내역 리스트
     */
    public String getRouletteWinList(P_MiniGameWinListVo pMiniGameWinListVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMiniGameWinListVo);
        List<P_MiniGameWinListVo> winList = miniGameDao.callRouletteWinList(procedureVo);
        String result="";

        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);

            List<MiniGameWinListVo> outVoList = new ArrayList<>();
            if(!DalbitUtil.isEmpty(winList)){
                for(P_MiniGameWinListVo vo: winList) {
                    vo.setImage_profile(new ImageVo(vo.getImage_profile(), vo.getMem_sex(), DalbitUtil.getProperty("server.photo.url")));
                    outVoList.add(new MiniGameWinListVo(vo));
                }
            }
            resultMap.put("list", outVoList);

            result = gsonUtil.toJson(new JsonOutputVo(Status.룰렛당첨내역_조회_성공, resultMap));
        }else if(Status.룰렛당첨내역_조회_회원아님.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.룰렛당첨내역_조회_회원아님));
        }else if(Status.룰렛당첨내역_조회_해당방없음.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.룰렛당첨내역_조회_해당방없음));
        }else if(Status.룰렛당첨내역_조회_방이종료됨.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.룰렛당첨내역_조회_방이종료됨));
        }else if(Status.룰렛당첨내역_조회_방장다름.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.룰렛당첨내역_조회_방장다름));
        }else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.룰렛당첨내역_조회_실패));
        }
        return result;
    }

    /**
     * 저장된 미니게임 설정 불러오기
     */
    public String callMiniGameSetSelect(P_MiniGameVo pMiniGameVo) {
        ProcedureVo procedureVo = new ProcedureVo(pMiniGameVo);
        miniGameDao.callMiniGameSetSelect(procedureVo);

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > -1) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap returnMap = new HashMap();
            returnMap.put("rouletteNo", DalbitUtil.getIntMap(resultMap, "roulette_no"));
            returnMap.put("gameNo", DalbitUtil.getIntMap(resultMap, "gameNo"));
            returnMap.put("isFree", DalbitUtil.getIntMap(resultMap, "payYn") == 0);
            returnMap.put("payAmt", DalbitUtil.getIntMap(resultMap, "payAmt"));
            returnMap.put("optCnt", DalbitUtil.getIntMap(resultMap, "optCnt"));

            List optList = new ArrayList();
            for(int i=1; i<=DalbitUtil.getIntMap(resultMap, "optCnt"); i++) {
                optList.add(DalbitUtil.getStringMap(resultMap, "opt"+i));
            }

            returnMap.put("optList", optList);
            returnMap.put("versionIdx", DalbitUtil.getIntMap(resultMap, "versionIdx"));

            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_성공, returnMap));
        } else if (procedureVo.getRet().equals(Status.미니게임조회_회원아님.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_회원아님));
        } else if (procedureVo.getRet().equals(Status.미니게임조회_해당방없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_해당방없음));
        } else if (procedureVo.getRet().equals(Status.미니게임조회_방이종료됨.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_방이종료됨));
        } else if (procedureVo.getRet().equals(Status.미니게임조회_무료상태.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_무료상태));
        } else if (procedureVo.getRet().equals(Status.미니게임조회_없음.getMessageCode())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_없음));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.미니게임조회_실패));
        }
        return result;
    }
}
