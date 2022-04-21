package com.dalbit.broadcast.dao;

import com.dalbit.broadcast.vo.OwnerResultVo;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerDao {

    /**
     * ##### 방송방 방장정보
     *
     * CALL p_dalla_room_master_sel(
     * ,roomNo BIGINT			-- 방번호
     * )
     *
     * # RETURN
     * room_no      BIGINT          -- 방번호
     * mem_no       BIGINT          -- 방장회원번호
     * mem_nick     VARCHAR(20)     -- 방장닉네임
     */
    OwnerResultVo pDallaRoomMasterSel(String roomNo);

}
