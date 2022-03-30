package com.dalbit.store.service;

import com.dalbit.broadcast.dao.UserDao;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_MemberInfoVo;
import com.dalbit.member.vo.procedure.P_ProfileInfoVo;
import com.dalbit.store.dao.StoreDao;
import com.dalbit.store.etc.Store;
import com.dalbit.store.vo.PayChargeVo;
import com.dalbit.store.vo.StoreChargeVo;
import com.dalbit.store.vo.StoreResultVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class StoreService {
    @Autowired
    private StoreDao storeDao;
    @Autowired
    MypageService mypageService;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    CommonDao commonDao;
    @Autowired
    UserDao userDao;


    public List<StoreChargeVo> getStoreChargeList(HttpServletRequest request){
        DeviceVo deviceVo = new DeviceVo(request);
        String platform = "110";
        if(deviceVo.getOs() == 2){
            platform = "001";
        }
        return storeDao.selectChargeItem(platform);
    }

    public StoreChargeVo getStoreChargeListByParam(HttpServletRequest request){
        String platform = "110";
        String itemNo = request.getParameter("itemNo");
        if("2".equals(request.getParameter("os"))){
            platform = "001";
        }
        PayChargeVo payChargeVo = new PayChargeVo();
        payChargeVo.setItemNo(itemNo);
        payChargeVo.setPlatform(platform);
        return storeDao.selectPayChargeItem(payChargeVo);
    }

    public int getDalCnt(HttpServletRequest request){
        int dalCnt = 0;
        if(DalbitUtil.isLogin(request)){
            P_MemberInfoVo apiData = new P_MemberInfoVo();
            apiData.setMem_no(MemberVo.getMyMemNo(request));
            String result = mypageService.callMemberInfo(apiData);
            HashMap infoView = new Gson().fromJson(result, HashMap.class);
            if(!DalbitUtil.isEmpty(infoView) && !DalbitUtil.isEmpty(infoView.get("result")) && "success".equals(infoView.get("result").toString()) && !DalbitUtil.isEmpty(infoView.get("data"))){
                HashMap infoMap = new Gson().fromJson(new Gson().toJson(infoView.get("data")), HashMap.class);
                if(!DalbitUtil.isEmpty(infoMap) && !DalbitUtil.isEmpty(infoMap.get("dalCnt"))){
                    dalCnt = DalbitUtil.getIntMap(infoMap, "dalCnt");
                }
            }
        }
        return dalCnt;
    }

    private P_ProfileInfoVo getMemberInfo(HttpServletRequest request) {
        String myMemNo = MemberVo.getMyMemNo(request);
        P_ProfileInfoVo pProfileInfo = new P_ProfileInfoVo();
        pProfileInfo.setMem_no(myMemNo);
        pProfileInfo.setTarget_mem_no(myMemNo);

        ProcedureVo procedureVo = new ProcedureVo(pProfileInfo);
        userDao.callMemberInfo(procedureVo);
        return new Gson().fromJson(procedureVo.getExt(), P_ProfileInfoVo.class);
    }



    public String getIndexData(HttpServletRequest request) {
        Map<String, Object> returnMap = new HashMap<>();
        try{
            P_ProfileInfoVo memberInfo = this.getMemberInfo(request);
            if(memberInfo == null || StringUtils.isEmpty(memberInfo.getMemId())){
                return gsonUtil.toJson(new JsonOutputVo(Status.스토어_홈_데이터_조회_회원정보없음, returnMap));
            }
            DeviceVo deviceVo = new DeviceVo(request);

            // 1. 외부결제 설정 정보 조회(어드민 설정 조회)
            StoreResultVo paymentSetting = storeDao.pPaymentSetSel();
            ObjectMapper objectMapper = new ObjectMapper();
            Map paymentSettingMap = objectMapper.convertValue(paymentSetting, Map.class);
            returnMap.putAll(paymentSettingMap);

            // 회원 외부결제 활성화 체크(정책)
            Integer paymentSetMemberChk = storeDao.pPaymentSetMemberChk(memberInfo.getMem_no(), deviceVo.getDeviceUuid());
            returnMap.put("paymentSetMemberChk", paymentSetMemberChk);

            // 아이피 국가코드 조회
            String nationCode = commonDao.getNationCode(request.getRemoteAddr());
            returnMap.put("nationCode", nationCode);

            LocalDateTime localDateTime = LocalDateTime.now();
            String platform = Store.Platform.UNKNOWN;
            String mode = Store.ModeType.IN_APP;
            List<Integer> hourRange = Arrays.asList(0,1,2,3,4,5,18,19,20,21,22,23);
            int hourRangeResult = hourRange.stream().filter(f->f.equals(localDateTime.getHour())).findFirst().orElse(-1);
//            hourRange.stream().filter(f->f.equals(localDateTime.getHour())).findFirst().orElse(-1);
            if(deviceVo.getOs() == 1){
                if("n".equals(paymentSetting.getAosPaymentSet())){// 어드민 외부결제 활성화 체크
                    mode = Store.ModeType.IN_APP;
                    platform = Store.Platform.AOS_IN_APP;
                }else if(Store.Screen.aosMemberId.equals(memberInfo.getMemId())){// 심사 아이디 체크
                    mode = Store.ModeType.IN_APP;
                    platform = Store.Platform.AOS_IN_APP;
                }else if(!Store.NationCodeType.KR.equals(nationCode)){// 해외 아이피 체크
                    mode = Store.ModeType.IN_APP;
                    platform = Store.Platform.AOS_IN_APP;
                }else if("y".equals(paymentSetting.getAosPaymentSet())){
                    if(localDateTime.getDayOfWeek().equals(DayOfWeek.SATURDAY) || localDateTime.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                        // 주말 : 외부결제 노출
                        mode = Store.ModeType.ALL;
                        platform = Store.Platform.AOS_IN_APP;
                    }else if(hourRangeResult > 0){
                        // 평일 18:00 ~ 24:00, 00:00 ~ 06:00 : 외부결제 노출
                        mode = Store.ModeType.ALL;
                        platform = Store.Platform.AOS_IN_APP;
                    }else if(paymentSetMemberChk == 1){
                        // 안드로이드 & IOS 공통 : DAO 주석 참고
                        mode = Store.ModeType.ALL;
                        platform = Store.Platform.AOS_IN_APP;
                    }else{
                        mode = Store.ModeType.IN_APP;
                        platform = Store.Platform.AOS_IN_APP;
                    }
                }else{
                    mode = Store.ModeType.NONE;
                    platform = Store.Platform.UNKNOWN;
                }
            } else if(deviceVo.getOs() == 2){ // aos 조건과 동일, 주석 참고
                if("n".equals(paymentSetting.getIosPaymentSet())){
                    mode = Store.ModeType.IN_APP;
                    platform = Store.Platform.IOS_IN_APP;
                }else if(Store.Screen.iosMemberId.equals(memberInfo.getMemId())){
                    mode = Store.ModeType.IN_APP;
                    platform = Store.Platform.IOS_IN_APP;
                }else if(!Store.NationCodeType.KR.equals(nationCode)) {
                    mode = Store.ModeType.IN_APP;
                    platform = Store.Platform.IOS_IN_APP;
                }else if("y".equals(paymentSetting.getIosPaymentSet())){
                    if(localDateTime.getDayOfWeek().equals(DayOfWeek.SATURDAY) || localDateTime.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                        mode = Store.ModeType.ALL;
                        platform = Store.Platform.IOS_IN_APP;
                    }else if(hourRangeResult > 0){
                        mode = Store.ModeType.ALL;
                        platform = Store.Platform.IOS_IN_APP;
                    }else if(paymentSetMemberChk == 1){
                        mode = Store.ModeType.ALL;
                        platform = Store.Platform.IOS_IN_APP;
                    }else{
                        mode = Store.ModeType.IN_APP;
                        platform = Store.Platform.IOS_IN_APP;
                    }
                }else{
                    mode = Store.ModeType.NONE;
                    platform = Store.Platform.UNKNOWN;
                }
            } else if(deviceVo.getOs() == 3){
                mode = Store.ModeType.OTHER;
                platform = Store.Platform.OTHER;
            } else {
                mode = Store.ModeType.NONE;
                platform = Store.Platform.UNKNOWN;
            }
            returnMap.put("mode", mode);

            List<StoreChargeVo> dalPriceList = storeDao.selectChargeItem(platform);
            returnMap.put("dalCnt", getDalCnt(request));
            returnMap.put("dalPriceList", dalPriceList.isEmpty() ? new ArrayList<>() : dalPriceList);
            returnMap.put("defaultNum", 2); // IOS 스토어 기본값 설정 위한 값(legacy code)

        } catch (Exception e) {
            log.error("StoreService getIndexData Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.스토어_홈_데이터_조회, returnMap));
    }
}
