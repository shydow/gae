<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>

<title>Word Cloud</title>

<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<script src="js/jquery.tagcanvas.min.js" type="text/javascript"></script>
<script src="js/tagcanvas.min.js" type="text/javascript"></script>
<script src="js/jquery-1.6.1.min.js" type="text/javascript"></script>
<script type="text/javascript">
        var gradient = {
	    		 0:    '#99333E',
	    		 0.25: '#CF633D',
	    		 0.5: '#E89745', 
				 0.75:  '#F7E7AB',
	    		 1:    '#61A179' 
     		};
	    window.onload = function() {
			TagCanvas.interval = 20;
			TagCanvas.textFont = 'Impact' ; //'Impact,Arial Black,sans-serif';
			//TagCanvas.textColour = '#00f';
			TagCanvas.textHeight = 30;
			TagCanvas.outlineColour = '#f96';
			TagCanvas.outlineThickness = 5;
			TagCanvas.maxSpeed = 0.04;
			TagCanvas.maxBrightness = 1;
			TagCanvas.minBrightness = 0.6;
			TagCanvas.depth = 0.9; //深度
			TagCanvas.pulsateTo = 0.2;
			TagCanvas.pulsateTime = 0.75;
			TagCanvas.initial = [0.1,-0.1];
			TagCanvas.decel = 0.98;
			TagCanvas.reverse = true;
			TagCanvas.shadow = '#ccf';
			TagCanvas.shadowBlur = 2;
			TagCanvas.weight = true;
			TagCanvas.weightFrom = 'data-weight';
			TagCanvas.weightMode = 'both';  //变色
			TagCanvas.weightGradient = gradient;
		 try {
			 TagCanvas.Start('myCanvas','tags');
		 } catch(e) {
		 }
		
		};
	</script>
</head>

<body style="background-color: #DBDBDB;">
	<div id="myCanvasContainer" align="center">
		<canvas width="1000" height="500" id="myCanvas">
		<p>浏览器不支持！</p>
		</canvas>
	</div>
	<div id="tags">
		<ul id="menu" style="font-size: 50%">
			<c:forEach items="${hotWords}" var="entry">
				<li><a data-weight='${entry.value}' href='#'>${entry.key}</a>
			</c:forEach>
		</ul>
	</div>
</body>
</html>
