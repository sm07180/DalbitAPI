package com.dalbit.event.vo;

public class WelcomeCondVO {
    private String autoNo; //		BIGINT		-- 자동증가번호
    private String theMonth; //	DATE		-- 사용월
    private String qualifyName; //	VARCHAR	--조건 이름
    private String qualifyGubun; //	VARCHAR	-- 조건구분[1:방송시청,2:좋아요,3:달충전]
    private Integer qualifyVal; //	BIGINT		-- 조건 값
    private Integer qualifyStepNo; //	BIGINT		-- 조건 단계
    private String qualifySlct; //	BIGINT		--  1:dj,2:회원
    private String insDate; //		DATETIME	-- 등록일자
    private String updDate; //		DATETIME	-- 수정일자
    private String chrgrName; //	VARCHAR	-- 관리자이름

    public String getAutoNo() {
        return autoNo;
    }

    public String getTheMonth() {
        return theMonth;
    }

    public String getQualifyName() {
        return qualifyName;
    }

    public String getQualifyGubun() {
        return qualifyGubun;
    }

    public Integer getQualifyVal() {
        return qualifyVal;
    }

    public Integer getQualifyStepNo() {
        return qualifyStepNo;
    }

    public String getQualifySlct() {
        return qualifySlct;
    }

    public String getInsDate() {
        return insDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public String getChrgrName() {
        return chrgrName;
    }

    public void setAuto_no(String auto_no) {
        this.autoNo = auto_no;
    }

    public void setThe_month(String the_month) {
        this.theMonth = the_month;
    }

    public void setQualify_name(String qualify_name) {
        this.qualifyName = qualify_name;
    }

    public void setQualify_gubun(String qualify_gubun) {
        this.qualifyGubun = qualify_gubun;
    }

    public void setQualify_val(Integer qualify_val) {
        this.qualifyVal = qualify_val;
    }

    public void setQualify_step_no(Integer qualify_step_no) {
        this.qualifyStepNo = qualify_step_no;
    }

    public void setQualify_slct(String qualify_slct) {
        this.qualifySlct = qualify_slct;
    }

    public void setIns_date(String ins_date) {
        this.insDate = ins_date;
    }

    public void setUpd_date(String upd_date) {
        this.updDate = upd_date;
    }

    public void setChrgr_name(String chrgr_name) {
        this.chrgrName = chrgr_name;
    }
}
