package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SettingListVo extends AdminBaseVo {

   private String code;
   private String value;

   private String type;
   private int is_use;

   private int rowNum;
}
