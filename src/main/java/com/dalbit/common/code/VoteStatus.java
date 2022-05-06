package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum VoteStatus implements Status {
    //투표
    투표_생성("C001", "vote.code.insert.success", "투표_생성"),
    투표_삭제("C002", "vote.code.delete.success", "투표_삭제"),
    투표_투표처리("C003", "vote.code.member.insert.success", "투표_투표처리"),
    투표_리스트조회("C004", "vote.code.vote.list.success", "투표_리스트조회"),
    투표_정보조회("C005", "vote.code.vote.sel.success", "투표_정보조회"),
    투표_항목조회("C006", "vote.code.vote.detail.list.success", "투표_항목조회"),
    투표_단일_마감("C007", "vote.code.end.one.success", "투표_단일_마감"),
    투표_전체_마감("C008", "vote.code.end.all.success", "투표_전체_마감"),
    투표_항목과리스트_조회("C009", "vote.code.vote.sel.detail.list.success", "투표_항목과리스트_조회"),
    투표_생성_파라미터("EP001", "vote.code.insert.parameter", "투표_생성_파리미터"),
    투표_삭제_파라미터("EP002", "vote.code.delete.parameter", "투표_삭제_파리미터"),
    투표_투표처리_파라미터("EP003", "vote.code.member.insert.parameter", "투표_투표처리_파리미터"),
    투표_리스트조회_파라미터("EP004", "vote.code.vote.list.parameter", "투표_리스트조회_파리미터"),
    투표_정보조회_파라미터("EP005", "vote.code.vote.sel.parameter", "투표_정보조회_파리미터"),
    투표_항목조회_파라미터("EP006", "vote.code.vote.detail.list.parameter", "투표_항목조회_파리미터"),
    투표_마감_파라미터("EP007", "vote.code.vote.end.list.parameter", "투표_마감_파리미터"),
    투표_항목과리스트_조회_파라미터("EP008", "vote.code.vote.sel.detail.list.parameter", "투표_항목과리스트_조회_파라미터"),
    투표_생성_개수초과_에러("E000", "vote.code.insert.max.fail", "투표_생성_개수초과_에러"),
    투표_생성_에러("E001", "vote.code.insert.fail", "투표_생성_에러"),
    투표_삭제_에러("E002", "vote.code.delete.fail", "투표_삭제_에러"),
    투표_투표처리_에러("E003", "vote.code.member.insert.fail", "투표_투표처리_에러"),
    투표_리스트조회_에러("E004", "vote.code.vote.list.fail", "투표_리스트조회_에러"),
    투표_정보조회_에러("E005", "vote.code.vote.sel.fail", "투표_정보조회_에러"),
    투표_항목조회_에러("E006", "vote.code.vote.detail.list.fail", "투표_항목조회_에러"),
    투표_단일_마감_에러("E007", "vote.code.vote.end.one.list.fail", "투표_단일_마감_에러"),
    투표_전체_마감_에러("E008", "vote.code.vote.end.all.list.fail", "투표_전체_마감_에러"),
    투표_항목과리스트_조회_에러("E009", "vote.code.vote.sel.detail.list.fail", "투표_항목과리스트_조회_에러");

    final private String RESULT_SUCCESS = "success";
    final private String RESULT_FAIL = "fail";

    final private String result;
    final private String messageCode;
    final private String messageKey;
    final private String desc;

    VoteStatus(String messageCode, String messageKey, String desc){
        this.result = messageKey.contains("success") ? RESULT_SUCCESS : RESULT_FAIL;
        this.messageCode = messageCode;
        this.messageKey = messageKey;
        this.desc = desc;
    }
}
