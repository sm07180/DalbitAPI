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
                        <div class="card-body">
                            <form id="tokenCheckFrm" method="post">
                                <div class="form-group row">
                                    <label for="authToken" class="col-md-4 col-form-label text-md-right">authToken</label>
                                    <div class="col-md-6">
                                        <input type="text" name="authToken" id="authToken" class="form-control" autofocus value="" />
                                    </div>
                                </div>
                                <div class="col-md-6 offset-md-4">
                                    <button type="button" id="tokenCheckBtn" class="btn btn-primary">토큰검증</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header">authToken 체크</div>
                        <div class="card-body">
                            <div class="form-group row">
                                <label for="authToken" class="col-md-4 col-form-label text-md-right">응답 결과</label>
                                <div class="col-md-6" id="r_tokenMessage">-</div>
                            </div>
                            <div class="form-group row">
                                <label for="authToken" class="col-md-4 col-form-label text-md-right">신규토큰</label>
                                <div class="col-md-6" id="r_authToken">-</div>
                            </div>
                            <div class="form-group row">
                                <label for="authToken" class="col-md-4 col-form-label text-md-right">회원번호</label>
                                <div class="col-md-6" id="r_memNo">-</div>
                            </div>
                            <div class="form-group row">
                                <label for="authToken" class="col-md-4 col-form-label text-md-right">로그인여부</label>
                                <div class="col-md-6" id="r_isLogin">-</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </main>

    <main class="login-form">
        <div class="cotainer">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-header">authToken으로 Redis 정보 조회</div>
                        <div class="card-body">
                            <div class="form-group row">
                                <label for="authToken" class="col-md-4 col-form-label text-md-right">응답 결과</label>
                                <div class="col-md-6" id="r_getRedis_tokenMessage">-</div>
                            </div>
                            <div class="form-group row">
                                <label for="authToken" class="col-md-4 col-form-label text-md-right">회원번호</label>
                                <div class="col-md-6" id="r_getRedis_memNo">-</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <hr />

    <main class="login-form">
        <div class="cotainer">
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card">
                        <div class="card-body">
                            <form id="searchMemFrm" method="post">
                                <div class="form-group row">
                                    <label for="memNo" class="col-md-4 col-form-label text-md-right">회원번호</label>
                                    <div class="col-md-4">
                                        <input type="text" name="memNo" id="memNo" class="form-control" value="" />
                                    </div>
                                </div>
                                <div class="col-md-4 offset-md-4">
                                    <button type="button" id="searchMemBtn" class="btn btn-primary">조회</button>
                                </div>
                            </form>
                        </div>
                    </div>

                    <div class="card">
                        <div class="card-header">회원번호로 로그인 체크</div>
                        <div class="card-body">
                            <div class="form-group row">
                                <label for="authToken" class="col-md-4 col-form-label text-md-right">응답 결과</label>
                                <div class="col-md-6" id="r_getRedis_tokenMessage">-</div>
                            </div>
                            <div class="form-group row">
                                <label for="authToken" class="col-md-4 col-form-label text-md-right">회원번호</label>
                                <div class="col-md-6" id="r_getRedis_memNo">-</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>

<script src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script type="text/javascript">

    //토큰체크
    $('#tokenCheckBtn').on('click', function(){
        fnTokenCheck();
        fnGetRedisData();
    });

    function isEmpty(value){
        return !value ? true : false;
    }

    function isFail(response){
        return response.result == 'fail';
    }

    function fnTokenCheck(){
        $.ajax({
            type: "POST",
            url: "/rest/sample/tokenCheck",
            dataType: "json",
            data: $("#tokenCheckFrm").serialize(),
            success: function (response) {
                console.log(response);

                var failMsg = '';
                if(isFail(response)){
                    failMsg = '<br />' + response.data;
                }
                $("#r_tokenMessage").html(response.message + failMsg);

                var data = response.data;
                $("#r_authToken").html(isFail(response) ? '' : data.authToken);
                $("#r_memNo").html(isFail(response) ? '' : data.memNo);

                if(isFail(response)){
                    $("#r_isLogin").html('');
                }else{
                    $("#r_isLogin").html(data.isLogin ? '로그인' : '비로그인');
                }


            }, error: function (xhr, textStatus) {
                console.log(xhr, textStatus);
            }
        });
    }

    //토큰으로 레디스 데이터 조회
    $('#getRedisBtn').on('click', function(){
        fnGetRedisData();
    });

    function fnGetRedisData(){
        $.ajax({
            type: "POST",
            url: "/rest/sample/getRedisData",
            dataType: "json",
            data: $("#tokenCheckFrm").serialize(),
            success: function (response) {
                console.log(response);
                $("#r_getRedis_tokenMessage").html(response.message);
                $("#r_getRedis_memNo").html(response.data == null ? '레디스에 데이터가 없습니다.' : response.data.memNo);

            }, error: function (xhr, textStatus) {
                console.log(xhr, textStatus);
            }
        });
    }
</script>
</body>
</html>
