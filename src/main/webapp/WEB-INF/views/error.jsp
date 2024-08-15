<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>error</title>

    <script type="text/javascript">
        <c:if test="${not empty message}">
        alert("${message}");
        location.href = ${url};
        </c:if>
    </script>
</head>
</html>