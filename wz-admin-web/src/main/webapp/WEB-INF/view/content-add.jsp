<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="${ctx}/js/kindeditor/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="${ctx}/js/kindeditor/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/js/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript">
	
	/** 定义编辑器全局参数 */
	var kingEditorParams = {
		filePostName  : "pic",   // 文件上传请求参数name名称  
		uploadJson : '/pic/upload',// 请求地址
		dir : "image" // 指定为图片
	};
	
	$(function(){
		
		/** 获取该页面对应的div容器 */
		var contentAddDiv = $("#content-add"); 
		
		/** 获取用户选中的树节点id */
		var id = $("#content #content_category_tree").tree("getSelected").id;
		/** 把内容分类id放到input中，提交到后台 */
		contentAddDiv.find("#contentAddForm [name=categoryId]").val(id);
		
		/** 创建富文本编辑器 */
		window.contentAddEditor = KindEditor.create(contentAddDiv.find("#contentAddForm [name=content]"),
						kingEditorParams);
		/** 初始化单图片上传 */
		contentAddDiv.find(".onePicUpload").click(function(){
			var input = $(this).siblings("input");
			KindEditor.editor(kingEditorParams).loadPlugin('image', function() {
				this.plugin.imageDialog({
					showRemote : false,
					clickFn : function(url) {
						input.parent().find("img").remove();
						input.val(url);
						input.after("<a href='"+ url +"' target='_blank'><img src='"+ url +"' width='80' height='50'/></a>");
						this.hideDialog();
					}
				});
			});
		});
		
		/** 为提交按钮绑定点击事件 */
		contentAddDiv.find("#submitBtn").click(function(){
			/** 表单校验 */
			if(!contentAddDiv.find('#contentAddForm').form('validate')){
				$.messager.alert('提示','表单还未填写完成!');
				return;
			}
			/** 同步富文本编辑器中的数据到textarea中 */
			contentAddEditor.sync();
			/** 异步添加内容 */
			$.ajax({
			   type: "post",
			   url: "/content/save",
			   data: contentAddDiv.find("#contentAddForm").serialize(),
			   success: function(msg){
				   	$.messager.alert('提示','新增内容成功!');
	 				$("#content #contentList").datagrid("reload");
	 				TT.closeCurrentWindow();
			   },
			   error: function(){
				   $.messager.alert('提示','新增内容失败!');
			   }
			});
		});
		/** 为重置按钮绑定点击事件 */
		contentAddDiv.find("#resetBtn").click(function(){
			/** 重置表单 */
			contentAddDiv.find('#contentAddForm').form('reset');
			/** 设置富文本编辑器中的内容为空 */
			contentAddEditor.html('');
		});
		
	});
</script>

<div style="padding:5px 5px 5px 5px" id="content-add">
	<form id="contentAddForm" class="itemForm" method="post">
		<input type="hidden" name="categoryId"/>
	    <table cellpadding="5">
	        <tr>
	            <td>内容标题:</td>
	            <td><input class="easyui-textbox" type="text" name="title" data-options="required:true" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>内容子标题:</td>
	            <td><input class="easyui-textbox" type="text" name="subTitle" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>内容描述:</td>
	            <td><input class="easyui-textbox" name="titleDesc" data-options="multiline:true,validType:'length[0,150]'" style="height:60px;width: 280px;"></input>
	            </td>
	        </tr>
	         <tr>
	            <td>URL:</td>
	            <td><input class="easyui-textbox" type="text" name="url" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>图片:</td>
	            <td>
	                <a href="javascript:void(0)" class="easyui-linkbutton onePicUpload">图片上传</a>
	                <br><input type="hidden" name="pic" />
	            </td>
	        </tr>
	        <tr>
	            <td>图片2:</td>
	            <td>
	            	<a href="javascript:void(0)" class="easyui-linkbutton onePicUpload">图片上传</a>
	            	<br><input type="hidden" name="pic2" />
	            </td>
	        </tr>
	        <tr>
	            <td>内容:</td>
	            <td>
	                <textarea style="width:800px;height:300px;visibility:hidden;" name="content"></textarea>
	            </td>
	        </tr>
	    </table>
	</form>
	<div style="padding:5px">
	    <a class="easyui-linkbutton" 
	    		data-options="iconCls:'icon-ok'" id="submitBtn">提交</a>
	    <a class="easyui-linkbutton" 
	    		data-options="iconCls:'icon-no'" id="resetBtn">重置</a>
	</div>
</div>