package com.dalbit.event.vo;

public class WelcomListVO {
    private Integer stepNo = 0; //		INT		-- 단계 번호
    private String memNo = ""; //		INT		-- 회원 번호
    private Integer memTime = 0; //	INT		-- 시간(초)[방송,청취]
    private Integer likeCnt = 0; //		INT		-- 좋아요[받은,보낸]
    private Integer dalCnt = 0; //		INT		-- 달[선물,결재]
    private String giftReqYn = "n"; //		CHAR		-- 경품신청 완료
    private String giftRcvYn = "n"; //		CHAR		-- 경품받기 완료
    private String giftTheMonth = ""; //	DATE		-- 경품 일자(월)
    private String giftCode = ""; //		VARCHAR	-- 경품 코드
    private String giftName = ""; //		VARCHAR	-- 경품 이름
    private Integer giftOrd = 0; //		INT	-- 경품 순번
    private String insDate = ""; //		DATETIME	-- 등록일자
    private String updDate = ""; //		DATETIME	-- 수정일자

    public Integer getMemTime() {
        return memTime;
    }

    public Integer getLikeCnt() {
        return likeCnt;
    }

    public Integer getDalCnt() {
        return dalCnt;
    }

    public String getGiftReqYn() {
        return giftReqYn;
    }

    public String getGiftRcvYn() {
        return giftRcvYn;
    }

    public String getGiftTheMonth() {
        return giftTheMonth;
    }

    public String getGiftCode() {
        return giftCode;
    }

    public String getGiftName() {
        return giftName;
    }

    public Integer getGiftOrd() { return giftOrd; }

    public void setMem_time(Integer mem_time) {
        this.memTime = mem_time;
    }

    public void setLike_cnt(Integer like_cnt) {
        this.likeCnt = like_cnt;
    }

    public void setDal_cnt(Integer dal_cnt) {
        this.dalCnt = dal_cnt;
    }

    public void setGift_req_yn(String gift_req_yn) {
        this.giftReqYn = gift_req_yn;
    }

    public void setGift_rcv_yn(String gift_rcv_yn) {
        this.giftRcvYn = gift_rcv_yn;
    }

    public void setGift_the_month(String gift_the_month) {
        this.giftTheMonth = gift_the_month;
    }

    public void setGift_code(String gift_code) {
        this.giftCode = gift_code;
    }

    public void setGift_name(String gift_name) {
        this.giftName = gift_name;
    }

    public void setGift_ord(Integer gift_ord) { this.giftOrd = gift_ord; }

    public Integer getStepNo() {
        return stepNo;
    }

    public String getMemNo() {
        return memNo;
    }

    public String getInsDate() {
        return insDate;
    }

    public String getUpdDate() {
        return updDate;
    }

    public void setStep_no(Integer step_no) {
        this.stepNo = step_no;
    }

    public void setMem_no(String mem_no) {
        this.memNo = mem_no;
    }

    public void setIns_date(String ins_date) {
        this.insDate = ins_date;
    }

    public void setUpd_date(String upd_date) {
        this.updDate = upd_date;
    }
}
