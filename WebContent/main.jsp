<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<html>
<head>
<title>突发事件案例搜索引擎</title>
<meta http-equiv=Content-Type content="text/html; charset=utf-8">
<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/engine.js"></script>
<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/util.js"></script>
<script type='text/javascript' src="${pageContext.request.contextPath}/dwr/interface/searchService.js"></script>
<script>

var request;
var minPage;
var maxPage;
var startIndex;
var hasNext;
var infoPos = [];

function doSearch(type){

	var result = document.getElementById("result");
	var pagingdiv = document.getElementById('paging');
	result.innerHTML="";
	pagingdiv.innerHTML = "";

	if (type != 'paging') {
		var startindexinput = document.getElementById('startIndex');
		startindexinput.value = "1";
	}	
	
	request = {startIndex:1, query:""};
	DWRUtil.getValues(request);

	searchService.getSearchResults(request, fillPage);
}

function fillPage(data){

	var list = data.results;
	//alert("list.length:"+list.length);
	var resultdiv = document.getElementById('result');
	var pagingdiv = document.getElementById('paging');

	resultdiv.innerHTML = "";
	pagingdiv.innerHTML = "";
	

	if (list.length == 0) {
		resultdiv.innerHTML = "<span>抱歉，找不到您搜索的关键字...</span>";
		return;
	}
	
	//alert("list.length:"+list.length);
	for(var i=0; i<list.length; i++) {		
		var ele = document.createElement('div');
		ele.setAttribute('id','info' + i);
		ele.innerHTML = "";
		resultdiv.appendChild(ele);
		searchService.getSearchResultById(list[i], fillDetailResult);
	}
	
	minPage = data.minPage;
	maxPage = data.maxPage;
	startIndex = data.startIndex;
	hasNext = data.hasNext;
	
	if (minPage != 1) {
		var link = document.createElement('a');
		link.setAttribute("style","margin:0 2 0 2");
		link.setAttribute("href","javascript:paging('" + ((minPage-11)*10+1) + "')");
		link.innerHTML = "前10页<<";	
		pagingdiv.appendChild(link);
	}
	
	for (var j=minPage; j <=maxPage; j++)
	{
		if ((j-1)*10+1 != startIndex)
		{		
			var link = document.createElement('a');
			link.setAttribute("style","margin:0 2 0 2");
			link.setAttribute("href","javascript:paging('" + ((j-1)*10+1) + "')");
			link.innerHTML = "第" + j + "页";
			pagingdiv.appendChild(link);
		}
		else {
			pagingdiv.innerHTML += ("第" + j + "页");
		}
	}
	
	if (hasNext == 1) {
		var link = document.createElement('a');
		link.setAttribute("style","margin:0 2 0 2");
		link.setAttribute("href","javascript:paging('" + (maxPage*10+1) + "')");
		link.innerHTML = ">>后10页";
		pagingdiv.appendChild(link);
	}
	
}

function fillDetailResult(record) {
	//alert("record.title："+record.title);
	var result = document.getElementById('info'+ record.id);
	result.innerHTML= "<table border='0' cellpadding='10' cellspacing='0' width='800' ><tr><td>"
		+"<div style='font-size:18'><a href='"+ record.link +"' target='_blank'>"+ record.title +"</a></div>"
		+"<div style='font-size:14'>"+ record.introduction +"...</div>"
		+"<div style='font-size:14'>"+ record.link +"&nbsp;"+ record.date +"&nbsp;-&nbsp;<a href='"+ record.link +"' target='_blank'>网页快照</a>"
		+"&nbsp;-&nbsp;<a href='"+ record.link +"' target='_blank'>地图定位</a></div>"
		+"</td></tr></table>";
}

function paging(newIndex) {
	document.getElementById('startIndex').value = newIndex;
	doSearch('paging');
}

function handlekey(){
	if (document.getElementById('query').value == '')
		return;
	var intKey = -1;
	if(window.event) {
		intKey = event.keyCode;
		if(intKey == 13){
			doSearch('');
		}
	}
}

</script>

</head>
<body>
<input type="hidden" name="startIndex" id="startIndex" value="1">
<!-- 这是搜索栏 -->
<div id="searchbar">
	<table align="center">
		<tr align="center">
			<td>
				<img src="./image/logo.png" style="width:870; height:150" /><br/><br/><br/>
			</td>
		</tr>
		<tr align="center"><td style="font-weight:bold; color:red">
		A search engine demo for disasters and accidents in China.<br>
		Try searching keyword '火灾', which means fire :)
		</td></tr>
		<tr align="center">
			<td><input style="height:30" size="60" type="text" name="query" id="query" value=""  onkeyup="handlekey()">
			<input style="height:30;width:90;text-align:center;font-size:14" type="button" value="搜  索"  id="search" onclick="javascript:doSearch('')"></td>
		</tr>
	</table>
</div>



<!-- 这是结果栏 -->
<div id="result">
</div>


<!-- 这是分页栏 -->
<div id="paging">

</div>

<!-- 这是Footer -->
<div id="footer">
	<table align="center">
		<tr align="center">
			<td>
				<br/><br/>
				<img src="./image/footer.png"/>
			</td>
		</tr>
	</table>
</div>
</body>
</html>
