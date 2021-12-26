package com.dalbit.event.vo;


public class WelcomItemListVO {
    private String theMonth = ""; //	DATE		-- 경품 일자(월)
    private Integer djGiftNo = 0; //	INT		-- 경품 번호
    private Integer djStepNo = 0; //	INT		-- 단계 번호
    private String djGiftName = ""; //	VARCHAR	-- 경품 이름
    private String djGiftFileName = ""; //	VARCHAR	-- 경품 파일이름
    private Integer djGiftDalCnt = 0; //	INT		-- 달수
    private String djGiftOrd = ""; //	INT		-- 정렬 [1,2,3]
    private Integer totInsCnt = 0; //	INT		-- 응모수
    private String insDate = ""; //		DATETIME	-- 등록일자
    private String updDate = ""; //		DATETIME	-- 수정일자

    public String getTheMonth() {
        return theMonth;
    }

    public Integer getDjGiftNo() {
        return djGiftNo;
    }

    public Integer getDjStepNo() {
        return djStepNo;
    }

    public String getDjGiftName() {
        return djGiftName;
    }

    public String getDjGiftFileName() {
        return djGiftFileName;
    }

    public Integer getDjGiftDalCnt() {
        return djGiftDalCnt;
    }

    public String getDjGiftOrd() {
        return djGiftOrd;
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

    public void setDj_gift_no(Integer dj_gift_no) {
        this.djGiftNo = dj_gift_no;
    }

    public void setDj_step_no(Integer dj_step_no) {
        this.djStepNo = dj_step_no;
    }

    public void setDj_gift_name(String dj_gift_name) {
        this.djGiftName = dj_gift_name;
    }

    public void setDj_gift_file_name(String dj_gift_file_name) {
        this.djGiftFileName = dj_gift_file_name;
    }

    public void setDj_gift_dal_cnt(Integer dj_gift_dal_cnt) {
        this.djGiftDalCnt = dj_gift_dal_cnt;
    }

    public void setDj_gift_ord(String dj_gift_ord) {
        this.djGiftOrd = dj_gift_ord;
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
