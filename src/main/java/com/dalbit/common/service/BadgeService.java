package com.dalbit.common.service;

import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.vo.BadgeFrameVo;
import com.dalbit.common.vo.BadgeVo;
import com.dalbit.common.vo.FanBadgeVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisHashCommands;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class BadgeService {
    private List<FanBadgeVo> badgeList = new ArrayList<>();

    @Autowired
    CommonDao commonDao;

    @Autowired
    RedisTemplate redisTemplateSocket;

    @Value("${redis.socket.rank.key}")
    private String rankKey;
    @Value("${redis.socket.rank.daily.dj}")
    private String rankDailyDj;
    @Value("${redis.socket.rank.time.dj}")
    private String rankTimeDj;
    @Value("${redis.socket.rank.daily.fan}")
    private String rankDailyFan;
    //@Value("${redis.socket.rank.time.fan}")
    //private String rankTimeFan;

    public void setBadgeInfo(String mem_no, int type){
        RedisHashCommands redisHashCommands = redisTemplateSocket.getConnectionFactory().getConnection().hashCommands();
        Date now = new Date();
        long djRank = 0, fanRank = 0;
        int timeDay = (now.getMinutes() / 5) % 2;
        String djRankKey = timeDay == 0 ? rankTimeDj : rankDailyDj;
        String fanRankKey = rankDailyFan;

        String djTime = new String(redisHashCommands.hGet(rankKey.getBytes(), djRankKey.getBytes()));
        String fanTime = new String(redisHashCommands.hGet(rankKey.getBytes(), fanRankKey.getBytes()));

        String djRanks = new String(redisHashCommands.hGet(djRankKey.getBytes(), djTime.getBytes()));
        String fanRanks = new String(redisHashCommands.hGet(fanRankKey.getBytes(), fanTime.getBytes()));

        if(!DalbitUtil.isEmpty(djRanks)){
            HashMap<String, Object> ranks = new Gson().fromJson(djRanks, HashMap.class);
            if(!DalbitUtil.isEmpty(ranks) && ranks.containsKey(mem_no)){
                LinkedTreeMap<String, Double> tmp = (LinkedTreeMap)ranks.get(mem_no);
                djRank = tmp.get("rank").longValue();
            }
        }
        if(!DalbitUtil.isEmpty(fanRanks)){
            HashMap<String, Object> ranks = new Gson().fromJson(fanRanks, HashMap.class);
            if(!DalbitUtil.isEmpty(ranks) && ranks.containsKey(mem_no)){
                LinkedTreeMap<String, Double> tmp = (LinkedTreeMap)ranks.get(mem_no);
                fanRank = tmp.get("rank").longValue();
            }
        }

        HashMap param = new HashMap();
        param.put("mem_no", mem_no);
        param.put("type", type);
        param.put("timeDay", timeDay);
        param.put("djRank", djRank);
        param.put("fanRank", fanRank);
        param.put("by", "api");
        badgeList = commonDao.callMemberBadgeListServer(param);
    }

    public List<BadgeVo> getCommonBadge(){
        if(DalbitUtil.isEmpty(badgeList)){
            return new ArrayList<>();
        }else{
            List<BadgeVo> commonBadge = new ArrayList<>();
            for(FanBadgeVo fanBadgeVo : badgeList){
                commonBadge.add(new BadgeVo(fanBadgeVo));
            }
            return commonBadge;
        }
    }

    public BadgeFrameVo getBadgeFrame(){
        if(DalbitUtil.isEmpty(badgeList)){
            return new BadgeFrameVo();
        }else{
            BadgeFrameVo badgeFrameVo = new BadgeFrameVo();
            for(FanBadgeVo fanBadgeVo : badgeList){
                if(!DalbitUtil.isEmpty(fanBadgeVo.getFrameTop()) && !DalbitUtil.isEmpty(fanBadgeVo.getFrameChat())){
                    badgeFrameVo.setFrameTop(fanBadgeVo.getFrameTop());
                    badgeFrameVo.setFrameChat(fanBadgeVo.getFrameChat());
                    badgeFrameVo.setFrameAni(fanBadgeVo.getFrameAni());
                    break;
                }
            }
            return badgeFrameVo;
        }
    }

    public BadgeFrameVo getBadgeFrame(String mem_no, int type){
        HashMap param = new HashMap();
        param.put("mem_no", mem_no);
        param.put("type", type);
        param.put("by", "api");
        return commonDao.callMemberBadgeFrame(param);
    }


}
