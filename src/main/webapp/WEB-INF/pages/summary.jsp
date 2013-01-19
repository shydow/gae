<%@ page language="java" pageEncoding="UTF-8" session="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<body>
	<section class="me">
		<h3>Your Profile</h3>
		<dl>
			<dt>Image</dt>
			<dd>
				<a href="${user.gplusUrl}"> <img
					src="${user.gplusImageUrl}" /></a>
			</dd>
			<dt>ID</dt>
			<dd>${user.id }
			</dd>
			<dt>Name</dt>
			<dd>
				<a href="${user.gplusUrl}"> ${user.gplusDisplayName}
				</a>
			</dd>
		</dl>
	</section>
	
	<hr>
	<a href="main?profileid=${user.id }">get your post!</a> | <a href="words?profileid=${user.id}">Show Your Word Cloud</a> | <a href="trendency?profileid=${user.id}">Show Your Trendency</a> 
</body>
</html>