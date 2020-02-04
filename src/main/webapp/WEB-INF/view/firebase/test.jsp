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
                    <div class="card-header">동적링크 테스트</div>
                    <div class="card-body">
                        <form id="frm" method="post">
                            <div class="form-group row">
                                <label for="memType" class="col-md-4 col-form-label text-md-right">shareLink</label>
                                <div class="col-md-6">
                                    <input type="text" name="memType" id="memType" class="form-control" autofocus value="https://testdalbit.page.link/test" />
                                </div>
                            </div>
                            <div class="col-md-6 offset-md-4">
                                <button type="button" id="btn" class="btn btn-primary">
                                    이동
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
        location.href = "https://testdalbit.page.link/test";
    });
</script>
</body>
</html>
