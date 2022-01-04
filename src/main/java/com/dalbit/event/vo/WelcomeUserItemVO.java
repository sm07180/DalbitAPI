package com.dalbit.event.vo;

public class WelcomeUserItemVO {
    private String theMonth; //	DATE		-- 경품 일자(월)
    private Integer memGiftNo; //	INT		-- 경품 번호
    private Integer memStepNo; //	INT		-- 단계 번호
    private String memGiftName; //	VARCHAR	-- 경품 이름
    private String memGiftFileName; //	VARCHAR	-- 경품 파일이름
    private Integer memGiftDalCnt; //	INT		-- 달수
    private String memGiftOrd; //	INT		-- 정렬 [1,2,3]
    private Integer totInsCnt; //	INT		-- 응모수
    private String insDate; //		DATETIME	-- 등록일자
    private String updDate; //		DATETIME	-- 수정일자

    public String getTheMonth() {
        return theMonth;
    }

    public Integer getMemGiftNo() {
        return memGiftNo;
    }

    public Integer getMemStepNo() {
        return memStepNo;
    }

    public String getMemGiftName() {
        return memGiftName;
    }

    public String getMemGiftFileName() {
        return memGiftFileName;
    }

    public Integer getMemGiftDalCnt() {
        return memGiftDalCnt;
    }

    public String getMemGiftOrd() {
        return memGiftOrd;
    }

    public Integer getTotInsCnt() {
        return totInsCnt;
    }

    public String getInsDate() {
        return insDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setThe_month(String the_month) {
        this.theMonth = the_month;
    }

    public void setMem_gift_no(Integer mem_gift_no) {
        this.memGiftNo = mem_gift_no;
    }

    public void setMem_step_no(Integer mem_step_no) {
        this.memStepNo = mem_step_no;
    }

    public void setMem_gift_name(String mem_gift_name) {
        this.memGiftName = mem_gift_name;
    }

    public void setMem_gift_file_name(String mem_gift_file_name) {
        this.memGiftFileName = mem_gift_file_name;
    }

    public void setMem_gift_dal_cnt(Integer mem_gift_dal_cnt) {
        this.memGiftDalCnt = mem_gift_dal_cnt;
    }

    public void setMem_gift_ord(String mem_gift_ord) {
        this.memGiftOrd = mem_gift_ord;
    }

    public void setTot_ins_cnt(Integer tot_ins_cnt) {
        this.totInsCnt = tot_ins_cnt;
    }

    public void setIns_date(String ins_date) {
        this.insDate = ins_date;
    }

    public void setUpd_date(String upd_date) {
        this.updDate = upd_date;
    }
}
