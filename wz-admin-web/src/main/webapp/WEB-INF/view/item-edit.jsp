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
		var itemEditDiv = $("#item-edit"); 

		/** 实例化编辑器 */
		window.itemEditEditor = TAOTAO.createEditor(itemEditDiv.find("textarea[name=desc]"), kingEditorParams);
		
		/** 为选择物品类目按钮绑定点击事件 */
		itemEditDiv.find(".selectItemCat").click(function(){
			/** 创建div作为弹出窗口 */
   			var div = $("<div/>").css({padding:"5px"}).append("<ul/>");
			/** 把div转化成window */
   			div.window({
   				width : 500, // 宽度
   			    height : 450, // 高度
   			    modal : true, // 模态窗口
   			    closed : true, // 是否可以关闭
   			    iconCls : 'icon-save', // 图标
   			    title : '选择物品类目', // 标题
   			    onOpen : function(){ // 监听打开窗口事件
   			    	/** 从div中找ul，再把ul转化成树容器  */
   			    	div.find("ul").tree({
   			    		url : '/itemcat', // 请求URL
   			    		method :'get', // 请求方式
   			    		animate : true, // 动画效果
   			    		onClick : function(node){ // 为点击树节点绑定事件
   			    			/** 判断树节点是不是叶节点 */
   			    			if($(this).tree("isLeaf", node.target)){
   			    				/** 设置id到隐藏表单中 */
   			    				itemEditDiv.find("input[name=cid]").val(node.id);
   			    				/** 设置显示文体 */
   			    				itemEditDiv.find("#cid_span").text(node.text);
   			    				/** 关闭窗口 */
   			    				div.window('close');
   			    			}
   			    		}
   			    	});
   			    },
   			    onClose : function(){ // 监听关闭窗口事件
   			    	$(this).window("destroy"); // 消毁窗口
   			    }
   			}).window('open'); // 打开窗口
   		});
		
		// 初始化图片上传
		itemEditDiv.find(".picFileUpload").click(function(){
			/** 加载文件上传插件,监听文件上传 */
       		KindEditor.editor(kingEditorParams).loadPlugin('multiimage',function(){
       			/** 富文本编辑器对象 */
       			var editor = this;
       			/** 文件上传插件 */
       			editor.plugin.multiImageDialog({
       				/** 为全部插件按钮绑定事件 */
					clickFn : function(urlList) {
						itemEditDiv.find(".pics li").remove();
						var imgArr = new Array();
						$.each(urlList, function(i, data) {
							imgArr.push(data.url);
							itemEditDiv.find(".pics ul").append("<li><a href='"+ data.url +"' target='_blank'><img src='"+data.url+"' width='80' height='50' /></a></li>");
						});
						/** 把上传的文件地址存入input元素，为保存做准备 */
						itemEditDiv.find("#itemeEditForm [name=image]").val(imgArr.join(","));
						/** 隐藏文件上传对话框 */
						editor.hideDialog();
					}
				});
       		});
       	});
		
		
		/** 为提交按钮绑定点击事件 */
		itemEditDiv.find("#submitBtn").click(function(){
			if(!itemEditDiv.find('#itemeEditForm').form('validate')){
				$.messager.alert('提示','表单还未填写完成!');
				return ;
			}
			/** 处理商品的价格的单位，将元转化为分 */
			itemEditDiv.find("#itemeEditForm [name=price]")
					.val(eval(itemEditDiv.find("#itemeEditForm [name=priceView]").val()) * 100);
			
			/** 将编辑器中的内容同步到隐藏的多行文本中 */
			itemEditEditor.sync();
			/** 异步请求修改 */
			$.ajax({
			   type: "post",
			   url: "/item/update",
			   data: itemEditDiv.find("#itemeEditForm").serialize(),
			   success: function(){
				   $.messager.alert('提示','修改商品成功!','info',function(){
						$("#itemEditWindow").window('close');
						$("#itemList").datagrid("reload");
					});
			   },
			   error: function(){
				   $.messager.alert('提示','修改商品失败!');
			   }
			});
		});
	});

</script>

<div style="padding:5px 5px 5px 5px" id="item-edit">
	<form id="itemeEditForm" class="itemForm" method="post">
		<input type="hidden" name="id"/>
	    <table cellpadding="5">
	        <tr>
	            <td>商品类目:</td>
	            <td>
	            	<a href="javascript:void(0)" class="easyui-linkbutton selectItemCat">选择类目</a>
	            	<span id="cid_span"></span>
	            	<input type="hidden" name="cid" style="width: 280px;"></input>	
	            </td>
	        </tr>
	        <tr>
	            <td>商品标题:</td>
	            <td><input class="easyui-textbox" type="text" name="title" data-options="required:true" style="width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>商品卖点:</td>
	            <td><input class="easyui-textbox" name="sellPoint" data-options="multiline:true,validType:'length[0,150]'" style="height:60px;width: 280px;"></input></td>
	        </tr>
	        <tr>
	            <td>商品价格:</td>
	            <td><input class="easyui-numberbox" type="text" name="priceView" data-options="min:1,max:99999999,precision:2,required:true" />
	            	<input type="hidden" name="price"/>
	            </td>
	        </tr>
	        <tr>
	            <td>库存数量:</td>
	            <td><input class="easyui-numberbox" type="text" name="num" data-options="min:1,max:99999999,precision:0,required:true" /></td>
	        </tr>
	        <tr>
	            <td>条形码:</td>
	            <td>
	                <input class="easyui-textbox" type="text" name="barcode" data-options="validType:'length[1,30]'" />
	            </td>
	        </tr>
	        <tr>
	            <td>商品图片:</td>
	            <td>
	            	<a href="javascript:void(0)" class="easyui-linkbutton picFileUpload">上传图片</a>
	                <div class="pics"><ul></ul></div>
	                <input type="hidden" name="image"/>
	            </td>
	        </tr>
	        <tr>
	            <td>商品描述:</td>
	            <td>
	                <textarea style="width:800px;height:300px;visibility:hidden;" name="desc"></textarea>
	            </td>
	        </tr>
	    </table>
	</form>
	<div style="padding:5px">
	    <a href="javascript:void(0)" class="easyui-linkbutton" 
	    		id="submitBtn" data-options="iconCls:'icon-ok'">提交</a>
	</div>
</div>