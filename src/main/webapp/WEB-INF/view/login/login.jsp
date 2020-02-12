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
                                    <label for="memType" class="col-md-4 col-form-label text-md-right">memType</label>
                                    <div class="col-md-6">
                                        <input type="text" name="memType" id="memType" class="form-control" autofocus value="p" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="memId" class="col-md-4 col-form-label text-md-right">memId</label>
                                    <div class="col-md-6">
                                        <!--<input type="text" name="memId" id="memId" class="form-control" value="010-1234-4568" />-->
                                        <select name="memId" id="memId" class="form-control"></select>
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="memId" class="col-md-4 col-form-label text-md-right">memPwd</label>
                                    <div class="col-md-6">
                                        <input type="text" name="memPwd" id="memPwd" class="form-control" value="123qweasd" />
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
                                        <input type="text" name="deviceId" id="deviceId" class="form-control" value="2200DDD1-77A" />
                                    </div>
                                </div>

                                <div class="form-group row">
                                    <label for="deviceToken" class="col-md-4 col-form-label text-md-right">deviceToken</label>
                                    <div class="col-md-6">
                                        <input type="text" name="deviceToken" id="deviceToken" class="form-control" value="45E3156FDE20E7F11AF" />
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
                                        <input type="text" name="appAdId" id="appAdId" class="form-control" value="asd123asdas1" />
                                    </div>
                                </div>

                                <div class="col-md-6 offset-md-4">
                                    <button type="button" id="loginBtn" class="btn btn-primary">
                                        LOGIN
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
                <div class="col-md-8">
                        <div class="card">
                            <div class="card-header">방송종료</div>
                            <div class="card-body">
                                <select id="deleteRoomNo" class="castList" class="form-control">
                                </select>
                                <button type="button" id="exitRoom" class="btn btn-primary">
                                    종료
                                </button>
                                <button type="button" class="btn btn-primary btnRefresh">
                                    다시가져오기
                                </button>
                            </div>
                        </div>
                </div>
                <div class="col-md-8" style="display:inline-block;">
                    <div class="card">
                        <div class="card-header">소켓통신 챗</div>
                        <div class="card-body">
                            <select id="socketRoomNo" class="castList" class="form-control">
                            </select>
                            <input type="text" />
                            <button type="button" id="socketRoom" class="btn btn-primary">
                                참여
                            </button>
                            <button type="button" class="btn btn-primary btnRefresh">
                                다시가져오기
                            </button>
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
        var authToken = "";
        //Fade in dashboard box
        $(document).ready(function(){
            $('.box').hide().fadeIn(1000);
            $.ajax({
                type: "GET"
                , url : "/token"
                , dataType : "json"
                , data : {
                    os : 3
                }
                , success : function(response){
                    authToken = response.data.authToken;
                    fnLoadCast();
                    fnSelectMemId();
                }
            });
        });

        //Stop click event
        $('a').click(function(event){
            event.preventDefault();
        });

        $('#loginBtn').on('click', function(){
            ajaxLogin();
        });

        $(".btnRefresh").on("click", function(){
            fnLoadCast();
        });

        $("#exitRoom").on("click", function(){
            var value = $("#deleteRoomNo").val();
            if(value != undefined && value != null && value != "" && confirm("방종료 하시겠습니까?")){
                $.ajax({
                    type: "DELETE"
                    , url : "/broad/exitForce"
                    , dataType : "json"
                    , data : {
                        roomNo : value.split("|")[0]
                        , memNo : value.split("|")[1]
                    }
                    , success : function(response) {
                        alert(response.message);
                        if (response.result == 'success') {
                            fnLoadCast();
                        }else{
                            console.info(response);
                        }
                    }
                });
            }
        })

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
                        authToken=response.data.authToken;
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

        function fnLoadCast(){
            $.ajax({
                type: "GET"
                , url : "/broad/list"
                , dataType : "json"
                , data : {
                    page : 1
                    , records : 100
                }
                , beforeSend : function(xhr){
                    xhr.setRequestHeader("authToken", authToken);
                }
                , success : function(response){
                    if(response.result == 'success' && response.data.list != undefined){
                        $(".castList").html("");
                        var html = "";
                        for(var i = 0; i < response.data.list.length; i++){
                            html += "<option value=\"" + response.data.list[i].roomNo + "|" + response.data.list[i].bjMemNo + "\">";
                            html += response.data.list[i].roomNo;
                            html += " | " + response.data.list[i].title;
                            html += " | " + response.data.list[i].startDt;
                            html += "</option>";
                        }
                        $(".castList").html(html);
                    }
                }
            });
        }

        function fnSelectMemId(){
            $.ajax({
                type: "GET"
                , url : "/id"
                , dataType : "json"
                , success : function(response){
                    console.log(response)
                    $("#memId").html("");
                    var html = "";
                    for(var i = 0; i < response.length; i++){
                        html += "<option value="+response[i].mem_id+">"+response[i].id_info+"</option>";
                    }
                    $("#memId").html(html);

                }
            });
        }
    </script>
</body>
</html>
