<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<body>
	<h1>All Posts:</h1>

	<p>
		<c:forEach items="${posts }" var="post">
		↓↓↓↓↓↓↓↓↓↓<a style="" href="${post.itemUrl}">查看原po</a>↓↓↓↓↓↓↓↓↓↓<br>
 		${post.content} 
 		<hr>
		</c:forEach>
	</p>

</body>
</html>