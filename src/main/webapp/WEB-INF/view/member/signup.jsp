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
                        <div class="card-header">회원가입</div>
                        <div class="card-body">
                            <form id="frm" method="post">
                                <div class="form-group row">
                                    <label for="memType" class="col-md-4 col-form-label text-md-right">memType</label>
                                    <div class="col-md-6">
                                        <input type="text" name="memType" id="memType" class="form-control" autofocus value="p" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="memId" class="col-md-4 col-form-label text-md-right">memId</label>
                                    <div class="col-md-6">
                                        <input type="text" name="memId" id="memId" class="form-control" value="010-1234-4568" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="memId" class="col-md-4 col-form-label text-md-right">memPwd</label>
                                    <div class="col-md-6">
                                        <input type="text" name="memPwd" id="memPwd" class="form-control" value="1234" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="gender" class="col-md-4 col-form-label text-md-right">gender</label>
                                    <div class="col-md-6">
                                        <input type="text" name="gender" id="gender" class="form-control" value="m" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="nickNm" class="col-md-4 col-form-label text-md-right">nickNm</label>
                                    <div class="col-md-6">
                                        <input type="text" name="nickNm" id="nickNm" class="form-control" value="닉네임" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="birthYY" class="col-md-4 col-form-label text-md-right">birthYY</label>
                                    <div class="col-md-6">
                                        <input type="text" name="birthYY" id="birthYY" class="form-control" value="1999" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="birthMM" class="col-md-4 col-form-label text-md-right">birthMM</label>
                                    <div class="col-md-6">
                                        <input type="text" name="birthMM" id="birthMM" class="form-control" value="11" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="birthDD" class="col-md-4 col-form-label text-md-right">birthDD</label>
                                    <div class="col-md-6">
                                        <input type="text" name="birthDD" id="birthDD" class="form-control" value="22" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="term1" class="col-md-4 col-form-label text-md-right">term1</label>
                                    <div class="col-md-6">
                                        <input type="text" name="term1" id="term1" class="form-control" value="y" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="term2" class="col-md-4 col-form-label text-md-right">term2</label>
                                    <div class="col-md-6">
                                        <input type="text" name="term2" id="term2" class="form-control" value="y" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="term3" class="col-md-4 col-form-label text-md-right">term3</label>
                                    <div class="col-md-6">
                                        <input type="text" name="term3" id="term3" class="form-control" value="y" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="name" class="col-md-4 col-form-label text-md-right">name</label>
                                    <div class="col-md-6">
                                        <input type="text" name="name" id="name" class="form-control" value="테스트" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="profImg" class="col-md-4 col-form-label text-md-right">profImg</label>
                                    <div class="col-md-6">
                                        <input type="text" name="profImg" id="profImg" class="form-control" value="/2020/01/03/profile.png" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="profImgRacy" class="col-md-4 col-form-label text-md-right">profImgRacy</label>
                                    <div class="col-md-6">
                                        <input type="text" name="profImgRacy" id="profImgRacy" class="form-control" value="3" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="email" class="col-md-4 col-form-label text-md-right">email</label>
                                    <div class="col-md-6">
                                        <input type="text" name="email" id="email" class="form-control" value="test@inforex.co.kr" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="os" class="col-md-4 col-form-label text-md-right">os</label>
                                    <div class="col-md-6">
                                        <input type="text" name="os" id="os" class="form-control" value="1" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="deviceId" class="col-md-4 col-form-label text-md-right">deviceId</label>
                                    <div class="col-md-6">
                                        <input type="text" name="deviceId" id="deviceId" class="form-control" value="2200DDD1-11C" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="deviceToken" class="col-md-4 col-form-label text-md-right">deviceToken</label>
                                    <div class="col-md-6">
                                        <input type="text" name="deviceToken" id="deviceToken" class="form-control" value="15E3156FDE20E7F11AF" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="appVer" class="col-md-4 col-form-label text-md-right">appVer</label>
                                    <div class="col-md-6">
                                        <input type="text" name="appVer" id="appVer" class="form-control" value="1.0.0.1" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="appAdId" class="col-md-4 col-form-label text-md-right">appAdId</label>
                                    <div class="col-md-6">
                                        <input type="text" name="appAdId" id="appAdId" class="form-control" value="ekdkaklfke11" />
                                    </div>
                                </div>

                                <div class="col-md-6 offset-md-4">
                                    <button type="button" id="btn" class="btn btn-primary">
                                        회원가입
                                    </button>
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

    $('#btn').on('click', function(){
        fnAjaxSubmit();
    });

    function fnAjaxSubmit(){
        $.ajax({
            type: "POST",
            url: "/member/signup",
            dataType: "json",
            data: $("#frm").serialize(),
            success: function (response) {
                console.log(response);
                if(response.result == 'success'){
                    alert(response.message);

                }else if(response.result == 'fail'){
                    alert(response.message);
                }
            }, error: function (xhr, textStatus) {
                alert(xhr.status + "회원가입 중 오류가 발생했습니다.");
            }
        });
    }
</script>
</body>
</html>
