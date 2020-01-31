package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationVo {

    private String zip;
    private String country;
    private String city;
    private String timezone;
    private String query;
    private String regionName;
    private String countryCode;
    private int lat;
    private int lon;
    private String status;

}
