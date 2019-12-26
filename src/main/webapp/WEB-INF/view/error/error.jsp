<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Spring Boot Application with JSP</title></head>
<body>
    <div>
        <h1>Error Page</h1>
        error code : <span th:text="${code}"></span>
        <br>error msg : <span th:text="${msg}"></span>
        <br>timestamp : <span th:text="${timestamp}"></span>
    </div>
</body>
</html>
