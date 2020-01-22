package com.dalbit.util;

import com.dalbit.member.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Slf4j
@Component
public class RedisUtil {

    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    HttpSession httpSession;

    @Value("${spring.redis.user.index.prefix}")
    String SPRING_REDIS_USER_INDEX_PREFIX;

    @Value("${spring.redis.session.prefix}")
    String SPRING_REDIS_SESSION_PREFIX;

    @Value("${spring.session.memberInfo.key}")
    String SPRING_SESSION_MEMBERINFO_KEY;

    public boolean isExistLoginSession(String memNo){
        return 0 < redisTemplate.opsForHash().getOperations().boundSetOps(SPRING_REDIS_USER_INDEX_PREFIX + memNo).size();
    }

    public String getSessionId(String memNo){
        Object[] sessionIdArr = (Object[])redisTemplate.opsForHash().getOperations().boundSetOps(SPRING_REDIS_USER_INDEX_PREFIX + memNo).members().toArray();
        for(int i = sessionIdArr.length; i-->0;){
            if(sessionIdArr[i].toString().length() == 36){
                return sessionIdArr[i].toString();
            }
        }
        return "";
    }

    public MemberVo getMemberInfo(String sessionId){
        MemberVo memberVo = null;
        try{
            if(!DalbitUtil.isEmpty(sessionId)){
                memberVo = (MemberVo)redisTemplate.opsForHash().getOperations().boundHashOps(SPRING_REDIS_SESSION_PREFIX + sessionId).get(SPRING_SESSION_MEMBERINFO_KEY);
            }
        }catch(Exception e){
            log.error("RedisUtil.getMemberInfo : {}", e.getMessage());
        }

        return memberVo;
    }

    public MemberVo getMemberInfoFromRedis(String memNo){
        String SessionId = getSessionId(memNo);
        MemberVo memberVo = getMemberInfo(SessionId);
        return memberVo;
    }

    public MemberVo getMemberInfo(){
        String memNo = MemberVo.getMemNo();
        String sessionId = getSessionId(memNo);
        MemberVo memberVo = getMemberInfo(sessionId);
        return memberVo;
    }
}
