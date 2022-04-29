package com.dalbit.store.service;

import com.dalbit.admin.dao.AdminDao;
import com.dalbit.admin.vo.SettingListVo;
import com.dalbit.broadcast.dao.UserDao;
import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.StoreStatus;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.main.etc.MainEtc;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_MemberInfoVo;
import com.dalbit.member.vo.procedure.P_ProfileInfoVo;
import com.dalbit.store.dao.StoreDao;
import com.dalbit.store.etc.Store;
import com.dalbit.store.etc.StoreEnum;
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
    @Autowired
    AdminDao adminDao;


    public StoreChargeVo getStoreChargeListByParam(HttpServletRequest request){
        String itemNo = request.getParameter("itemNo");
        PayChargeVo payChargeVo = new PayChargeVo();
        payChargeVo.setItemNo(itemNo);
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
    public String getDalCntJsonStr(HttpServletRequest request){
        Map<String, Object> returnMap = new HashMap<>();
        try{
            returnMap.put("dalCnt", this.getDalCnt(request));
        } catch (Exception e) {
            log.error("StoreService getDalCntJsonStr Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, returnMap));
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
                return gsonUtil.toJson(new JsonOutputVo(StoreStatus.스토어_홈_데이터_조회_회원정보없음, returnMap));
            }
            returnMap.put("memberInfo", memberInfo);
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
            String platform;
            String mode;
            List<Integer> hourRange = Arrays.asList(0,1,2,3,4,5,18,19,20,21,22,23);
//            List<Integer> hourRange = Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23);
            int hourRangeResult = hourRange.stream().filter(f->f.equals(localDateTime.getHour())).findFirst().orElse(-1);

            boolean isWeekEnd =
                    localDateTime.getDayOfWeek().equals(DayOfWeek.SATURDAY) ||
                    localDateTime.getDayOfWeek().equals(DayOfWeek.SUNDAY);


            if(deviceVo.getOs() == 1){
                SettingListVo settingListVo = new SettingListVo();
                settingListVo.setIs_use(1);
                settingListVo.setType("system_config");
                ArrayList<SettingListVo> settingListVos = adminDao.selectSettingList(settingListVo);
                SettingListVo aosSetting = settingListVos.stream().filter(settingListVo1 -> {
                    if(!StringUtils.isEmpty(settingListVo1.getCode())){
                        return settingListVo1.getCode().contains("AOS");
                    }
                    return false;
                }).findFirst().orElse(null);
                if(aosSetting != null){
                    if("Y".equals(aosSetting.getValue())){
                        // 심사중
                        mode = Store.ModeType.IN_APP;
                        platform = Store.Platform.AOS_IN_APP;
                    }else if("N".equals(aosSetting.getValue())){
                        // 심사중이 아니라면
                        mode = Store.ModeType.OTHER;
                        platform = Store.Platform.OTHER;
                    }else{
                        // DB 확인 필요
                        mode = Store.ModeType.OTHER;
                        platform = Store.Platform.OTHER;
                    }
                }else{
                    // DB 확인 필요
                    mode = Store.ModeType.OTHER;
                    platform = Store.Platform.OTHER;
                }
            } else if(deviceVo.getOs() == 2){ // aos 조건과 동일, 주석 참고
                // 기존 소스에 IOS는 스토어 페이지에 접근하지 않지만 방어 코드 용도
                if(DalbitUtil.versionCompare(MainEtc.IN_APP_UPDATE_VERSION.IOS, deviceVo.getAppVersion())){
                    mode = Store.ModeType.IN_APP;
                    platform = Store.Platform.IOS_IN_APP;
                }else if("n".equals(paymentSetting.getIosPaymentSet())){
                    mode = Store.ModeType.IN_APP;
                    platform = Store.Platform.IOS_IN_APP;
                }else if(Store.Screen.iosMemberId.equals(memberInfo.getMemId())){
                    mode = Store.ModeType.IN_APP;
                    platform = Store.Platform.IOS_IN_APP;
                }else if(!Store.NationCodeType.KR.equals(nationCode)) {
                    mode = Store.ModeType.IN_APP;
                    platform = Store.Platform.IOS_IN_APP;
                }else if("y".equals(paymentSetting.getIosPaymentSet())){
//                    if(isWeekEnd){
                    if(localDateTime.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                        mode = Store.ModeType.ALL;
                    }else {
//                        if (hourRangeResult > 0) {
//                            mode = Store.ModeType.ALL;
//                        }else{
//                            mode = Store.ModeType.IN_APP;
//                        }
                        mode = Store.ModeType.IN_APP;
                    }
                    if(Store.ModeType.IN_APP.equals(mode) && paymentSetMemberChk == 1){
                        mode = Store.ModeType.ALL;
                    }
                    platform = Store.Platform.IOS_IN_APP;
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

            /**
             * mode, platform 스토어 페이지 접근 시 탭 초기화랑 연관되어있음
             * mode: inapp | other | all(탭 생성 됨)
             * platform(탭 key) : inapp | other
             */
            returnMap.put("mode", mode);
            returnMap.put("platform", StoreEnum.searchFromPlatform(platform).getMode());

            returnMap.put("deviceInfo", deviceVo);
            returnMap.put("dalCnt", getDalCnt(request));
            returnMap.put("defaultNum", 2); // IOS 스토어 기본값 설정 위한 값(legacy code)

        } catch (Exception e) {
            log.error("StoreService getIndexData Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(StoreStatus.스토어_홈_데이터_조회, returnMap));
    }

    public String selectChargeItem(String platform, HttpServletRequest request) {
        Map<String, Object> returnMap = new HashMap<>();

        try{
            if(!isValidPlatformData(platform,request)){
                return gsonUtil.toJson(new JsonOutputVo(StoreStatus.스토어_아이템_조회_파라미터, returnMap));
            }
            String target = this.platformTarget(platform,request);
            List<StoreChargeVo> dalPriceList = storeDao.selectChargeItem(target);
            returnMap.put("dalPriceList", dalPriceList);
        } catch (Exception e) {
            log.error("StoreService selectChargeItem Error => {}", e.getMessage());
        }
        return gsonUtil.toJson(new JsonOutputVo(StoreStatus.스토어_아이템_조회, returnMap));
    }

    public List<StoreChargeVo> getDalPriceList(String platform, HttpServletRequest request) {
        List<StoreChargeVo> dalPriceList = null;
        try{
            if(!isValidPlatformData(platform,request)){
                return Collections.emptyList();
            }
            String target = this.platformTarget(platform,request);

            dalPriceList = storeDao.selectChargeItem(target);
        } catch (Exception e) {
            log.error("StoreService getDalPriceList Error => {}", e.getMessage());
        }
        return dalPriceList;
    }

    public List<StoreChargeVo> getTargetPriceList(String target) {
        List<StoreChargeVo> dalPriceList = null;
        try{
            dalPriceList = storeDao.selectChargeItem(target);
        } catch (Exception e) {
            log.error("StoreService getTargetPriceList Error => {}", e.getMessage());
        }
        return dalPriceList;
    }

    private String platformTarget(String platform, HttpServletRequest request){
        DeviceVo deviceVo = new DeviceVo(request);

        return platform.equals(Store.ModeType.OTHER) ?
                Store.Platform.OTHER : deviceVo.getOs() == Store.OS.ANDROID ?
                Store.Platform.AOS_IN_APP : Store.Platform.IOS_IN_APP;
    }
    private Boolean isValidPlatformData(String platform, HttpServletRequest request){
        if(StringUtils.isEmpty(platform)){
            return false;
        }
        DeviceVo deviceVo = new DeviceVo(request);

        if(platform.equals(Store.ModeType.IN_APP) && !(deviceVo.getOs() == Store.OS.ANDROID || deviceVo.getOs() == Store.OS.IOS)){
            return false;
        }
        return platform.equals(Store.ModeType.IN_APP) || platform.equals(Store.ModeType.OTHER);
    }
}
