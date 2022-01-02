package com.dalbit.event.vo;

import java.util.Date;

public class LikeTreeStoryVO {
    private Integer autoNo = 0; //    BIGINT    -- 자동등록 번호
    private Integer storyDecorationNo = 0; // BIGINT    -- 0:기본, 1:1번노출,2:2번 노출, 3:3번노출
    private String storyConts = ""; //  VARCHAR  -- 스토리내용
    private String memNo = ""; //    BIGINT    -- 회원 번호
    private String memId = ""; //    VARCHAR  -- 회원 아이디
    private String memNick = ""; //  VARCHAR  -- 회원 닉네임
    private String memSex = ""; //    CHAR    -- 회원성별
    private String imageProfile = ""; //  VARCHAR  -- 프로필
    private Integer memLevel = 0; //  BIGINT    -- 레벨
    private Integer memState = 0; //  BIGINT    -- 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
    private String insDate; //    DATETIME  -- 등록일자
    private String updDate; //    DATETIME  -- 수정일자
    private String decorationDate; //  DATETIME  -- 장식 등록일자

    // 프론트에서 comment 컴포넌트 쓰게 하기 위한 데이터처리(underscope 킹받네 ㅡㅡ)
    private Integer tail_no = 0;
    private String image_profile = "";
    private String mem_nick = "";
    private String tail_mem_no = "";
    private String ins_date;

    public Integer getTail_no() {
        return tail_no;
    }

    public String getImage_profile() {
        return image_profile;
    }

    public String getImageProfile() {
        return image_profile;
    }

    public void setImage_Profile(String image_profile) {
        this.image_profile = image_profile;
        this.imageProfile = image_profile;
    }

    public String getMem_nick() {
        return mem_nick;
    }

    public String getTail_mem_no() {
        return tail_mem_no;
    }

    public String getIns_date() {
        return ins_date;
    }

    public String getTail_conts() {
        return tail_conts;
    }

    private String tail_conts = "";

    public Integer getAutoNo() {
        return autoNo;
    }

    public void setAuto_No(Integer auto_no) {
        this.tail_no = auto_no;
        this.autoNo = auto_no;
    }

    public Integer getStoryDecorationNo() {
        return storyDecorationNo;
    }

    public void setStory_Decoration_No(Integer story_decoration_no) {
        this.storyDecorationNo = story_decoration_no;
    }

    public String getStoryConts() {
        return storyConts;
    }

    public void setStory_Conts(String story_conts) {
        this.tail_conts = story_conts;
        this.storyConts = story_conts;
    }

    public String getMemNo() {
        return memNo;
    }

    public void setMem_No(String mem_no) {
        this.tail_mem_no = mem_no;
        this.memNo = mem_no;
    }

    public String getMemId() {
        return memId;
    }

    public void setMem_Id(String mem_id) {
        this.memId = mem_id;
    }

    public String getMemNick() {
        return memNick;
    }

    public void setMem_Nick(String mem_nick) {
        this.mem_nick = mem_nick;
        this.memNick = mem_nick;
    }

    public String getMemSex() {
        return memSex;
    }

    public void setMem_Sex(String mem_sex) {
        this.memSex = mem_sex;
    }


    public Integer getMemLevel() {
        return memLevel;
    }

    public void setMem_Level(Integer mem_level) {
        this.memLevel = mem_level;
    }

    public Integer getMemState() {
        return memState;
    }

    public void setMem_State(Integer mem_state) {
        this.memState = mem_state;
    }

    public String getInsDate() {
        return insDate;
    }

    public void setIns_Date(String ins_date) {
        this.ins_date = ins_date;
        this.insDate = ins_date;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setUpd_Date(String upd_date) {
        this.updDate = upd_date;
    }

    public String getDecorationDate() {
        return decorationDate;
    }

    public void setDecoration_Date(String decoration_date) {
        this.decorationDate = decoration_date;
    }
}
