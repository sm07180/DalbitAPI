package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.BaseVo;
import com.dalbit.member.vo.request.SignUpVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class P_JoinVo extends BaseVo {

    public P_JoinVo() {
    }

    public P_JoinVo(SignUpVo signUpVo
            , int os, String deviceUuid, String deviceToken, String appVersion, String adId, String location, String ip, String browser, String nativeTid) {

        setMemSlct(signUpVo.getMemType());
        setId(signUpVo.getMemId());
        setPw(signUpVo.getMemPwd());
        if(DalbitUtil.isEmpty(signUpVo.getGender())){
            setMemSex("n");
        }else{
            setMemSex(signUpVo.getGender());
        }
        setNickName(signUpVo.getNickNm());
        setBirth(signUpVo.getBirth());
        setTerms1(signUpVo.getTerm1());
        setTerms2(signUpVo.getTerm2());
        setTerms3(signUpVo.getTerm3());
        setTerms4(signUpVo.getTerm4());
        setTerms5(signUpVo.getTerm5());
        setName(signUpVo.getName());
        setProfileImage(signUpVo.getProfImg());
        setProfileImageGrade(DalbitUtil.isStringToNumber(signUpVo.getProfImgRacy()));
        setEmail(signUpVo.getEmail());
        setOs(os);
        setDeviceUuid(deviceUuid);
        setDeviceToken(deviceToken);
        setAppVersion(appVersion);
        setAdId(adId);
        setLocation(location);
        setIp(ip);
        setBrowser(browser);
        if (!DalbitUtil.isEmpty(this.name) && this.name.length() > 50) {
            this.name = this.name.substring(0, 49);
        }
        setNativeTid(nativeTid);
    }

    private String memSlct;
    private String id;
    private String pw;
    private String memSex;
    private String nickName;
    private String birth;
    private int birthYear;
    private int birthMonth;
    private int birthDay;
    private String terms1;
    private String terms2;
    private String terms3;
    private String terms4;
    private String terms5;
    private String name;
    private String profileImage;
    private int profileImageGrade;
    private String email;
    private int os;
    private String deviceUuid;
    private String deviceToken;
    private String appVersion;
    private String adId;
    private String location;
    private String ip;
    private String browser;
    private String nativeTid;


    public void setBirth(String birth) {

        if (!DalbitUtil.isEmpty(birth)) {
            try {
                this.birth = birth;
                this.birthYear = LocalDate.parse(birth, DateTimeFormatter.BASIC_ISO_DATE).getYear();
                this.birthMonth = LocalDate.parse(birth, DateTimeFormatter.BASIC_ISO_DATE).getMonthValue();
                this.birthDay = LocalDate.parse(birth, DateTimeFormatter.BASIC_ISO_DATE).getDayOfMonth();
            } catch (Exception e) {
                this.birth = "";
            }
        } else {
            this.birth = LocalDate.now().minusYears(18).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            this.birthYear = LocalDate.parse(this.birth, DateTimeFormatter.BASIC_ISO_DATE).getYear();
            this.birthMonth = LocalDate.parse(this.birth, DateTimeFormatter.BASIC_ISO_DATE).getMonthValue();
            this.birthDay = LocalDate.parse(this.birth, DateTimeFormatter.BASIC_ISO_DATE).getDayOfMonth();
        }
    }
}

