package com.dalbit.event.vo;


public class WelcomItemListVO {
    private String giftCode; //		INT		-- 경품 코드
    private String giftName; //	VARCHAR	-- 경품 이름
    private String giftCont; //		VARCHAR	-- 경품 설명
    private Integer giftDalCnt; //	INT		-- 달수
    private Integer giftCnt; //		INT		-- 지급수
    private Integer giftStepNo; //	INT		-- 단계 번호
    private Integer giftOrd; //		INT		-- 정렬 [1,2,3]
    private Integer giftSlct; //		INT		-- [1:dj, 2:청취자]
    private String useYn; //		CHAR		-- 사용여부
    private Integer totInsCnt; //	INT		-- 응모수
    private String theMonth; //	DATE		-- 경품 일자(월)
    private String insDate; //		DATETIME	-- 등록일자
    private String updDate; //		DATETIME	-- 수정일자

    public String getGiftCode() {
        return giftCode;
    }

    public String getGiftName() {
        return giftName;
    }

    public String getGiftCont() {
        return giftCont;
    }

    public Integer getGiftDalCnt() {
        return giftDalCnt;
    }

    public Integer getGiftCnt() {
        return giftCnt;
    }

    public Integer getGiftStepNo() {
        return giftStepNo;
    }

    public Integer getGiftOrd() {
        return giftOrd;
    }

    public Integer getGiftSlct() {
        return giftSlct;
    }

    public String getUseYn() {
        return useYn;
    }

    public Integer getTotInsCnt() {
        return totInsCnt;
    }

    public String getTheMonth() {
        return theMonth;
    }

    public String getInsDate() {
        return insDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setGift_code(String gift_code) {
        this.giftCode = gift_code;
    }

    public void setGift_name(String gift_name) {
        this.giftName = gift_name;
    }

    public void setGift_cont(String gift_cont) {
        this.giftCont = gift_cont;
    }

    public void setGift_dal_cnt(Integer gift_dal_cnt) {
        this.giftDalCnt = gift_dal_cnt;
    }

    public void setGift_cnt(Integer gift_cnt) {
        this.giftCnt = gift_cnt;
    }

    public void setGift_step_no(Integer gift_step_no) {
        this.giftStepNo = gift_step_no;
    }

    public void setGift_ord(Integer gift_ord) {
        this.giftOrd = gift_ord;
    }

    public void setGift_slct(Integer gift_slct) {
        this.giftSlct = gift_slct;
    }

    public void setUse_yn(String use_yn) {
        this.useYn = use_yn;
    }

    public void setTot_ins_cnt(Integer tot_ins_cnt) {
        this.totInsCnt = tot_ins_cnt;
    }

    public void setThe_month(String the_month) {
        this.theMonth = the_month;
    }

    public void setIns_date(String ins_date) {
        this.insDate = ins_date;
    }

    public void setUpd_date(String upd_date) {
        this.updDate = upd_date;
    }
}
