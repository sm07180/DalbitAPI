package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum CustomerStatus implements Status {

    //공지사항 읽음 표시
    공지사항_읽음확인_성공("C001", "customer.center.notice.read.success", "공지사항 읽음 확인 성공 시"),
    공지사항_읽음확인_실패("0", "customer.center.notice.read.fail", "공지사항 읽음 확인 실패 시"),
    공지사항_읽음확인_이미읽음("-1", "customer.center.notice.read.error", "공지사항 읽음 확인 이미 읽음 시"),
    //고객센터 공지사항 목록 조회
    고객센터_공지사항조회_성공("C001", "customer.center.notice.select.success", "고객센터 공지사항 조회 성공 시"),
    고객센터_공지사항조회_없음("0", "customer.center.notice.no.success", "고객센터 공지사항 목록 없을 시"),
    고객센터_공지사항조회_실패("C006", "customer.center.notice.select.fail", "고객센터 공지사항 조회 실패 시"),
    //고객센터 공지사항 내용 조회
    고객센터_공지사항내용조회_성공("0", "customer.center.notice.detail.select.success", "고객센터 공지사항 내용조회 성공 시"),
    고객센터_공지사항내용조회_없음("-1", "customer.center.notice.detail.no", "고객센터 공지사항 내용 없을 시"),
    고객센터_공지사항내용조회_실패("C006", "customer.center.notice.detail.select.fail", "고객센터 공지사항 내용조회 실패 시"),
    //고객센터 FAQ 목록 조회
    고객센터_FAQ조회_성공("C001", "customer.center.faq.select.success", "고객센터 FAQ 조회 성공 시"),
    고객센터_FAQ조회_없음("0", "customer.center.faq.no.success", "고객센터 FAQ 목록 없을 시"),
    고객센터_FAQ조회_실패("C006", "customer.center.faq.select.fail", "고객센터 FAQ 조회 실패 시"),
    //고객센터 FAQ 내용 조회
    고객센터_FAQ내용조회_성공("0", "customer.center.faq.detail.select.success", "고객센터 FAQ 내용조회 성공 시"),
    고객센터_FAQ내용조회_없음("-1", "customer.center.faq.detail.no", "고객센터 FAQ 내용 없을 시"),
    고객센터_FAQ내용조회_실패("C006", "customer.center.faq.detail.select.fail", "고객센터 FAQ 내용조회 실패 시"),
    //고객센터 1:1문의작성 등록
    고객센터_문의작성_성공("0", "customer.center.qna.add.success", "고객센터 1:1문의하기 작성 성공 시"),
    고객센터_문의작성_요청회원번호_회원아님("-1", "customer.center.qna.add.member.number.error", "요청회원번호가 회원 아닐 시"),
    고객센터_문의작성_재문의불가("-2", "customer.center.qna.re.add.error", "재문의 불가 시"),
    고객센터_문의작성_실패("C006", "customer.center.qna.add.fail", "고객센터 1:1문의하기 작성 실패 시"),
    //고객센터 문의내역 조회
    고객센터_문의내역조회_성공("C001", "customer.center.qna.select.success", "고객센터 문의내역 조회 성공 시"),
    고객센터_문의내역_없음("0", "customer.center.qna.no.success", "고객센터 문의내역 없을 시"),
    고객센터_문의내역조회_실패("C006", "customer.center.qna.select.fail", "고객센터 문의내역 조회 실패 시"),
    //1:1 문의 상세 조회
    고객센터_문의상세조회_성공("0", "select.success", "1:1문의 상세조회 성공 시"),
    고객센터_문의상세조회_문의번호없음("-1", "no.data", "1:1문의 상세조회에서 문의 번호가 없을 시"),
    고객센터_문의상세조회_에러("C006", "server.error", "1:1문의 상세조회에서 에러 발생 시"),
    //고객센터 1:1문의삭제
    고객센터_문의삭제_성공("0", "customer.center.qna.delete.success", "고객센터 1:1문의하기 삭제 성공 시"),
    고객센터_문의삭제_요청회원번호_회원아님("-1", "customer.center.qna.delete.member.number.error", "요청회원번호가 회원 아닐 시"),
    고객센터_문의삭제_문의번호없음("-2", "customer.center.qna.delete.number.error", "문의번호가 없을 시"),
    고객센터_문의삭제_실패("C006", "customer.center.qna.delete.fail", "고객센터 1:1문의하기 삭제 실패 시"),
    //1:1문의 처리하기
    일대일문의처리_성공("0", "customer.center.qna.operate.success", "1:1문의 처리 성공 시"),
    일대일문의처리_문의번호없음("-1", "customer.center.qna.operate.no.number", "1:1문의 처리에서 문의 번호가 없을 시"),
    일대일문의처리_이미처리됐음("-2", "customer.center.qna.operate.aleady.operate", "1:1문의 처리에서 이미 처리된 문의일 시"),
    일대일문의처리_에러("C006", "customer.center.qna.operate.server.error", "1:1문의 처리에서 에러 발생 시"),
    일대일문의처리_이미_진행중("3", "customer.center.qna.operate.error", "1:1문의 처리에서 에러 발생 (처리 진행중)"),
    //1:1문의 수정하기
    일대일문의수정_성공("0", "customer.center.qna.update.success", "1:1문의 수정 성공 시"),
    일대일문의수정_문의번호없음("-1", "customer.center.qna.update.no.number", "1:1문의 수정에서 문의 번호가 없을 시"),
    일대일문의수정_이미처리됐음("-2", "customer.center.qna.update.aleady.operate", "1:1문의 수정에서 이미 처리된 문의일 시"),
    일대일문의수정_에러("C006", "customer.center.qna.update.server.error", "1:1문의 수정에서 에러 발생 시");


    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    CustomerStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
