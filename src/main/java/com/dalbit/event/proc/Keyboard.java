package com.dalbit.event.proc;

import com.dalbit.event.vo.KeyboardBonusVO;
import com.dalbit.event.vo.KeyboardRewardVO;
import com.dalbit.event.vo.KeyboardWinVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Keyboard {

    //당첨자리스트
    @Select("CALL rd_data.p_dalla_lucky_chat_win_list(#{theDate})")
    List<KeyboardWinVO> keyboardWinList(@Param("theDate") String theDate);

    //보상수령
    //-2:이미수령, -1:상품 최대지급수량 초과, 0:에러 , 1:정상
    @Select("CALL rd_data.p_dalla_lucky_chat_com_ins(#{theDate}, #{theSeq}, #{memNo}, #{preCode}, #{preSlct})")
    Integer keyboardReward(KeyboardRewardVO vo);

    //키보드히어로 보너스회원정보
    @Select("CALL rd_data.p_dalla_lucky_bonus_sel(#{memNo})")
    KeyboardBonusVO keyboardMyInfo(String memNo);

    @Select("CALL rd_data.p_dalla_lucky_bonus_dal_ins(#{memNo}, #{tDate}, #{bonusSlct})")
    Integer getBonusItem(String memNo, String tDate, String bonusSlct);


}
