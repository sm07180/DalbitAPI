package com.dalbit.event.vo;

import java.util.Date;

public class LikeTreeStoryVO {
    private Integer autoNo; //    BIGINT    -- 자동등록 번호
    private Integer storyDecorationNo; // BIGINT    -- 0:기본, 1:1번노출,2:2번 노출, 3:3번노출
    private String storyConts; //  VARCHAR  -- 스토리내용
    private String memNo; //    BIGINT    -- 회원 번호
    private String memId; //    VARCHAR  -- 회원 아이디
    private String memNick; //  VARCHAR  -- 회원 닉네임
    private String memSex; //    CHAR    -- 회원성별
    private String imageProfile; //  VARCHAR  -- 프로필
    private Integer memLevel; //  BIGINT    -- 레벨
    private Integer memState; //  BIGINT    -- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
    private Date insDate; //    DATETIME  -- 등록일자
    private Date updDate; //    DATETIME  -- 수정일자
    private Date decorationDate; //  DATETIME  -- 장식 등록일자

    public Integer getAutoNo() {
        return autoNo;
    }

    public void setAutoNo(Integer auto_no) {
        this.autoNo = auto_no;
    }

    public Integer getStoryDecorationNo() {
        return storyDecorationNo;
    }

    public void setStoryDecorationNo(Integer story_decoration_no) {
        this.storyDecorationNo = story_decoration_no;
    }

    public String getStoryConts() {
        return storyConts;
    }

    public void setStoryConts(String story_conts) {
        this.storyConts = story_conts;
    }

    public String getMemNo() {
        return memNo;
    }

    public void setMemNo(String mem_no) {
        this.memNo = mem_no;
    }

    public String getMemId() {
        return memId;
    }

    public void setMemId(String mem_id) {
        this.memId = mem_id;
    }

    public String getMemNick() {
        return memNick;
    }

    public void setMemNick(String mem_nick) {
        this.memNick = mem_nick;
    }

    public String getMemSex() {
        return memSex;
    }

    public void setMemSex(String mem_sex) {
        this.memSex = mem_sex;
    }

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String image_profile) {
        this.imageProfile = image_profile;
    }

    public Integer getMemLevel() {
        return memLevel;
    }

    public void setMemLevel(Integer mem_level) {
        this.memLevel = mem_level;
    }

    public Integer getMemState() {
        return memState;
    }

    public void setMemState(Integer mem_state) {
        this.memState = mem_state;
    }

    public Date getInsDate() {
        return insDate;
    }

    public void setInsDate(Date ins_date) {
        this.insDate = ins_date;
    }

    public Date getUpdDate() {
        return updDate;
    }

    public void setUpdDate(Date upd_date) {
        this.updDate = upd_date;
    }

    public Date getDecorationDate() {
        return decorationDate;
    }

    public void setDecorationDate(Date decoration_date) {
        this.decorationDate = decoration_date;
    }
}
