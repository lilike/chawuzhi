<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link href="${ctx}/js/kindeditor/themes/default/default.css" type="text/css" rel="stylesheet">
<script type="text/javascript" charset="utf-8" src="${ctx}/js/kindeditor/kindeditor-all-min.js"></script>
<script type="text/javascript" charset="utf-8" src="${ctx}/js/kindeditor/lang/zh_CN.js"></script>
<script type="text/javascript">
	$(function(){
		
		/** 获取该页面对应的div容器 */
		var contentEditDiv = $("#content-edit"); 
		/** 初始化文本编辑器 */
		window.contentEditEditor = TT.createEditor(contentEditDiv.find("#contentEditForm [name=content]"));
		/**
	     * 初始化单图片上传组件 <br/>
	     * 选择器为：.onePicUpload <br/>
	     * 上传完成后会设置input内容以及在input后面追加<img> 
	     */
	     contentEditDiv.find(".onePicUpload").click(function(){
			var _self = $(this);
			KindEditor.editor(TT.kingEditorParams).loadPlugin('image', function() {
				this.plugin.imageDialog({
					showRemote : false,
					clickFn : function(url) {
						var input = _self.siblings("input");
						input.parent().find("img").remove();
						input.val(url);
						input.after("<a href='"+ url +"' target='_blank'><img src='"+ url +"' width='80' height='50'/></a>");
						/** 隐藏文件上传对话框 */
						this.hideDialog();
					}
				});
			});
		});
	     
	     /** 为提交按钮绑定点击事件 */
		contentEditDiv.find("#submitBtn").click(function(){
			if(!contentEditDiv.find('#contentEditForm').form('validate')){
				$.messager.alert('提示','表单还未填写完成!','warning');
				return;
			}
			/** 将编辑器中的内容同步到隐藏的多行文本中 */
			contentEditEditor.sync();
			/** 发送异步请求 */
			$.post("/content/update",contentEditDiv.find("#contentEditForm").serialize(), function(data){
					$.messager.alert('提示','编辑内容成功!', 'info',function(){
						$("#content #contentList").datagrid("reload");
						TT.closeCurrentWindow();
					});
			});
		});
	});
</script>

<div style="padding:5px 5px 5px 5px" id="content-edit">
	<form id="contentEditForm" class="itemForm" method="post">
		<input type="hidden" name="categoryId"/>
		<input type="hidden" name="id"/>
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
	                <input type="hidden" name="pic" />
	                <a href="javascript:void(0)" class="easyui-linkbutton onePicUpload">图片上传</a>
	            </td>
	        </tr>
	        <tr>
	            <td>图片2:</td>
	            <td>
	            	<input type="hidden" name="pic2" />
	            	<a href="javascript:void(0)" class="easyui-linkbutton onePicUpload">图片上传</a>
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
	</div>
</div>