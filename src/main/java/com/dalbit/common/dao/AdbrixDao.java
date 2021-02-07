package com.dalbit.common.dao;

import com.dalbit.common.vo.AdbrixVo;
import org.springframework.stereotype.Repository;

@Repository
public interface AdbrixDao {

    AdbrixVo getMakeAdbrixData(String memNo);

}
