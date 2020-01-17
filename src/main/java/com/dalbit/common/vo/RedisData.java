package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RedisData implements Serializable {

    private static final long serialVersionUID = 1L;

    public RedisData() {}

    public RedisData(String sourceId, String itemId) {
        super();
        this.sourceId = sourceId;
        this.itemId = itemId;
    }

    private String sourceId;
    private String itemId;

}
