<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
	/** 定义工具栏数据 */
    var itemToolbar = [{
        text : '新增',
        iconCls : 'icon-add',
        handler : function(){
        	$(".tree-title:contains('新增商品')").parent().click();
        }
    },{
        text : '编辑',
        iconCls : 'icon-edit',
        handler : function(){
        	/** 获取用户选中的行 */
        	var rows = $("#itemList").datagrid("getSelections");
        	if(rows.length == 0){
        		$.messager.alert('提示','必须选择一个商品才能编辑!', "warning");
        	}else if(rows.length == 1){
        		$("#itemEditWindow").window({
            		onLoad : function(){
            			/** 回显数据 */
            			var data =rows[0];
            			/** 显示价格 */
            			data.priceView = TAOTAO.formatPrice(data.price);
            			/** 让表单回显数据 */
            			$("#item-edit #itemeEditForm").form("load", data);
            			/** 加载商品描述 */
            			$.getJSON('/itemdesc/' + data.id, function(data){
            				/** 在富文本编辑器中显示数据 */
            				itemEditEditor.html(data.itemDesc);
            			});
            			/** 回显类目名称 */
                    	$("#item-edit #cid_span").text(data.name);
            			/** 判断是否有图片数据 */
                    	if(data.image){
                    		/** 获取item-edit.jsp中<div class="pics"><ul></ul></div>的ul标签 */
                			var ul = $("#item-edit .pics").find("ul");
                			/** 清空ul中的内容 */
                			ul.empty();
                			/** 分隔多个图片地址成数组 */
                    		var imgs = data.image.split(",");
                			/** 迭代数组 */
                    		for(var i in imgs){
                    			if($.trim(imgs[i]).length > 0){
                    				ul.append("<li><a href='"+imgs[i]+"' target='_blank'><img src='"+imgs[i]+"' width='80' height='50' /></a></li>");
                    			}
                    		}
                    	}
            			
            		}
            	}).window("open");
        	}else{
        		$.messager.alert('提示','只能选择一个商品!', "warning");
        	}
        }
    },{
        text : '删除', // 删除功能
        iconCls : 'icon-cancel',
        handler : function(){
        	/** 获取用户选中的行 */
        	var rows = $("#itemList").datagrid("getSelections");
        	if(rows.length == 0){
        		$.messager.alert('提示','请选择要删除的商品！', "warning");
        	}else{
        		var ids = [];
        		/** 迭代所有行的数据 */
        		$.each(rows, function(){
        			ids.push(this.id);
        		});
        		$.messager.confirm('确认','亲您确定要删除吗？',function(r){
            	    if (r){
                    	$.post("/item/delete",{"ids" : ids.join(",")}, function(){
               				$.messager.alert('提示','删除商品成功!','info', function(){
               					$("#itemList").datagrid("reload");
               				});
                		});
            	    }
            	});
        	}
        }
    },'-',{
        text : '下架', // 下架功能
        iconCls : 'icon-remove',
        handler : function(){
        	/** 获取用户选中的行 */
        	var rows = $("#itemList").datagrid("getSelections");
        	if(rows.length == 0){
        		$.messager.alert('提示','未选中商品!', 'info');
        	}else{
        		var ids = [];
        		/** 迭代所有行的数据 */
        		$.each(rows, function(){
        			ids.push(this.id);
        		});
        		$.messager.confirm('确认','确定下架ID为 '+ids+' 的商品吗？',function(r){
            	    if (r){
                    	$.post("/item/instock",{"ids" : ids.join(",")}, function(){
               				$.messager.alert('提示','下架商品成功!','info',function(){
               					$("#itemList").datagrid("reload");
               				});
                		});
            	    }
            	});
        	}
        }
    },{
        text : '上架', // 上架
        iconCls : 'icon-remove',
        handler : function(){
        	/** 获取用户选中的行 */
        	var rows = $("#itemList").datagrid("getSelections");
        	if(rows.length == 0){
        		$.messager.alert('提示','未选中商品!', 'info');
        	}else{
        		var ids = [];
        		/** 迭代所有行的数据 */
        		$.each(rows, function(){
        			ids.push(this.id);
        		});
        		$.messager.confirm('确认','确定上架ID为 '+ids+' 的商品吗？',function(r){
            	    if (r){
                    	$.post("/item/reshelf", {"ids" : ids.join(",")} , function(data){
               				$.messager.alert('提示','上架商品成功!','info',function(){
               					$("#itemList").datagrid("reload");
               				});
                		});
            	    }
            	});
        	}
        }
    },{
        text : '<input type="text" id="search" name="search">' // 搜索框
    }];
	
    /** 监听文档加载完事件 */
	$(document).ready(function(){
		/** 初始化数据表格 */
		$("#itemList").datagrid({
			collapsible : true,
			pagination : true,
			url : '/item',
			method : 'get',
			pageSize : 30,
			toolbar : itemToolbar
		});
		/** 搜索框 */
		$("#search").searchbox({ 
			width : 300,
			searcher : function(value,name){ 
				$("#itemList").datagrid("load", {"title" : encodeURI(value)});
			}, 
			prompt:'请输入商品标题'
		});
	});
</script>

<!-- 定义展示商品的数据表格 -->
<table id="itemList" title="商品列表" data-options="fit:true,fitColumns:true,nowrap:false">
    <thead>
        <tr>
        	<th data-options="field:'ck',checkbox:true"></th>
        	<th data-options="field:'id',width:30">商品ID</th>
            <th data-options="field:'title',width:150,formatter:TAOTAO.formatText">商品标题</th>
            <th data-options="field:'name',width:50">叶子类目</th>
            <th data-options="field:'sellPoint',width:220,formatter:TAOTAO.formatText">卖点</th>
            <th data-options="field:'price',width:70,align:'right',formatter:TAOTAO.formatPrice">价格</th>
            <th data-options="field:'num',width:70,align:'right'">库存数量</th>
            <th data-options="field:'barcode',width:100,formatter:TAOTAO.formatText">条形码</th>
            <th data-options="field:'status',width:60,align:'center',formatter:TAOTAO.formatItemStatus">状态</th>
            <th data-options="field:'created',width:130,align:'center',formatter:TAOTAO.formatDateTime">创建日期</th>
            <th data-options="field:'updated',width:130,align:'center',formatter:TAOTAO.formatDateTime">更新日期</th>
        </tr>
    </thead>
</table>

<!-- 定义div作为弹出窗口 -->
<div id="itemEditWindow" 
	 title="编辑商品" 
	 data-options="modal:true,closed:true,iconCls:'icon-save',href:'/page/item-edit'"
	 style="width:80%;height:80%;padding:10px;">
</div>