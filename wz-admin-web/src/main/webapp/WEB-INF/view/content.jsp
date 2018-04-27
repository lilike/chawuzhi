<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">

	/** 定义工具栏 */
	var contentToolbar = [{ 
	    text : '新增', // 文本
	    iconCls : 'icon-add', // 图标
	    handler : function(){ // 监听事件
	    	/** 获取用户选中的树节点 */
	    	var tree_node = $("#content").find("#content_category_tree").tree("getSelected");
	    	/** 判断是否有选中的树节点，判断树节点是不是叶节点 */
	    	if(!tree_node || !$("#content").find("#content_category_tree").tree("isLeaf",tree_node.target)){
	    		$.messager.alert('提示','新增内容必须选择一个内容分类!', "info");
	    		return;
	    	}
	    	/** 调用创建窗口函数 */
	    	TT.createWindow({
				url : "/page/content-add" // 传进请求URL
			}); 
	    }
	},{
	    text : '编辑', // 文本
	    iconCls : 'icon-edit', // 图标
	    handler : function(){ // 监听事件
	    	var ids = TT.getSelectionsIds("#content #contentList");
	    	if(ids.length == 0){
	    		$.messager.alert('提示','必须选择一个内容才能编辑!', "info");
	    		return;
	    	}
	    	if(ids.indexOf(',') > 0){
	    		$.messager.alert('提示','只能选择一个内容!', "info");
	    		return;
	    	}
	    	/** 弹出修改窗口 */
	    	TT.createWindow({
				url : "/page/content-edit", // 请求地址
				onLoad : function(){ // 窗口加载成功后，为修改页面填充数据
					var data = $("#content #contentList").datagrid("getSelections")[0];
					/** 为表单填充数据 */
					$("#contentEditForm").form("load",data);
					/** 实现图片显示 */
					if(data.pic){
						$("#contentEditForm [name=pic]").after("<a href='"+ data.pic +"' target='_blank'><img src='"+data.pic+"' width='80' height='50'/></a>");	
					}
					/** 实现图片显示 */
					if(data.pic2){
						$("#contentEditForm [name=pic2]").after("<a href='"+ data.pic2 +"' target='_blank'><img src='"+data.pic2+"' width='80' height='50'/></a>");					
					}
					/** 为富文本编辑器添加内容 */
					contentEditEditor.html(data.content);
				}
			});    	
	    }
	},{
	    text : '删除', // 删除
	    iconCls : 'icon-cancel',
	    handler : function(){
	    	var ids = TT.getSelectionsIds("#content #contentList");
	    	if(ids.length == 0){
	    		$.messager.alert('提示','未选中商品!', 'info');
	    		return;
	    	}
	    	/** 确认窗口 */
	    	$.messager.confirm('确认','确定删除ID为 '+ ids +' 的内容吗？', function(r){
	    	    if (r){
	    	    	var params = {"ids" : ids};
	            	$.post("/content/delete", params, function(data, status){
	        			if(status == "success"){
	        				$.messager.alert('提示','删除内容成功!', "info", function(){
	        					$("#contentList").datagrid("reload");
	        				});
	        			}
	        		});
	    	    }
	    	});
	    }
	}];
	
	$(function(){
		
		/** 获取该页面对应的div容器 */
		var contentDiv = $("#content"); 
		/** 获取树容器对象 */
		var treeObj = contentDiv.find("#content_category_tree");
		/** 获取内容数据表格对象 */
		var datagrid = contentDiv.find("#contentList");
		/** 为树节点绑定点击事件 */
		treeObj.tree({
			onClick : function(node){ //点击事件
				/** 判断树节点是不是叶节点 */
				if(treeObj.tree("isLeaf",node.target)){
					/** 发请求重新加载内容 */
					datagrid.datagrid('reload', {
						categoryId : node.id // 请求参数
			        });
				}
			}
		});
	});
</script>

<!-- 定义easyui-panel面板 -->
<div class="easyui-panel" id="content" data-options="width:'100%',height:'100%',
					noheader:true,border:false"	style="padding:5px;">
				
	<!-- 使用easyui-layout布局 -->
    <div class="easyui-layout" data-options="fit:true">
    	
    	<!-- 定义西部区域 -->
        <div data-options="region:'west',split:false" style="width:200px;padding:5px">
            <ul id="content_category_tree" class="easyui-tree" 
            		data-options="url:'/contentcategory',animate: true,method : 'get'"></ul>
        </div>
        
        <!-- 定义中间区域 -->
        <div data-options="region:'center'" style="padding:5px">
	            <table class="easyui-datagrid" id="contentList" 
	            	data-options="toolbar:contentToolbar,singleSelect:false,
	            				  collapsible:true,pagination:true,method:'get',
	            				  pageSize:20,fit:true,url:'/content',
	            				  fitColumns:true,
	            				  queryParams:{categoryId:0}">
				    <thead>
				        <tr>
				            <th data-options="field:'id',width:30">ID</th>
				            <th data-options="field:'title',width:120,formatter:TAOTAO.formatText">内容标题</th>
				            <th data-options="field:'subTitle',width:100,formatter:TAOTAO.formatText">内容子标题</th>
				            <th data-options="field:'titleDesc',width:120,formatter:TAOTAO.formatText">内容描述</th>
				            <th data-options="field:'url',width:60,align:'center',formatter:TAOTAO.formatUrl">内容连接</th>
				            <th data-options="field:'pic',width:50,align:'center',formatter:TAOTAO.formatUrl">图片</th>
				            <th data-options="field:'pic2',width:50,align:'center',formatter:TAOTAO.formatUrl">图片2</th>
				            <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
				            <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
				        </tr>
				    </thead>
			</table>
        </div>
        
    </div>
</div>