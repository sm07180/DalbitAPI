package com.dalbit.common.service;

import com.dalbit.common.dao.EmailDao;
import com.dalbit.common.vo.EmailVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    @Autowired
    EmailDao emailDao;

    /**
     * 이메일 발송 - 타겟
     * */
    public int sendEmail(EmailVo emailVo){
        return emailDao.sendEmail(emailVo);
    }
}
