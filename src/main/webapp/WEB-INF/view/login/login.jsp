<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
</head>
<body>

    <main class="login-form">
        <div class="cotainer">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header">로그인</div>
                        <div class="card-body">
                            <form id="loginFrm" method="post">
                                <div class="form-group row">
                                    <label for="s_mem" class="col-md-4 col-form-label text-md-right">s_mem</label>
                                    <div class="col-md-6">
                                        <input type="text" name="s_mem" id="s_mem" class="form-control" autofocus value="p" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="s_id" class="col-md-4 col-form-label text-md-right">s_id</label>
                                    <div class="col-md-6">
                                        <input type="text" name="s_id" id="s_id" class="form-control" value="010-1234-4568" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="s_id" class="col-md-4 col-form-label text-md-right">s_pwd</label>
                                    <div class="col-md-6">
                                        <input type="text" name="s_pwd" id="s_pwd" class="form-control" value="1234" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="i_os" class="col-md-4 col-form-label text-md-right">i_os</label>
                                    <div class="col-md-6">
                                        <input type="text" name="i_os" id="i_os" class="form-control" value="1" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="s_deviceId" class="col-md-4 col-form-label text-md-right">s_deviceId</label>
                                    <div class="col-md-6">
                                        <input type="text" name="s_deviceId" id="s_deviceId" class="form-control" value="2200DDD1-77A" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="s_deviceToken" class="col-md-4 col-form-label text-md-right">s_deviceToken</label>
                                    <div class="col-md-6">
                                        <input type="text" name="s_deviceToken" id="s_deviceToken" class="form-control" value="45E3156FDE20E7F11AF" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="s_appVer" class="col-md-4 col-form-label text-md-right">s_appVer</label>
                                    <div class="col-md-6">
                                        <input type="text" name="s_appVer" id="s_appVer" class="form-control" value="1.0.0.1" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="s_appAdId" class="col-md-4 col-form-label text-md-right">s_appVer</label>
                                    <div class="col-md-6">
                                        <input type="text" name="s_appAdId" id="s_appAdId" class="form-control" value="asd123asdas1" />
                                    </div>
                                </div>

                                <div class="col-md-6 offset-md-4">
                                    <button type="button" id="loginBtn" class="btn btn-primary">
                                        LOGIN
                                    </button>
                                </div>
                        </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
        </div>

    </main>

    <script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
    <script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
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
