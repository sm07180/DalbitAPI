package com.dalbit.admin.service;

import com.dalbit.admin.vo.AdminMenuVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Service
public class AdminCommonService {

    public List<AdminMenuVo> getAdminMenuInSession(HttpServletRequest request){
        List<AdminMenuVo> adminMenuVoList = (List<AdminMenuVo>) request.getSession().getAttribute("adminMenu");
        return adminMenuVoList;
    }
}
