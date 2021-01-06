package com.dalbit.interceptor;

import com.dalbit.admin.dao.AdminDao;
import com.dalbit.admin.vo.AdminMenuVo;
import com.dalbit.admin.vo.SearchVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@Slf4j
public class AdminAuthCheckInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    AdminDao adminDao;

    private final String[] IGNORE_URLS = {
        //"/ctrl/check/service", "/self/auth"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{

        String uri = request.getRequestURI();
        for(String ignoreUri : IGNORE_URLS) {
            if(uri.startsWith(ignoreUri)) {
                return true;
            }
        }

        String authToken = request.getHeader(DalbitUtil.getProperty("sso.header.cookie.name"));
        if(jwtUtil.validateToken(authToken)){
            TokenVo tokenVo = jwtUtil.getTokenVoFromJwt(authToken);
            tokenVo.getMemNo();

            var searchVo = new SearchVo();
            searchVo.setMem_no(tokenVo.getMemNo());
            ProcedureVo procedureVo = new ProcedureVo(searchVo);
            ArrayList<AdminMenuVo> menuList = adminDao.callMobileAdminMenuAuth(procedureVo);
            if(DalbitUtil.isEmpty(menuList)){
                responseJson(response, gsonUtil.toJson(new JsonOutputVo(Status.관리자메뉴조회_권한없음)));
                return false;
            }

            HttpSession session = request.getSession();
            session.setAttribute("adminMenu", menuList);
            //responseJson(response, gsonUtil.toJson(new JsonOutputVo(Status.조회, menuList)));
            return true;

        }

        responseJson(response, gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류)));
        return false;

    }

    public void responseJson(HttpServletResponse response, String json) throws IOException {
        PrintWriter w = response.getWriter();
        w.write(json);
        w.flush();
        w.close();
    }
}
