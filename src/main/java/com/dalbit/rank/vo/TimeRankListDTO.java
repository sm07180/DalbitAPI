package com.dalbit.rank.vo;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class TimeRankListDTO {

    private Integer rankSlct;
    private Integer page;
    private Integer records;
    private String rankingDate;
    private String prevRankingDate;

}
