package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.math.BigInteger;

@Getter @Setter
public class ClipPlayVo {
    @NotBlank(message = "{\"ko_KR\" : \"클립 번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립 번호를\"}")
    @Pattern(regexp="^[0-9]{10,15}",message="{\"ko_KR\" : \"클립 번호를\"}")
    private String clipNo;
}
