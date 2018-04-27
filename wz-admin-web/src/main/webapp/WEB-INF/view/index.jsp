<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>淘淘商城后台管理系统</title>
	<jsp:include page="commons/common-js.jsp"></jsp:include>
	<style type="text/css">
		.content {
			padding: 5px 5px 5px 5px;
		}
	</style>
	<script type="text/javascript">
		$(function(){
			$('#menu').tree({
				onClick: function(node){
					if($('#menu').tree("isLeaf",node.target)){
						var tabs = $("#tabs");
						var tab = tabs.tabs("getTab",node.text);
						if(tab){
							tabs.tabs("select",node.text);
						}else{
							tabs.tabs('add',{
							    title : node.text,
							    href : node.attributes.url,
							    closable : true,
							    bodyCls : "content"
							});
						}
					}
				}
			});
		});
	</script>
</head>

<body class="easyui-layout">

	<!-- 西部区域 -->
	<div data-options="region:'west',title:'菜单',split:true, width:180">
		<ul id="menu" class="easyui-tree"
			style="margin-top: 10px; margin-left: 5px;">
			<li>
				<span>商品管理</span>
				<ul>
					<li data-options="attributes:{'url':'/page/item-add'}">新增商品</li>
					<li data-options="attributes:{'url':'/page/item-list'}">查询商品</li>
				</ul>
			</li>
			<li>
				<span>网站内容管理</span>
				<ul>
					<li data-options="attributes:{'url':'/page/content-category'}">内容分类管理</li>
					<li data-options="attributes:{'url':'/page/content'}">内容管理</li>
				</ul>
			</li>
		</ul>
	</div>

	<!-- 中间区域 -->
	<div data-options="region:'center'">
		<div id="tabs" class="easyui-tabs" data-options="fit:true,border:false">
			<div title="首页" style="padding: 20px;"></div>
		</div>
	</div>
</body>
</html>