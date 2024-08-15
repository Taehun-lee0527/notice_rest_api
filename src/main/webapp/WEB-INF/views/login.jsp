<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>login</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" />
    <script src="${pageContext.request.contextPath}/js/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>

    <script type="text/javascript">
        // 메시지가 있을 경우 alert로 표시
        <c:if test="${not empty message}">
        alert("${message}");
        </c:if>
    </script>
</head>
<body>
<div class="container">
    <form action="/login" method="post">
        <div class="mb-3">
            <label class="form-label" for="loginId">아이디</label>
            <input class="form-control" type="text" name="loginId" id="loginId" />
        </div>
        <div class="mb-3">
            <label class="form-label" for="password">비밀번호</label>
            <input class="form-control" type="password" name="password" id="password" />
        </div>
        <c:if test="${not empty param.error}">
            <p style="color: red;">로그인에 실패하였습니다. 다시 시도해주세요.</p>
        </c:if>
        <div class="d-grid gap-2 d-md-flex justify-content-md-end">
            <a class="btn btn-outline-primary btn-sm" href="/join">회원가입</a>
            <button class="btn btn-outline-primary btn-sm" type="submit" value="Login">로그인</button>
        </div>
    </form>
</div>
</body>
</html>