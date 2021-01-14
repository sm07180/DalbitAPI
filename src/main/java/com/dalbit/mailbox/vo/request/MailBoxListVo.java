package com.dalbit.mailbox.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MailBoxListVo {
    private Integer page;
    private Integer records;
}
