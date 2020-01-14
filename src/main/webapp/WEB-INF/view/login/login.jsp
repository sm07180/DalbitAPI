<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Spring Boot Application with JSP</title></head>
    <style>
        body{
            font-family: 'Open Sans', sans-serif;
            background:#3498db;
            margin: 0 auto 0 auto;
            width:100%;
            text-align:center;
            margin: 20px 0px 20px 0px;
        }

        p{
            font-size:12px;
            text-decoration: none;
            color:#ffffff;
        }

        h1{
            font-size:1.5em;
            color:#525252;
        }

        .box{
            background:white;
            width:300px;
            border-radius:6px;
            margin: 0 auto 0 auto;
            padding:0px 0px 70px 0px;
            border: #2980b9 4px solid;
        }

        .email{
            background:#ecf0f1;
            border: #ccc 1px solid;
            border-bottom: #ccc 2px solid;
            padding: 8px;
            width:250px;
            color:#AAAAAA;
            margin-top:10px;
            font-size:1em;
            border-radius:4px;
        }

        .password{
            border-radius:4px;
            background:#ecf0f1;
            border: #ccc 1px solid;
            padding: 8px;
            width:250px;
            font-size:1em;
        }

        .btn{
            background:#2ecc71;
            width:125px;
            padding-top:5px;
            padding-bottom:5px;
            color:white;
            border-radius:4px;
            border: #27ae60 1px solid;

            margin-top:20px;
            margin-bottom:20px;
            float:left;
            margin-left:16px;
            font-weight:800;
            font-size:0.8em;
        }

        .btn:hover{
            background:#2CC06B;
        }

        #btn2{
            float:left;
            background:#3498db;
            width:125px;  padding-top:5px;
            padding-bottom:5px;
            color:white;
            border-radius:4px;
            border: #2980b9 1px solid;

            margin-top:20px;
            margin-bottom:20px;
            margin-left:10px;
            font-weight:800;
            font-size:0.8em;
        }

        #btn2:hover{
            background:#3594D2;
        }
    </style>
<body>
    <link href='https://fonts.googleapis.com/css?family=Open+Sans:700,600' rel='stylesheet' type='text/css'>
    <form id="loginFrm" method="post">
        <div class="box">
            <h1>Dashboard</h1>
            <input type="text" name="s_mem" id="s_mem" value="p" class="email" />
            <input type="text" name="s_id" id="s_id" value="010-1234-4568" class="email" />
            <input type="text" name="s_pwd" id="s_pwd" value="1234" class="email" />
            <input type="text" name="i_os" id="i_os" value="1" class="email" />
            <input type="text" name="s_deviceId" id="s_deviceId" value="2200DDD1-77A" class="email" />
            <input type="text" name="s_deviceToken" id="s_deviceToken" value="45E3156FDE20E7F11AF" class="email" />
            <input type="text" name="s_appVer" id="s_appVer" value="1.0.0.1" class="email" />
            <input type="text" name="s_appAdId" id="s_appAdId" value="asd123asdas1" class="email" />

            <a href="javascript://" id="loginBtn"><div class="btn">Sign In</div></a> <!-- End Btn -->
            <a href="javascript://"><div id="btn2">Sign Up</div></a> <!-- End Btn2 -->
        </div> <!-- End Box -->
    </form>
    <p>Forgot your password? <u style="color:#f1c40f;">Click Here!</u></p>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.0/jquery.min.js" type="text/javascript"></script>
    <script type="text/javascript">
        //Fade in dashboard box
        $(document).ready(function(){
            $('.box').hide().fadeIn(1000);
        });

        //Stop click event
        $('a').click(function(event){
            event.preventDefault();
        });

        $('#loginBtn').on('click', function(){
            ajaxLogin();
        });

        function ajaxLogin(){
            $.ajax({
                type: "POST",
                url: "/member/login",
                dataType: "json",
                data: $("#loginFrm").serialize(),
                success: function (response) {
                    console.log(response);
                    if(response.result == 'success'){    //로그인성공
                        alert(response.message);
                        if(response.data.returnUrl != undefined){
                            location.href = response.data.returnUrl;
                        }
                    }else if(response.result == 'fail'){
                        alert(response.message);
                    }
                }, error: function (xhr, textStatus) {
                    alert(xhr.status + "로그인 중 오류가 발생했습니다.");
                }
            });
        }
    </script>
</body>
</html>
