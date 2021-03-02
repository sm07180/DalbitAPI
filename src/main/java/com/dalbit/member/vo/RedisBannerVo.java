package com.dalbit.member.vo;

import com.dalbit.main.vo.BannerVo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RedisBannerVo {
    private long setTs;
    private List<BannerVo> bannerList;
}
