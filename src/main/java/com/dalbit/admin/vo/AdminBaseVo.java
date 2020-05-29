package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@ToString
public class AdminBaseVo implements Serializable {

    List<AdminMenuVo> adminMenuList;
}
