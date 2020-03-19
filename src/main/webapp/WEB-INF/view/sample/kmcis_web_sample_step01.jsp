<%
	//************************************************************************
	//																		//
	//		본 샘플소스는 개발 및 테스트를 위한 목적으로 만들어졌으며,		//
	//																		//
	//		실제 서비스에 그대로 사용하는 것을 금합니다.					//
	//																		//
	//************************************************************************
%>
<%
	/*response.setHeader("Pragma","no-cache");			// HTTP1.0 캐쉬 방지
	response.setDateHeader("Expires",0);				// proxy 서버의 캐쉬 방지
	response.setHeader("Pragma", "no-store");			// HTTP1.1 캐쉬 방지
	if(request.getProtocol().equals("HTTP/1.1"))
			response.setHeader("Cache-Control", "no-cache"); // HTTP1.1 캐쉬 방지*/
%>
<%@ page  contentType = "text/html;charset=utf-8"%>
<%@ page import ="java.util.*,java.text.SimpleDateFormat"%>
<%
        //날짜 생성
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String day = sdf.format(today.getTime());

        java.util.Random ran = new Random();
        //랜덤 문자 길이
        int numLength = 6;
        String randomStr = "";

        for (int i = 0; i < numLength; i++) {
            //0 ~ 9 랜덤 숫자 생성
            randomStr += ran.nextInt(10);
        }

		//reqNum은 최대 40byte 까지 사용 가능
        String reqNum = day + randomStr;
%>
<html>
    <head>
        <title>본인인증서비스  테스트</title>
        <meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
		<meta name="robots" content="noindex">
        <style>
            <!--
            body,p,ol,ul,td
            {
                font-family: 굴림;
                font-size: 12px;
            }

            a:link { size:9px;color:#000000;text-decoration: none; line-height: 12px}
            a:visited { size:9px;color:#555555;text-decoration: none; line-height: 12px}
            a:hover { color:#ff9900;text-decoration: none; line-height: 12px}

            .style1 {
                color: #6b902a;
                font-weight: bold;
            }
            .style2 {
                color: #666666
            }
            .style3 {
                color: #3b5d00;
                font-weight: bold;
            }
            -->
        </style>
    </head>
    <body bgcolor="#FFFFFF" topmargin=0 leftmargin=0 marginheight=0 marginwidth=0>
        <center>
            <br><br><br>
            <span class="style1">본인인증서비스 테스트</span><br>

            <form name="reqForm" method="post" action="https://devm-hwlee.dalbitcast.com:4431/sample/auth/step2">
                <table cellpadding=1 cellspacing=1>
                    <tr>
                        <td align=center>회원사ID</td>
                        <td align=left><input type="text" name="cpId" size='41' maxlength ='10' value = "CLUM1001"></td>
                    </tr>
                    <tr>
                        <td align=center>URL코드</td>
                        <td align=left><input type="text" name="urlCode" size='41' maxlength ='6' value="031001"></td>
                    </tr>
                    <tr>
                        <td align=center>요청번호</td>
                        <td align=left><input type="text" name="certNum" size='41' maxlength ='40' value='<%=reqNum%>'></td>
                    </tr>
                    <tr>
                        <td align=center>요청일시</td>
						<!-- 현재시각 세팅(YYYYMMDDHI24MISS) -->
                        <td align=left><input type="text" name="date" size="41" maxlength ='14' value="<%=day%>"></td>
					</tr>
                    <tr>
                        <td align=center>본인인증방법</td>
                        <td align=left>
                            <select name="certMet" style="width:300px">
                                <option value="A">인증방법선택 화면</option>
                                <option value="M"selected>휴대폰인증 화면</option>
                                <option value="C">신용카드인증 화면</option>
                                <option value="P">공인인증서 화면</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align=center>이용자성명</td>
                        <td align=left><input type="text" name="name"  size="41" maxlength ='20' value=""></td>
                    </tr>
                    <tr>
                        <td align=center>휴대폰번호</td>
                        <td align=left>
						  <input type="text" name="phoneNo" id="textfield" style="width:160px;" class="hpinput" maxlength="11" value=""/>
						</td>
                    </tr>
                    <tr>
                        <td align=center>이통사</td>
                        <td align=left>
						<input type="radio" name="phoneCorp" value="SKT"> SKT
						<input type="radio" name="phoneCorp" value="KTF"> KT
						<input type="radio" name="phoneCorp" value="LGT"> LG U+
						<input type="radio" name="phoneCorp" value="SKM"> SKTmvno
						<input type="radio" name="phoneCorp" value="KTM"> KTmvno
						<input type="radio" name="phoneCorp" value="LGM"> LGU+mvno
						</td>
                    </tr>
                    <tr>
                        <td align=center>생년월일</td>
                        <td align=left>
						  <input type="text" name="birthDay" style="width:160px;" class="hpinput" maxlength="8" value=""/>
						</td>
                    </tr>
                    <tr>
                        <td align=center>이용자성별</td>
                        <td align=left><input type="radio" name="gender" value="0"> 남 <input type="radio" name="gender" id="radio" value="1"> 여</td>
                    </tr>
                    <tr>
                        <td align=center>내외국인</td>
                        <td align=left>
						<select name="nation" id="select" style="width:160px;">
                            <option value = "">-선택-</option>
                            <option value = "0" selected>내국인</option>
                            <option value = "1">외국인</option>
						</select>
						</td>
                    </tr>
                    <tr>
                        <td align=center>추가DATA정보</td>
                        <td align=left><input type="text" name="plusInfo"  size="41" maxlength ='320' value=""></td>
                    </tr>
                    <tr>
                        <td align=center>결과수신URL</td>
                        <td align=left><input type="text" name="tr_url" size="41" value="https://devm-hwlee.dalbitcast.com:4431/sample/auth/step4"></td>
                    </tr>
                    <tr>
                        <td align=center>IFrame사용여부</td>
                        <td align=left>
                            <select name="tr_add" style="width:300px">
                                <option value="N"selected>미사용</option>
                                <option value="Y">사용</option>
                            </select>
                        </td>
                    </tr>					
                </table>
                <br><br>
                <input type="submit" value="본인인증 테스트">
            </form>
            <br>
            <br>
            이 Sample화면은 본인인증서비스 테스트를 위해 제공하고 있는 화면입니다.<br>
            <br>
        </center>
    </body>
</html>