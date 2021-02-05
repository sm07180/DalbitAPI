package com.dalbit.member.vo.request;

import com.dalbit.validator.annotation.Password;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpVo {

    @NotBlank(message = "{\"ko_KR\" : \"회원구분를\"}")
    @NotNull(message = "{\"ko_KR\" : \"회원구분를\"}")
    @Size(message = "{\"ko_KR\" : \"회원구분를\"}", max = 1)
    private String memType;

    @NotBlank(message = "{\"ko_KR\" : \"아이디를 \"}")
    @NotNull(message = "{\"ko_KR\" : \"아이디를\"}")
    @Size(message = "{\"ko_KR\" : \"아이디를\"}", max = 50)
    private String memId;

    @Password
    private String memPwd;

    /*@Size(message = "{\"ko_KR\" : \"성별을\"}", max = 1)*/
    private String gender = "n";

    @NotBlank(message = "{\"ko_KR\" : \"닉네임을\"}")
    @NotNull(message = "{\"ko_KR\" : \"닉네임을\"}")
    @Size(message = "{\"ko_KR\" : \"닉네임을\"}", min = 2)
    private String nickNm;

    /*@NotBlank(message = "{\"ko_KR\" : \"생년월일을\"}")
    @NotNull(message = "{\"ko_KR\" : \"생년월일을\"}")
    @Size(message = "{\"ko_KR\" : \"생년월일을\"}", min = 8, max = 8)*/
    private String birth = "20200101";

    @NotBlank(message = "{\"ko_KR\" : \"약관 동의를\"}")
    @NotNull(message = "{\"ko_KR\" : \"약관 동의를\"}")
    @Size(message = "{\"ko_KR\" : \"약관 동의를\"}", max = 1)
    private String term1, term2, term3, term4, term5;

    private String name;
    private String profImg;
    private String profImgRacy;
    private String email;
    private String nativeTid;
}
