<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
	$(function(){
		/** 获取该页面对应的div容器 */
		var contentCategoryDiv = $("#content-category"); 
		
		/** 初始化tree树 */
		contentCategoryDiv.find("#content_category_tree").tree({
			url : '/contentcategory', // 请求URL
			method : "get", // 请求方式
			animate : true, // 是否有动画效果
			onContextMenu : function(e,node){// 监听上下文菜单事件
				/** 取消事件的默认行为 */
	            e.preventDefault();
				/** 选中当前树节点 */
	            $(this).tree('select',node.target);
				/** 显示右键菜单 */
	            $('#content_category_menu').menu('show',{
	                left : e.pageX, // 离左边的距离
	                top : e.pageY // 离上边的距离
	            });
	        },
	        onAfterEdit : function(node){ // 监听编辑完事件
	        	/** 判断树节点id */
	        	if(node.id == 0){
	        		/** 异步新增树节点 */
	        		$.post("/contentcategory/save", {parentId : node.parentId, name : node.text}, function(id){ // 返回响应数据为 id
	        			contentCategoryDiv.find("#content_category_tree").tree("update",{
        					target : node.target,
        					id : id
        				});
	        		});
	        	}else{
	        		/** 异步修改树节点 */
	        		$.ajax({
	        			type : "post", // 请求方式
	        			url : "/contentcategory/update", // 请求url
	        			data: {id : node.id, name : node.text}, // 请求参数
	        			success: function(){ // 请求成功
	        			},
	        			error: function(){ // 请求失败
	        				$.messager.alert('提示','重命名失败!');
	        			}
	        		});
	        	}
	        }
		});
		
		/** 初始化右键菜单 */
		contentCategoryDiv.find("#content_category_menu").menu({
			onClick : function(item){ // 监听菜单点击事件
				/** 获取树容器对象 */
				var treeObj = contentCategoryDiv.find("#content_category_tree");
				/** 获取选中的树节点 */
				var node = treeObj.tree("getSelected");
				/** 新增树节点 */
				if(item.name === "add"){
					treeObj.tree('append', {
			            parent: (node ? node.target : null), // 指定父级
			            data: [{ // 设置新节点数据
			                text: '新建分类',
			                id : 0,
			                parentId : node.id
			            }]
			        }); 
					/** 查找id为零的树节点 */
					var new_node = treeObj.tree('find',0);
					/** 设置该节点选中，并开启编辑 */
					treeObj.tree("select",new_node.target).tree('beginEdit',new_node.target);
				}else if(item.name === "rename"){ // 重命名
					treeObj.tree('beginEdit',node.target);
				}else if(item.name === "delete"){ // 删除
					/** 确认信息 */
					$.messager.confirm('确认','确定删除名为 '+ node.text +' 的分类吗？',function(r){
						if(r){
							$.ajax({
			     			   type: "post",
			     			   url: "/contentcategory/delete",
			     			   data : {parentId : node.parentId, id : node.id},
			     			   success: function(msg){
			     				  treeObj.tree("remove", node.target);
			     			   },
			     			   error: function(){
			     				   $.messager.alert('提示','删除失败!');
			     			   }
			     			});
						}
					});
				}
			}
		});
	});
</script>

<div id="content-category">
	
	<!-- 定义树容器 -->
	<ul id="content_category_tree"></ul>
	
	<!-- 定义右键菜单容器 -->
	<div id="content_category_menu" style="width:120px;">
	    <div data-options="iconCls:'icon-add',name:'add'">添加</div>
	    <div data-options="iconCls:'icon-remove',name:'rename'">重命名</div>
	    <div class="menu-sep"></div>
	    <div data-options="iconCls:'icon-remove',name:'delete'">删除</div>
	</div>
</div>