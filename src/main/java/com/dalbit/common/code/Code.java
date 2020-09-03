package com.dalbit.common.code;

import lombok.Getter;

@Getter
public enum Code {

    //포토 배경 이미지 PREFIX
    포토_배경_PREFIX("/bg_0", "포토서버 배경 이미지 실제 경로(done) prefix"),
    포토_배경_임시_PREFIX("/bg_1", "포토서버 배경 이미지 임시 경로 prefix "),
    포토_배경_썸네일_PREFIX("/bg_2", "포토서버 배경 이미지 썸네일 prefix"),
    포토_배경_디폴트_PREFIX("/bg_3", "포토서버 배경 이미지 배경 디폴트 prefix"),

    //포토 프로필 이미지 PREFIX
    포토_프로필_PREFIX("/profile_0", "포토서버 프로필 실제 경로 prefix"),
    포토_프로필_임시_PREFIX("/profile_1", "포토서버 프로필 임시 경로 prefix"),
    포토_프로필_썸네일_PREFIX("/profile_2", "포토서버 프로필 이미지 썸네일 prefix"),
    포토_프로필_디폴트_PREFIX("/profile_3", "포토서버 배경 이미지 배경 디폴트 prefix"),

    //포토 1:1문의 이미지 PREFIX
    포토_일대일_PREFIX("/qna_0", "포토서버 1:1문의 실제 경로 prefix"),
    포토_일대일_임시_PREFIX("/qna_1", "포토서버 1:1문의 임시 경로 prefix"),

    //환전신청 이미지 PREFIX
    포토_환전신청_PREFIX("/exchange_0", "포토서버 환전신청 실제 경로 prefix"),
    포토_환전신청_임시_PREFIX("/exchange_1", "포토서버 환전신청 임시 경로 prefix"),

    배경이미지_파일명_PREFIX("roombg_", "배경이미지 파일명"),
    프로필이미지_파일명_PREFIX("profile_", "배경이미지 파일명"),
    일대일문의이미지_파일명_PREFIX("qna_", "1:1문의 파일명"),
    환전신청_파일명_PREFIX("exchange_", "환전신청 파일명"),
    클립배경이미지_파일명_PREFIX("clipbg_", "클립 배경이미지 파일명"),

    포토_이미지_경로("_0/", "실제 파일이 올라갈 경로 -done 이후 경로"),
    포토_이미지_임시경로("_1/", "이미지가 올라가는 임시 경로"),


    //방송중인방 체크
    방송중체크_방송중("1", "방송중일 때"),
    방송중체크_DJ비정상종료("5", "DJ가 비정상 종료 되었을 때"),
    
    //방송방 참여 막기
    시스템설정_방송방막기("system_config", "방송방_생성_참여_불가"), //N이면 참여가능, Y이면 참여불가 (시스템 점검 전에 값을 바꿔 유입을 막는다)

    //방송주제
    방송주제("subject_type", "방송 주제 목록"),

    //클립주제
    클립주제("clip_type", "클립 주제 목록"),


    //클립 배경 이미지 PREFIX
    클립_배경_PREFIX("/clip-bg_0", "포토서버 클립 이미지 실제 경로(done) prefix"),
    클립_배경_임시_PREFIX("/clip-bg_1", "포토서버 클립 이미지 임시 경로 prefix "),
    클립_배경_썸네일_PREFIX("/clip-bg_2", "포토서버 클립 이미지 썸네일 prefix"),
    클립_배경_디폴트_PREFIX("/clip-bg_3", "포토서버 클립 이미지 배경 디폴트 prefix"),


   ;

    final private String code;
    final private String desc;

    Code(String code, String desc){
        this.code = code;
        this.desc = desc;
    }
}
