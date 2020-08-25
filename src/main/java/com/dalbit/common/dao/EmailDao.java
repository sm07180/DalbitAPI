package com.dalbit.common.dao;

import com.dalbit.common.vo.EmailVo;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailDao {
    int sendEmail(EmailVo emailVo);
}
