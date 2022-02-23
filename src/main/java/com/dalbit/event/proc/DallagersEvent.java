package com.dalbit.event.proc;

import com.dalbit.event.vo.DallagersEventScheduleSelVo;
import com.dalbit.event.vo.DallagersInitialAddVo;
import com.dalbit.event.vo.DallagersRoomFerverSelVo;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Component
@Repository
public interface DallagersEvent {

    /**
     * 이니셜 등록
     * @Param
     * memNo               BIGINT          회원번호
     * ,roomNo             BIGINT          방번호
     * ,collectSlct        INT             구분[1:방송청취,2:방송시간,3:보낸달,4: 부스터수,5:받은별]
     * ,dallaGubun         CHAR(1)         구분[d,a,l]
     * ,insDallaCnt        BIGINT          받은 이니셜값
     * ,slctValCnt         BIGINT          구분에서의 값 [방송청취,방송시간,보낸달,부스터수,받은별]
     * ,feverYn            VARCHAR(20)     피버[y,n]
     *
     * @Return
     * s_return		INT		--  -3: 이벤트 기간 초과 , -2: 실시간방 없음 , -1: 회원 데이터 없음 , 0: 에러, 1:정상
     * */
    @Select("Call rd_data.p_evt_dalla_collect_ins(#{memNo}, #{roomNo}, #{collectSlct}, #{dallaGubun}, #{insDallaCnt}, #{slctValCnt}, #{feverYn})")
    Integer pEvtDallaCollectIns(DallagersInitialAddVo param);

    /**
     * 피버타임 등록
     * @Param
     * feverSlct 		BIGINT		-- 피버 종류(1:선물,2:방송시간)
     * ,roomNo 	        BIGINT		-- 방번호
     *
     * @Return
     *
     * s_return		INT		--  -4:방송방시간 피버 2회 초과,  -3: 이벤트 기간 초과 , -2: 실시간방 없음 , -1: 현재 피버타임중 , 0: 에러
     * */
    @Select("CALL rd_data.p_evt_dalla_collect_room_fever_ins(#{feverSlct}, #{roomNo})")
    Integer pEvtDallaCollectRoomFeverIns(Map<String, Object> param);

    /**
     * 피버타임 종료
     * roomNo 	        BIGINT		-- 방번호
     *
     * # RETURN
     * s_return		INT		--   -3: 이벤트 기간 초과 , -2: 실시간방 없음 , -1: 현재 피버타임없음 , 0: 에러, 1:정상
     * */
    @Select("CALL rd_data.p_evt_dalla_collect_room_fever_end(#{roomNo})")
    Integer pEvtDallaCollectRoomFeverEnd(Long roomNo);

    /**
     * 달라이벤트 방 피버 정보
     * @Param
     * roomNo 	    BIGINT		-- 방번호
     *
     * @Return
     * room_no                 BIGINT   방번호
     * gift_fever_cnt          INT      피버횟수(선물)
     * play_fever_cnt          INT      피버횟수(시간)
     * tot_fever_cnt           INT      총합
     * fever_yn                CHAR     피버확인[y,n]
     * gold		               INT		받은별
     * booster_cnt	           INT		부스터수
     * */
    @Select("CALL rd_data.p_evt_dalla_collect_room_fever_sel(#{roomNo})")
    DallagersRoomFerverSelVo pEvtDallaCollectRoomFeverSel(Long roomNo);

    /**
     * 달라이벤트 스케줄 (현재 이벤트 진행여부 체크용도)
     * @Param
     *
     * @Return
     * room_no                 BIGINT   방번호
     * gift_fever_cnt          INT      피버횟수(선물)
     * play_fever_cnt          INT      피버횟수(시간)
     * tot_fever_cnt           INT      총합
     * fever_yn                CHAR     피버확인[y,n]
     * */
    @Select("CALL rd_data.p_evt_dalla_collect_schedule_sel(#{roomNo})")
    DallagersEventScheduleSelVo pEvtDallaCollectScheduleSel(Long roomNo);


}
