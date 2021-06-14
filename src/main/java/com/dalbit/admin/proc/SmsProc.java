package com.dalbit.admin.proc;

import com.dalbit.admin.vo.SmsProcVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public interface SmsProc {

    @Select("CALL rd_data.p_hpMsg_send(#{msgSlct}, #{sendPhone}, #{sendMemNo}, #{rcvPhone}, #{rcvMemId}, " +
            "#{titleConts}, #{msgBody}, #{atchFile}, #{rsrvDt}, #{tranSlct})")
    int sendSms(SmsProcVO smsProcVO);

}
