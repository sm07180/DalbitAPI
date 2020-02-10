package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.BaseVo;
import com.dalbit.member.vo.request.JoinValidationVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Getter
@Setter
public class P_JoinVo extends BaseVo {

    public P_JoinVo(){}

    public P_JoinVo(JoinValidationVo joinValidationVo
        , int os , String deviceUuid , String deviceToken , String appVersion , String adId, String location, String ip){

        setMemSlct(joinValidationVo.getMemType());
        setId(joinValidationVo.getMemId());
        setPw(joinValidationVo.getMemPwd());
        setMemSex(joinValidationVo.getGender());
        setNickName(joinValidationVo.getNickNm());
        setBirth(joinValidationVo.getBirth());
        setTerms1(joinValidationVo.getTerm1());
        setTerms2(joinValidationVo.getTerm2());
        setTerms3(joinValidationVo.getTerm3());
        setTerms4(joinValidationVo.getTerm4());
        setTerms5(joinValidationVo.getTerm5());
        setName(joinValidationVo.getName());
        setProfileImage(joinValidationVo.getProfImg());
        setProfileImageGrade(joinValidationVo.getProfImgRacy());
        setEmail(joinValidationVo.getEmail());
        setOs(os);
        setDeviceUuid(deviceUuid);
        setDeviceToken(deviceToken);
        setAppVersion(appVersion);
        setAdId(adId);
        setLocation(location);
        setIp(ip);
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


    public void setBirth(String birth){

        if(!DalbitUtil.isEmpty(birth))
            try {
                this.birth = birth;
                this.birthYear = LocalDate.parse(birth, DateTimeFormatter.BASIC_ISO_DATE).getYear();
                this.birthMonth = LocalDate.parse(birth, DateTimeFormatter.BASIC_ISO_DATE).getMonthValue();
                this.birthDay = LocalDate.parse(birth, DateTimeFormatter.BASIC_ISO_DATE).getDayOfMonth();
            } catch (Exception e){
                this.birth = "";
            }
        }
    }

