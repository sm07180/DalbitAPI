<%
	//************************************************************************
	//																		//
	//		º» »ùÇÃ¼Ò½º´Â °³¹ß ¹× Å×½ºÆ®¸¦ À§ÇÑ ¸ñÀûÀ¸·Î ¸¸µé¾îÁ³À¸¸ç,		//
	//																		//
	//		½ÇÁ¦ ¼­ºñ½º¿¡ ±×´ë·Î »ç¿ëÇÏ´Â °ÍÀ» ±İÇÕ´Ï´Ù.					//
	//																		//
	//************************************************************************
%>
<%
    response.setHeader("Pragma", "no-cache" );
    response.setDateHeader("Expires", 0);
    response.setHeader("Pragma", "no-store");
    response.setHeader("Cache-Control", "no-cache" );
%>
<%@ page  contentType = "text/html;charset=ksc5601"%>
<%@ page import = "java.util.*" %>
<%@ page import = "java.util.regex.*" %>
<%@ page import = "java.text.*" %>
<%
    //tr_cert µ¥ÀÌÅÍ º¯¼ö ¼±¾ğ ---------------------------------------------------------------
	String tr_cert       = "";
    String cpId          = request.getParameter("cpId");        // È¸¿ø»çID
    String urlCode       = request.getParameter("urlCode");     // URLÄÚµå
    String certNum       = request.getParameter("certNum");     // ¿äÃ»¹øÈ£
    String date          = request.getParameter("date");        // ¿äÃ»ÀÏ½Ã
    String certMet       = request.getParameter("certMet");     // º»ÀÎÀÎÁõ¹æ¹ı
    String name          = request.getParameter("name");        // ¼º¸í
    String phoneNo	     = request.getParameter("phoneNo");	    // ÈŞ´ëÆù¹øÈ£
    String phoneCorp     = request.getParameter("phoneCorp");   // ÀÌµ¿Åë½Å»ç
	if(phoneCorp == null) phoneCorp = "";
	String birthDay	     = request.getParameter("birthDay");	// »ı³â¿ùÀÏ
	String gender	     = request.getParameter("gender");		// ¼ºº°
	if(gender == null) gender = "";
    String nation        = request.getParameter("nation");      // ³»¿Ü±¹ÀÎ ±¸ºĞ
	String plusInfo      = request.getParameter("plusInfo");	// Ãß°¡DATAÁ¤º¸
	String extendVar     = "0000000000000000";                  // È®Àåº¯¼ö
    //End-tr_cert µ¥ÀÌÅÍ º¯¼ö ¼±¾ğ ---------------------------------------------------------------

	String tr_url     = request.getParameter("tr_url");         // º»ÀÎÀÎÁõ¼­ºñ½º °á°ú¼ö½Å POPUP URL
	String tr_add     = request.getParameter("tr_add");         // IFrame»ç¿ë¿©ºÎ

	/** certNum ÁÖÀÇ»çÇ× **************************************************************************************
	* 1. º»ÀÎÀÎÁõ °á°ú°ª º¹È£È­¸¦ À§ÇÑ Å°·Î È°¿ëµÇ¹Ç·Î Áß¿äÇÔ.
	* 2. º»ÀÎÀÎÁõ ¿äÃ»½Ã Áßº¹µÇÁö ¾Ê°Ô »ı¼ºÇØ¾ßÇÔ. (¿¹-½ÃÄö½º¹øÈ£)
	* 3. certNum°ªÀº º»ÀÎÀÎÁõ °á°ú°ª ¼ö½Å ÈÄ º¹È£È­Å°·Î »ç¿ëÇÔ.
	       tr_url°ª¿¡ certNum°ªÀ» Æ÷ÇÔÇØ¼­ Àü¼ÛÇÏ°í, (tr_url = tr_url + "?certNum=" + certNum;)
		   tr_url¿¡¼­ Äõ¸®½ºÆ®¸µ ÇüÅÂ·Î certNum°ªÀ» ¹ŞÀ¸¸é µÊ. (certNum = request.getParameter("certNum"); )
	*
	***********************************************************************************************************/
%>
<%!
   // ÆÄ¶ó¹ÌÅÍ À¯È¿¼º °ËÁõ --------------------------------------------
	boolean b = true;
	String regex = "";
	String regex1 = "";

	public Boolean paramChk(String patn, String param){
		Pattern pattern = Pattern.compile(patn);
		Matcher matcher = pattern.matcher(param);
		b = matcher.matches();
		return b;
	}
%>
<%
	/*
	regex = "[A-Z]*";
	regex1 = "[0-9]*";
	if( cpId.length() == 8 ){
		String engcpId = cpId.substring(0,4);
		String numcpId = cpId.substring(4,8);
		if( !paramChk(regex, engcpId) || !paramChk(regex1, numcpId) ){
			out.println("<script> alert('È¸¿ø»çID ºñÁ¤»ó');");
			out.println("history.back(); </script>");
		}
	} else {
		out.println("<script> alert('È¸¿ø»çID ºñÁ¤»ó');");
		out.println("history.back(); </script>");
	}
	*/
	
	/*
	regex = "[0-9]*";
	if( urlCode.length() != 6 || !paramChk(regex, urlCode) ){
		out.println("<script> alert('URLÄÚµå ºñÁ¤»ó');");
		out.println("history.back(); </script>");
	}
	*/

	/*
	if( certNum.length() == 0 || certNum.length() > 40){
		out.println("<script> alert('¿äÃ»¹øÈ£¸¦ ÀÔ·ÂÇÏ¼¼¿ä.');");
		out.println("history.back(); </script>");
	}
	*/

	/*
	regex = "[0-9]*";
	if( date.length() != 14 || !paramChk(regex, date) ){
		out.println("<script> alert('¿äÃ»ÀÏ½Ã ºñÁ¤»ó');");
		out.println("history.back(); </script>");
	}
	*/

	/*
	regex = "[A-Z]*";
	if( certMet.length() != 1 || !paramChk(regex, certMet) ){
		out.println("<script> alert('º»ÀÎÀÎÁõ¹æ¹ı ºñÁ¤»ó');");
		out.println("history.back(); </script>");
	}
	*/
	
	/*
	regex = "[0-9]*";
	if( phoneNo.length() != 0 ){
		if( (phoneNo.length() != 10 && phoneNo.length() != 11) || !paramChk(regex, phoneNo) ){
			out.println("<script> alert('ÈŞ´ëÆù¹øÈ£ ºñÁ¤»ó');");
			out.println("history.back(); </script>");
		}
	}
	*/

	/*
	regex = "[A-Z]*";
	if( phoneCorp.length() != 0 ){
		if( phoneCorp.length() != 3 || !paramChk(regex, phoneCorp) ){
			out.println("<script> alert('ÀÌµ¿Åë½Å»ç ºñÁ¤»ó');");
			out.println("history.back(); </script>");
		}
	}
	*/

	/*
	regex = "[0-9]*";
	if( birthDay.length() != 0 ){
		if( birthDay.length() != 8 || !paramChk(regex, birthDay) ){
			out.println("<script> alert('»ı³â¿ùÀÏ ºñÁ¤»ó');");
			out.println("history.back(); </script>");
		}
	}
	*/
	
	/*
	regex = "[0-9]*";
	if( gender.length() != 0 ){
		if( gender.length() != 1 || !paramChk(regex, gender) ){
			out.println("<script> alert('¼ºº° ºñÁ¤»ó');");
			out.println("history.back(); </script>");
		}
	}
	*/
	
	/*
	regex = "[0-9]*";
	if( nation.length() != 0 ){
		if( nation.length() != 1 || !paramChk(regex, nation) ){
			out.println("<script> alert('³»/¿Ü±¹ÀÎ ºñÁ¤»ó');");
			out.println("history.back(); </script>");
		}
	}
	*/
	
	/*
	regex = "[\\sA-Za-z°¡-ÆR.,-]*";
	if( name.length() != 0 ){
		if( name.length() > 60 || !paramChk(regex, name) ){
			out.println("<script> alert('¼º¸í ºñÁ¤»ó');");
			out.println("history.back(); </script>");
		}
	}
	*/
	
	/*
	if( tr_url.length() == 0 ){
		out.println("<script> alert('°á°ú¼ö½Å URL ºñÁ¤»ó');");
		out.println("history.back(); </script>");
	}
	*/
	
	/*
	regex = "[A-Z]*";
	if( tr_add.length() != 0 ){
		if( tr_add.length() != 1 || !paramChk(regex, tr_add) ){
			out.println("<script> alert('IFrame»ç¿ë¿©ºÎ ºñÁ¤»ó');");
			out.println("history.back(); </script>");
		}
	}
	*/

	// END ÆÄ¶ó¹ÌÅÍ À¯È¿¼º °ËÁõ --------------------------------------------

    //01. ÇÑ±¹¸ğ¹ÙÀÏÀÎÁõ(ÁÖ) ¾ÏÈ£È­ ¸ğµâ ¼±¾ğ
   com.icert.comm.secu.IcertSecuManager seed  = new com.icert.comm.secu.IcertSecuManager();

	//02. 1Â÷ ¾ÏÈ£È­ (tr_cert µ¥ÀÌÅÍº¯¼ö Á¶ÇÕ ÈÄ ¾ÏÈ£È­)
	String enc_tr_cert = "";
	tr_cert            = cpId +"/"+ urlCode +"/"+ certNum +"/"+ date +"/"+ certMet +"/"+ birthDay +"/"+ gender +"/"+ name +"/"+ phoneNo +"/"+ phoneCorp +"/"+ nation +"/"+ plusInfo +"/"+ extendVar;
	enc_tr_cert        = seed.getEnc(tr_cert, "");

	//03. 1Â÷ ¾ÏÈ£È­ µ¥ÀÌÅÍ¿¡ ´ëÇÑ À§º¯Á¶ °ËÁõ°ª »ı¼º (HMAC)
	String hmacMsg = "";
	hmacMsg = seed.getMsg(enc_tr_cert);

	//04. 2Â÷ ¾ÏÈ£È­ (1Â÷ ¾ÏÈ£È­ µ¥ÀÌÅÍ, HMAC µ¥ÀÌÅÍ, extendVar Á¶ÇÕ ÈÄ ¾ÏÈ£È­)
	tr_cert  = seed.getEnc(enc_tr_cert + "/" + hmacMsg + "/" + extendVar, "");
%>

<html>
<head>
<title>º»ÀÎÀÎÁõ¼­ºñ½º Sample È­¸é</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<meta name="robots" content="noindex">
<style>
<!--
   body,p,ol,ul,td
   {
	 font-family: ±¼¸²;
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

<script language=javascript>
<!--
   window.name = "kmcis_web_sample";
   
   var KMCIS_window;

   function openKMCISWindow(){    

    var UserAgent = navigator.userAgent;
    /* ¸ğ¹ÙÀÏ Á¢±Ù Ã¼Å©*/
    // ¸ğ¹ÙÀÏÀÏ °æ¿ì (º¯µ¿»çÇ× ÀÖÀ»°æ¿ì Ãß°¡ ÇÊ¿ä)
    if (UserAgent.match(/iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson/i) != null || UserAgent.match(/LG|SAMSUNG|Samsung/) != null) {
   		 document.reqKMCISForm.target = '';
	  } 
	  
	  // ¸ğ¹ÙÀÏÀÌ ¾Æ´Ò °æ¿ì
	  else {
   		KMCIS_window = window.open('', 'KMCISWindow', 'width=425, height=550, resizable=0, scrollbars=no, status=0, titlebar=0, toolbar=0, left=435, top=250' );
   		
   		if(KMCIS_window == null){
  			alert(" ¡Ø À©µµ¿ì XP SP2 ¶Ç´Â ÀÎÅÍ³İ ÀÍ½ºÇÃ·Î·¯ 7 »ç¿ëÀÚÀÏ °æ¿ì¿¡´Â \n    È­¸é »ó´Ü¿¡ ÀÖ´Â ÆË¾÷ Â÷´Ü ¾Ë¸²ÁÙÀ» Å¬¸¯ÇÏ¿© ÆË¾÷À» Çã¿ëÇØ ÁÖ½Ã±â ¹Ù¶ø´Ï´Ù. \n\n¡Ø MSN,¾ßÈÄ,±¸±Û ÆË¾÷ Â÷´Ü Åø¹Ù°¡ ¼³Ä¡µÈ °æ¿ì ÆË¾÷Çã¿ëÀ» ÇØÁÖ½Ã±â ¹Ù¶ø´Ï´Ù.");
      }
   		
   		document.reqKMCISForm.target = 'KMCISWindow';
	  }
	  
	  document.reqKMCISForm.action = 'https://www.kmcert.com/kmcis/web/kmcisReq.jsp';
	  document.reqKMCISForm.submit();
  }
//-->
</script>

</head>

<body bgcolor="#FFFFFF" topmargin=0 leftmargin=0 marginheight=0 marginwidth=0>

<center>
<br><br><br><br><br><br>
<span class="style1">º»ÀÎÀÎÁõ¼­ºñ½º ¿äÃ»È­¸é SampleÀÔ´Ï´Ù.</span><br>
<br><br>
<table cellpadding=1 cellspacing=1>
    <tr>
        <td align=center>È¸¿ø»çID</td>
        <td align=left><%=cpId%></td>
    </tr>
    <tr>
        <td align=center>URLÄÚµå</td>
        <td align=left><%=urlCode%></td>
    </tr>
    <tr>
        <td align=center>¿äÃ»¹øÈ£</td>
        <td align=left><%=certNum%></td>
    </tr>
    <tr>
        <td align=center>¿äÃ»ÀÏ½Ã</td>
        <td align=left><%=date%></td>
    </tr>
    <tr>
        <td align=center>º»ÀÎÀÎÁõ¹æ¹ı</td>
        <td align=left><%=certMet%></td>
        </td>
    </tr>
    <tr>
        <td align=center>ÀÌ¿ëÀÚ¼º¸í</td>
        <td align=left><%=name%></td>
    </tr>
    <tr>
        <td align=center>ÈŞ´ëÆù¹øÈ£</td>
        <td align=left><%=phoneNo%></td>
    </tr>
    <tr>
        <td align=center>ÀÌµ¿Åë½Å»ç</td>
        <td align=left><%=phoneCorp%></td>
    </tr>
    <tr>
        <td align=center>»ı³â¿ùÀÏ</td>
		<td align=left><%=birthDay%></td>
    </tr>
	<tr>
        <td align=center>ÀÌ¿ëÀÚ¼ºº°</td>
        <td align=left><%=gender%></td>
    </tr>
    <tr>
        <td align=center>³»¿Ü±¹ÀÎ</td>
        <td align=left><%=nation%></td>
    </tr>
    <tr>
        <td align=center>Ãß°¡DATAÁ¤º¸</td>
        <td align=left><%=plusInfo%></td>
        </td>
    </tr>
    <tr>
        <td align=center>&nbsp</td>
        <td align=left>&nbsp</td>
    </tr>
    <tr width=100>
        <td align=center>¿äÃ»Á¤º¸(¾ÏÈ£È­)</td>
        <td align=left>
            <%=tr_cert.substring(0,50)%>...
        </td>
    </tr>
    <tr>
        <td align=center>°á°ú¼ö½ÅURL</td>
        <td align=left><%=tr_url%></td>
    </tr>
    <tr>
        <td align=center>IFrame»ç¿ë¿©ºÎ</td>
        <td align=left><%=tr_add%></td>
    </tr>	
</table>

<!-- º»ÀÎÀÎÁõ¼­ºñ½º ¿äÃ» form --------------------------->
<form name="reqKMCISForm" method="post" action="#">
    <input type="hidden" name="tr_cert"     value = "<%=tr_cert%>">
    <input type="hidden" name="tr_url"      value = "<%=tr_url%>">
    <input type="hidden" name="tr_add"      value = "<%=tr_add%>">	
    <input type="submit" value="º»ÀÎÀÎÁõ¼­ºñ½º ¿äÃ»"  onclick= "javascript:openKMCISWindow();">
</form>
<BR>
<BR>
<!--End º»ÀÎÀÎÁõ¼­ºñ½º ¿äÃ» form ----------------------->

<br>
<br>
  ÀÌ SampleÈ­¸éÀº º»ÀÎÀÎÁõ¼­ºñ½º ¿äÃ»È­¸é °³¹ß½Ã Âü°í°¡ µÇµµ·Ï Á¦°øÇÏ°í ÀÖ´Â È­¸éÀÔ´Ï´Ù.<br>
<br>
</center>
</BODY>
</HTML>