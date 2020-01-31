package com.dalbit.common.vo;

import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

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
    private Double lat;
    private Double lon;
    private String status;

    public LocationVo(HashMap map) {
        this.zip = DalbitUtil.getStringMap(map, "zip");
        this.country = DalbitUtil.getStringMap(map, "country");
        this.timezone = DalbitUtil.getStringMap(map, "timezone");
        this.query = DalbitUtil.getStringMap(map, "query");
        this.regionName = DalbitUtil.getStringMap(map, "regionName");
        this.countryCode = DalbitUtil.getStringMap(map, "countryCode");
        this.lat = DalbitUtil.getDoubleMap(map, "lat");
        this.lon = DalbitUtil.getDoubleMap(map, "lon");
    }
}
