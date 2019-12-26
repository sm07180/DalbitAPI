<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
    <script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
    <script>
        function submit() {
            var sel = document.getElementById("fileType");
            var uploadFileType = sel.options[sel.selectedIndex].value;

            var formData = new FormData();

            if (uploadFileType == "file") {
                alert(uploadFileType)
                var files = $('#files')[0].files;
                for (var i = 0; i < files.length; ++i) {
                    console.log(files[i]);
                    formData.append('files', files[i]);
                }
                ajaxUploadFile(formData);
            } else if (uploadFileType == "dataurl") {
                alert(uploadFileType + "1");
                var reader = new FileReader();
                reader.readAsDataURL($('#files')[0].files[0]);

                reader.onload = function () {
                    console.log(reader.result);
                    formData.append('files', reader.result);
                    console.log("################");
                    console.log(formData);
                    ajaxUploadFile(formData);
                }
            } else if (uploadFileType == "api") {
                var files = $('#files')[0].files;
                for (var i = 0; i < files.length; ++i) {
                    console.log(files[i]);
                    formData.append('file', files[i]);
                }
                ajaxforKaKao(formData);
            }

        }

        function ajaxUploadFile(formData) {
            $.ajax({
                url: '/files/temp',
                method: 'POST',
                processData: false,
                contentType: false,
                data: formData,
                success: function (d) {
                    console.log('success');
                    console.log(d);
                    if (d.result == "success") {
                        document.getElementById("imgViewer1_URL").innerHTML = d.fileTempName;
                        // document.getElementById("imgViewer1").setAttribute("src", "http://localhost:80" + d.fileTempName);
                    } else {
                        console.log(d.consoleMsg);
                        alert(d.msg);
                    }
                },
                error: function (e) {
                    console.log(e);
                    alert("error : " + e);
                }
            });
        }


        function ajaxforKaKao(formData) {
            $.ajax({
                url: 'https://kapi.kakao.com/v1/vision/adult/detect',
                method: 'POST',
                processData: false,
                contentType: false,
                mimeType: "multipart/form-data",
                headers: {
                    "Authorization": "KakaoAK a37b19fcae2092b1d1a2dac1a5b84c1f",
                },
                data: formData,
                success: function (result) {
                    console.log(result);
                    alert("성공");
                },
                error: function (e) {
                    console.log("Erorr:");
                    console.log(e);
                    alert("error : " + e);
                }
            });
        }


        function getImg() {
            var imgUrl = document.getElementById("imgUrl").value;
            document.getElementById("imgViewer2").setAttribute("src", imgUrl);
        }

        function getImg1() {
            var imgUrl = document.getElementById("imgUrl1").value;
            document.getElementById("imgViewer3").setAttribute("src", imgUrl);
        }

        window.onload = function () {
            document.getElementById("imgUrl").value = window.location.href;
            document.getElementById("imgUrl1").value = window.location.href;
        }

        function imageNotFound(me){
            //this.src='https://localhost/img/common/default.png?45x45'
            console.log(me.prop('src', '/img/common/default.png'));
        }
    </script>
</head>

<body>
<input id="files" type="file" onchange="submit()">
<select id="fileType">
    <option value="file">MultiPartFile</option>
    <option value="dataurl">DataURL</option>
</select>
<p></p>

<br>
<img id="imgViewer1" src="" alt=""
     onerror="imageNotFound($(this))">
<p id="imgViewer1_URL"></p>
<br>
Szie: <input id="imgUrl" type="text" style="width: 500px">
<button onclick="getImg()">Get Image</button>
<br>
<img id="imgViewer2" src="" alt=""
     onerror="imageNotFound($(this))">
<br>
Szie: <input id="imgUrl1" type="text" style="width: 500px">
<button onclick="getImg1()">Get Image</button>
<br>
<img id="imgViewer3" src="" alt=""
     onerror="imageNotFound($(this))">
</body>
</html>