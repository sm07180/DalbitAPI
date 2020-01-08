<%--
  Created by IntelliJ IDEA.
  User: USER
  Date: 2020-01-08
  Time: 오후 1:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Rest Test</title>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script type="text/javascript">
        function fnImg(frm){
            if($("#tmpImg").val().trim() == ""){
                alert("이동할 이미지 경로 입력");
                $("#tmpImg").focus();
            }else{
                $.ajax({
                    url: $(frm).attr("action"),
                    type: $(frm).attr("method"),
                    data: $(frm).serialize(),
                    dataType : "json",
                    success: function (d) {
                        console.log(d);
                        $(frm).find("ul li:last-child").html(JSON.stringify(d));
                    },
                    error: function (e) {
                        console.log(e);
                        alert("error : " + e);
                    }
                });
            }
            return false;
        }
        function fnBroad(frm){
            if($("#roomNm").val().trim() == ""){
                alert("방송방 명 입력");
                $("#roomNm").focus();
            }else{
                $.ajax({
                    url: $(frm).attr("action"),
                    type: $(frm).attr("method"),
                    data: $(frm).serialize(),
                    dataType : "json",
                    success: function (d) {
                        console.log(d);
                        $(frm).find("ul li:last-child").html(JSON.stringify(d));
                    },
                    error: function (e) {
                        console.log(e);
                        alert("error : " + e);
                    }
                });
            }
            return false;
        }
    </script>
</head>
<body>
    <h2>포토서버 이동</h2>
    <div>
        <form name="frmImg" id="frmImg" action="/ex/rest/img" method="post" style="margin: 0; padding: 0;">
            <ul style="margin: 0;padding: 0; list-style: none;">
                <li>
                    <label for="tmpImg">이동할 이미지 경로</label>
                    <input type="text" name="tmpImg" id="tmpImg">
                </li>
                <li>
                    <label for="delImg">삭제할 이미지 경로</label>
                    <input type="text" name="delImg" id="delImg">
                </li>
                <li>
                </li>
            </ul>
            <button type="button" onclick="fnImg(this.form);">전송</button>
        </form>
    </div>
    <hr />
    <h2>Ant Media Room Create</h2>
    <div>
        <form name="frmBrod" id="frmBrod" action="/ex/rest/broad" method="post" style="margin: 0; padding: 0;">
            <ul style="margin: 0;padding: 0; list-style: none;">
                <li>
                    <label for="roomNm">방송방 명</label>
                    <input type="text" name="roomNm" id="roomNm">
                </li>
                <li>
                </li>
            </ul>
            <button type="button" onclick="fnBroad(this.form);">전송</button>
        </form>
    </div>
</body>
</html>
