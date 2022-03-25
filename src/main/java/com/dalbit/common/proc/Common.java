package com.dalbit.common.proc;

import com.dalbit.common.vo.ParentsAuthSelVo;
import com.dalbit.common.vo.PaySuccSelVo;
import com.dalbit.common.vo.request.ParentCertInputVo;
import com.dalbit.common.vo.request.ParentsEmailLogInsVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface Common {
    /**
     * 회원 본인인증 체크 및 상태해제 (휴면회원)
     * s_return		INT		--  -3:회원 없음, -2:휴면상태가 아님, -1: 본인인증 결과 없음,  0: 에러, 1:정상
     */
    @Select("CALL p_sleep_mem_chk_upd(#{memNo}, #{memPhone})")
    Integer sleepMemChkUpd(String memNo, String memPhone);

    /**********************************************************************************************
    * @Method 설명 : 법정대리인 이메일등록(결제)
    * @작성일   : 2021-12-30
    * @작성자   : 박성민
    * @param  :
    *  memNo: 회원번호(신청자) (BIGINT)
    *  pMemEmail: 대리인 이메일 주소 (VARCHAR(50))
    *  agreementDate: 동의기간 (INT)
    * @return : # -3:미인증, -2:나이 안맞음, -1:이미 동의된 데이터, 0:에러, 1:정상
    **********************************************************************************************/
    @Select("CALL rd_data.p_mem_parents_auth_email_ins(#{memNo}, #{pMemEmail}, #{agreementDate})")
    Integer parentsAuthEmailIns(ParentCertInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 법정대리인 인증등록(결제)
     * @작성일   : 2021-12-30
     * @작성자   : 박성민
     * @param  :
     *  memNo: 회원번호(신청자) (BIGINT)
     *  pMemName: 대리인 이름(VARCHAR(20))
     *  pMemSex: 대리인 성별(CHAR(1))
     *  pMemBirthYear: 대리인 생년(INT)
     *  pMemBirthDay: 대리인 월일(INT)
     *  pMemHphone: 대리인 핸드폰(VARCHAR(15))
     * @return : # -5:부모미성년,-4:미인증, -3:나이 안맞음, -2: 이메일 미등록, -1:이미 동의된 데이터, 0:에러, 1:정상
     **********************************************************************************************/
    @Select("CALL rd_data.p_mem_parents_auth_ins(#{memNo}, #{pMemName}, #{pMemSex}, #{pMemBirthYear}, #{pMemBirthDay}, #{pMemHphone})")
    Integer parentsAuthIns(ParentCertInputVo param);

    /**********************************************************************************************
     * @Method 설명 : 미성년자 법정 대리인 인증 여부 체크 (결제)
     * @작성일   : 2021-12-30
     * @작성자   : 박성민
     * @param  :
     *  memNo: 회원번호(신청자) (BIGINT)
     * @return : s_authYn: 인증 여부(y/n)
     **********************************************************************************************/
    @Select("CALL rd_data.p_mem_parents_auth_chk(#{memNo})")
    String parentsAuthChk(String memNo);

    /**********************************************************************************************
    * @Method 설명 : 법정대리인 인증 정보 (어드민이랑 같음)
    * @작성일   : 2022-01-03
    * @작성자   : 박성민
    * @param  : memNo: 회원번호(신청자)
    * @return :
    *   mem_no                  bigint(20)          -- 회원번호
    *   parents_mem_name        varchar(20)         -- 회원대리인 이름
    *   parents_mem_sex         char(1)             -- 회원대리인 성별
    *   parents_mem_birth_year  smallint(6)         -- 회원대리인 생년
    *   parents_mem_birth_day   smallint(6)         -- 회원대리인 월일
    *   parents_mem_email       varchar(50)         -- 회원대리인 이메일
    *   parents_mem_hphone      varchar(15)         -- 회원대리인 휴대전화
    *   agreement_date          smallint(6)         -- 동의기간
    *   expire_date             datetime(6)         -- 만료일자
    *   auth_yn                 char(1)             -- 동의완료여부
    *   ins_date                datetime(6)         -- 인증일자
    *   upd_date                datetime(6)         -- 수정일자
    **********************************************************************************************/
    @Select("CALL rd_data.p_mem_parents_auth_sel(#{memNo})")
    ParentsAuthSelVo parentsAuthSel(String memNo);
    
    /**********************************************************************************************
    * @Method 설명 : 법정대리인 이메일 발송로그 등록 (어드민이랑 같음)
    * @작성일   : 2022-01-04
    * @작성자   : 박성민
    * @param  :
    *   memNo  회원번호(신청자)
    *   pMemEmail  대리인 이메일주소
    *   mailSlct  메일발송구분[a:동의, p:결제, c:결제취소]
    *   mailEtc  메일발송정보
    * @return : s_return - 0:에러, 1:정상
    **********************************************************************************************/
    @Select("CALL rd_data.p_mem_parents_auth_email_log_ins(#{memNo}, #{pMemEmail}, #{mailSlct}, #{mailEtc})")
    Integer parentsAuthEmailLogIns(ParentsEmailLogInsVo param);

    /**********************************************************************************************
     * @Method 설명 : 미성년자 결제 후 메일 발송을 위한 결제 내용 조회
     * @작성일   : 2022-01-08
     * @작성자   : 박성민
     * @param  :
     *   memNo  회원번호(신청자)
     *   orderId 주문번호
     * @return : (결제 정보)
     *   order_id, mem_no, pay_way, pay_amt, item_amt, pay_code, card_no, card_nm, pay_ok_date, pay_ok_time
     **********************************************************************************************/
    @Select("CALL rd_data.p_mem_pay_succ_sel(#{memNo}, #{orderId})")
    PaySuccSelVo paySuccSel(String memNo, String orderId);
}
