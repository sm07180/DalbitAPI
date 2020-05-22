package com.dalbit.admin.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class AdminMenuVo {

    public List<AdminMenuVo> twoDepth;

    public AdminMenuVo(){}

    public AdminMenuVo(int depth){
        this.depth = depth;
    }

    private int idx;
    private String menu_name;
    private String menu_url;
    private int depth;
    private int order_no;
    private int parent_idx;
    private String icon;
    private int is_pop;
    private int is_comming_soon;
    private int is_use;
    private String mobile_yn;
    private Date reg_date;
    private Date last_upd_date;

    private int is_read;
    private int is_insert;
    private int is_delete;

    private String id;
    private String parent;
    private String text;

}
