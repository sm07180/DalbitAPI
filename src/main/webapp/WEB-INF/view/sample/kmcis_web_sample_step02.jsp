<%
	//************************************************************************
	//																		//
	//		�� ���üҽ��� ���� �� �׽�Ʈ�� ���� �������� �����������,		//
	//																		//
	//		���� ���񽺿� �״�� ����ϴ� ���� ���մϴ�.					//
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
    //tr_cert ������ ���� ���� ---------------------------------------------------------------
	String tr_cert       = "";
    String cpId          = request.getParameter("cpId");        // ȸ����ID
    String urlCode       = request.getParameter("urlCode");     // URL�ڵ�
    String certNum       = request.getParameter("certNum");     // ��û��ȣ
    String date          = request.getParameter("date");        // ��û�Ͻ�
    String certMet       = request.getParameter("certMet");     // �����������
    String name          = request.getParameter("name");        // ����
    String phoneNo	     = request.getParameter("phoneNo");	    // �޴�����ȣ
    String phoneCorp     = request.getParameter("phoneCorp");   // �̵���Ż�
	if(phoneCorp == null) phoneCorp = "";
	String birthDay	     = request.getParameter("birthDay");	// �������
	String gender	     = request.getParameter("gender");		// ����
	if(gender == null) gender = "";
    String nation        = request.getParameter("nation");      // ���ܱ��� ����
	String plusInfo      = request.getParameter("plusInfo");	// �߰�DATA����
	String extendVar     = "0000000000000000";                  // Ȯ�庯��
    //End-tr_cert ������ ���� ���� ---------------------------------------------------------------

	String tr_url     = request.getParameter("tr_url");         // ������������ ������� POPUP URL
	String tr_add     = request.getParameter("tr_add");         // IFrame��뿩��

	/** certNum ���ǻ��� **************************************************************************************
	* 1. �������� ����� ��ȣȭ�� ���� Ű�� Ȱ��ǹǷ� �߿���.
	* 2. �������� ��û�� �ߺ����� �ʰ� �����ؾ���. (��-��������ȣ)
	* 3. certNum���� �������� ����� ���� �� ��ȣȭŰ�� �����.
	       tr_url���� certNum���� �����ؼ� �����ϰ�, (tr_url = tr_url + "?certNum=" + certNum;)
		   tr_url���� ������Ʈ�� ���·� certNum���� ������ ��. (certNum = request.getParameter("certNum"); )
	*
	***********************************************************************************************************/
%>
<%!
   // �Ķ���� ��ȿ�� ���� --------------------------------------------
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
			out.println("<script> alert('ȸ����ID ������');");
			out.println("history.back(); </script>");
		}
	} else {
		out.println("<script> alert('ȸ����ID ������');");
		out.println("history.back(); </script>");
	}
	*/
	
	/*
	regex = "[0-9]*";
	if( urlCode.length() != 6 || !paramChk(regex, urlCode) ){
		out.println("<script> alert('URL�ڵ� ������');");
		out.println("history.back(); </script>");
	}
	*/

	/*
	if( certNum.length() == 0 || certNum.length() > 40){
		out.println("<script> alert('��û��ȣ�� �Է��ϼ���.');");
		out.println("history.back(); </script>");
	}
	*/

	/*
	regex = "[0-9]*";
	if( date.length() != 14 || !paramChk(regex, date) ){
		out.println("<script> alert('��û�Ͻ� ������');");
		out.println("history.back(); </script>");
	}
	*/

	/*
	regex = "[A-Z]*";
	if( certMet.length() != 1 || !paramChk(regex, certMet) ){
		out.println("<script> alert('����������� ������');");
		out.println("history.back(); </script>");
	}
	*/
	
	/*
	regex = "[0-9]*";
	if( phoneNo.length() != 0 ){
		if( (phoneNo.length() != 10 && phoneNo.length() != 11) || !paramChk(regex, phoneNo) ){
			out.println("<script> alert('�޴�����ȣ ������');");
			out.println("history.back(); </script>");
		}
	}
	*/

	/*
	regex = "[A-Z]*";
	if( phoneCorp.length() != 0 ){
		if( phoneCorp.length() != 3 || !paramChk(regex, phoneCorp) ){
			out.println("<script> alert('�̵���Ż� ������');");
			out.println("history.back(); </script>");
		}
	}
	*/

	/*
	regex = "[0-9]*";
	if( birthDay.length() != 0 ){
		if( birthDay.length() != 8 || !paramChk(regex, birthDay) ){
			out.println("<script> alert('������� ������');");
			out.println("history.back(); </script>");
		}
	}
	*/
	
	/*
	regex = "[0-9]*";
	if( gender.length() != 0 ){
		if( gender.length() != 1 || !paramChk(regex, gender) ){
			out.println("<script> alert('���� ������');");
			out.println("history.back(); </script>");
		}
	}
	*/
	
	/*
	regex = "[0-9]*";
	if( nation.length() != 0 ){
		if( nation.length() != 1 || !paramChk(regex, nation) ){
			out.println("<script> alert('��/�ܱ��� ������');");
			out.println("history.back(); </script>");
		}
	}
	*/
	
	/*
	regex = "[\\sA-Za-z��-�R.,-]*";
	if( name.length() != 0 ){
		if( name.length() > 60 || !paramChk(regex, name) ){
			out.println("<script> alert('���� ������');");
			out.println("history.back(); </script>");
		}
	}
	*/
	
	/*
	if( tr_url.length() == 0 ){
		out.println("<script> alert('������� URL ������');");
		out.println("history.back(); </script>");
	}
	*/
	
	/*
	regex = "[A-Z]*";
	if( tr_add.length() != 0 ){
		if( tr_add.length() != 1 || !paramChk(regex, tr_add) ){
			out.println("<script> alert('IFrame��뿩�� ������');");
			out.println("history.back(); </script>");
		}
	}
	*/

	// END �Ķ���� ��ȿ�� ���� --------------------------------------------

    //01. �ѱ����������(��) ��ȣȭ ��� ����
   com.icert.comm.secu.IcertSecuManager seed  = new com.icert.comm.secu.IcertSecuManager();

	//02. 1�� ��ȣȭ (tr_cert �����ͺ��� ���� �� ��ȣȭ)
	String enc_tr_cert = "";
	tr_cert            = cpId +"/"+ urlCode +"/"+ certNum +"/"+ date +"/"+ certMet +"/"+ birthDay +"/"+ gender +"/"+ name +"/"+ phoneNo +"/"+ phoneCorp +"/"+ nation +"/"+ plusInfo +"/"+ extendVar;
	enc_tr_cert        = seed.getEnc(tr_cert, "");

	//03. 1�� ��ȣȭ �����Ϳ� ���� ������ ������ ���� (HMAC)
	String hmacMsg = "";
	hmacMsg = seed.getMsg(enc_tr_cert);

	//04. 2�� ��ȣȭ (1�� ��ȣȭ ������, HMAC ������, extendVar ���� �� ��ȣȭ)
	tr_cert  = seed.getEnc(enc_tr_cert + "/" + hmacMsg + "/" + extendVar, "");
%>

<html>
<head>
<title>������������ Sample ȭ��</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<meta name="robots" content="noindex">
<style>
<!--
   body,p,ol,ul,td
   {
	 font-family: ����;
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
    /* ����� ���� üũ*/
    // ������� ��� (�������� ������� �߰� �ʿ�)
    if (UserAgent.match(/iPhone|iPod|Android|Windows CE|BlackBerry|Symbian|Windows Phone|webOS|Opera Mini|Opera Mobi|POLARIS|IEMobile|lgtelecom|nokia|SonyEricsson/i) != null || UserAgent.match(/LG|SAMSUNG|Samsung/) != null) {
   		 document.reqKMCISForm.target = '';
	  } 
	  
	  // ������� �ƴ� ���
	  else {
   		KMCIS_window = window.open('', 'KMCISWindow', 'width=425, height=550, resizable=0, scrollbars=no, status=0, titlebar=0, toolbar=0, left=435, top=250' );
   		
   		if(KMCIS_window == null){
  			alert(" �� ������ XP SP2 �Ǵ� ���ͳ� �ͽ��÷η� 7 ������� ��쿡�� \n    ȭ�� ��ܿ� �ִ� �˾� ���� �˸����� Ŭ���Ͽ� �˾��� ����� �ֽñ� �ٶ��ϴ�. \n\n�� MSN,����,���� �˾� ���� ���ٰ� ��ġ�� ��� �˾������ ���ֽñ� �ٶ��ϴ�.");
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
<span class="style1">������������ ��ûȭ�� Sample�Դϴ�.</span><br>
<br><br>
<table cellpadding=1 cellspacing=1>
    <tr>
        <td align=center>ȸ����ID</td>
        <td align=left><%=cpId%></td>
    </tr>
    <tr>
        <td align=center>URL�ڵ�</td>
        <td align=left><%=urlCode%></td>
    </tr>
    <tr>
        <td align=center>��û��ȣ</td>
        <td align=left><%=certNum%></td>
    </tr>
    <tr>
        <td align=center>��û�Ͻ�</td>
        <td align=left><%=date%></td>
    </tr>
    <tr>
        <td align=center>�����������</td>
        <td align=left><%=certMet%></td>
        </td>
    </tr>
    <tr>
        <td align=center>�̿��ڼ���</td>
        <td align=left><%=name%></td>
    </tr>
    <tr>
        <td align=center>�޴�����ȣ</td>
        <td align=left><%=phoneNo%></td>
    </tr>
    <tr>
        <td align=center>�̵���Ż�</td>
        <td align=left><%=phoneCorp%></td>
    </tr>
    <tr>
        <td align=center>�������</td>
		<td align=left><%=birthDay%></td>
    </tr>
	<tr>
        <td align=center>�̿��ڼ���</td>
        <td align=left><%=gender%></td>
    </tr>
    <tr>
        <td align=center>���ܱ���</td>
        <td align=left><%=nation%></td>
    </tr>
    <tr>
        <td align=center>�߰�DATA����</td>
        <td align=left><%=plusInfo%></td>
        </td>
    </tr>
    <tr>
        <td align=center>&nbsp</td>
        <td align=left>&nbsp</td>
    </tr>
    <tr width=100>
        <td align=center>��û����(��ȣȭ)</td>
        <td align=left>
            <%=tr_cert.substring(0,50)%>...
        </td>
    </tr>
    <tr>
        <td align=center>�������URL</td>
        <td align=left><%=tr_url%></td>
    </tr>
    <tr>
        <td align=center>IFrame��뿩��</td>
        <td align=left><%=tr_add%></td>
    </tr>	
</table>

<!-- ������������ ��û form --------------------------->
<form name="reqKMCISForm" method="post" action="#">
    <input type="hidden" name="tr_cert"     value = "<%=tr_cert%>">
    <input type="hidden" name="tr_url"      value = "<%=tr_url%>">
    <input type="hidden" name="tr_add"      value = "<%=tr_add%>">	
    <input type="submit" value="������������ ��û"  onclick= "javascript:openKMCISWindow();">
</form>
<BR>
<BR>
<!--End ������������ ��û form ----------------------->

<br>
<br>
  �� Sampleȭ���� ������������ ��ûȭ�� ���߽� ���� �ǵ��� �����ϰ� �ִ� ȭ���Դϴ�.<br>
<br>
</center>
</BODY>
</HTML>