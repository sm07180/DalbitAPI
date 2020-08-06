package com.dalbit.admin.dao;

import com.dalbit.admin.vo.SmsVo;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsDao {
    int sendSms(SmsVo smsVo);

    int sendSmsTargetTest(SmsVo smsVo);

    int sendSmsTargetMemberAll(SmsVo smsVo);
}
