package com.dalbit.common.service;

import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.vo.BadgeFrameVo;
import com.dalbit.common.vo.BadgeVo;
import com.dalbit.common.vo.FanBadgeVo;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class BadgeService {
    private List<FanBadgeVo> badgeList = new ArrayList<>();

    @Autowired
    CommonDao commonDao;

    public void setBadgeInfo(String mem_no, int type){
        HashMap param = new HashMap();
        param.put("mem_no", mem_no);
        param.put("type", type);
        param.put("by", "api");
        badgeList = commonDao.callMemberBadgeList(param);
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
