package com.dalbit.member.vo;

import com.dalbit.common.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_JoinVo extends BaseVo {

    public P_JoinVo(){}

    public P_JoinVo(
        String memSlct , String id , String pw , String memSex , String nickName , int birthYear , int birthMonth , int birthDay
        , String term1 , String term2 , String term3 , String name , String profileImage , int profileImageGrade , String email
        , int os , String deviceUuid , String deviceToken , String appVersion , String adId){

        setMemSlct(memSlct);
        setId(id);
        setPw(pw);
        setMemSex(memSex);
        setNickName(nickName);
        setBirthYear(birthYear);
        setBirthMonth(birthMonth);
        setBirthDay(birthDay);
        setTerm1(term1);
        setTerm2(term2);
        setTerm3(term3);
        setName(name);
        setProfileImage(profileImage);
        setProfileImageGrade(profileImageGrade);
        setEmail(email);
        setOs(os);
        setDeviceUuid(deviceUuid);
        setDeviceToken(deviceToken);
        setAppVersion(appVersion);
        setAdId(adId);
    }

    private String memSlct;
    private String id;
    private String pw;
    private String memSex;
    private String nickName;
    private int birthYear;
    private int birthMonth;
    private int birthDay;
    private String term1;
    private String term2;
    private String term3;
    private String name;
    private String profileImage;
    private int profileImageGrade;
    private String email;
    private int os;
    private String deviceUuid;
    private String deviceToken;
    private String appVersion;
    private String adId;
}
