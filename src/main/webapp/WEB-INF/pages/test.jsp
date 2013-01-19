<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

</head>

<body style="background-color: #DBDBDB;">
	<c:forEach items="${map}" var="entry">
		${entry.key}:${entry.value} <br>
	</c:forEach>
</body>
</html>
