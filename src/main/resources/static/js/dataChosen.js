
/***
 * 设置单选
 * @param self 本体
 */
function singleChosen(self){
	var idPrefix = $(self).parent().parent().attr('id-prefix');
	
	$('#' + idPrefix + 'Chosen .iradio_square-green.checked').removeClass('checked');
	$(self).find('.iradio_square-green').addClass('checked');
}


/***
 * 选择一个车间
 * @param self 本体
 * @param idPrefix 唯一标识符前缀
 */
function choseAShop(self, idPrefix, formId){
	var dataId = $(self).attr('data-id'),
		formId = formId ? formId : $(self).parent().parent().parent().attr('id'),
		shopTpl = shopDatas.innerHTML,//数据模板
		shopView = $('#shop-view');//数据视图容器
	
	loading = layer.load();	
	$.ajax({
		url: basePath + 'shop/loadShop',
		type: 'get',
		success:function(result){
			layer.close(loading);
			if (result.success) {
				laytpl(shopTpl).render(result.data.resultList, function(html){
					shopView.html(html);
					//form的id只能实时生成，单页面应用需要保证id的唯一性。
					layer.open({
						type : 1,
						title : '选择一个车间',
						skin : 'layui-layer-rim', //加上边框
						area : [ '60%', '480px' ], //宽高
						content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#shopChosenTpl').html() + '</div>',
						btn : [ '已选择', '取消' ],
						yes : function(lindex, layero) {
							var shop = $('#' + idPrefix + 'Chosen .iradio_square-green.checked').find('input[name="chosenShop"]').val(),
								shopObj =  (shop && shop.length > 0) ? JSON.parse(shop) : null;
								
							if (shopObj) {
								$('#' + formId + ' input[name="shopName"]').val(shopObj.shopName);
								$('#' + formId + ' input[name="shopName"]').attr('data-id', shopObj.id);
								$('#' + formId + ' input[name="shop"]').val(shop);
								layer.close(lindex);
							} else {
								layer.msg('请选择一个车间');
							}
							return false;
						}
					});
					$('#' + idPrefix + 'Chosen .i-checks').iCheck({
					    checkboxClass: 'icheckbox_square-green',
					    radioClass: 'iradio_square-green',
					});
					if(dataId) {
						$('#' + idPrefix + 'Chosen .iradio_square-green.checked').removeClass('checked');
					}
					$('#' + idPrefix + 'Chosen input[data-id="' + dataId + '"]').parent().addClass('checked');
					$('#' + idPrefix + 'Chosen .filter').attr('id', idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').attr('data-filter', '#' + idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').footable();
					$('#' + idPrefix + 'Chosen #shop-view').attr('id-prefix', idPrefix);
				});
			}
		}
	});
}
/***
 * 选择一个产品
 * @param self 本体
 * @param idPrefix 唯一标识符前缀
 */
function choseAProduct(self, idPrefix, formId){
	var dataId = $(self).attr('data-id'),
		formId = formId ? formId : $(self).parent().parent().parent().attr('id'),
		productTpl = productDatas.innerHTML,//数据模板
		productView = document.getElementById('product-view');//数据视图容器
	
	loading = layer.load();	
	$.ajax({
		url: basePath + 'product/loadProduct',
		type: 'get',
		success:function(result){
			layer.close(loading);
			result = JSON.parse(result);
			for(var i = 0; i < result.data.resultList.length; i++ ) {
				result.data.resultList[i].str = JSON.stringify(result.data.resultList[i]);
			}
			if (result.success) {
				laytpl(productTpl).render(result.data.resultList, function(html){
					productView.innerHTML = html;
					//form的id只能实时生成，单页面应用需要保证id的唯一性。
					layer.open({
						type : 1,
						title : '选择产品',
						skin : 'layui-layer-rim', //加上边框
						area : [ '60%', '400px' ], //宽高
						content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#productChosenTpl').html() + '</div>',
						btn : [ '已选择', '取消' ],
						yes : function(lindex, layero) {
							var pro = $('.iradio_square-green.checked').find('input[name="chosenProduct"]').val(),
								str = $('.iradio_square-green.checked').find('input[name="chosenProduct"]').attr('data-str'),
								proObj = (pro && pro.length > 0) ? JSON.parse(pro) : null;
								
								str = JSON.parse(str);
						
							if (proObj) {
								var productTemplete = str.productTemplete,
									type1 = [],
									type2 = {},
									type1Html = '';
								proObj.productTemplete = productTemplete;
								for(var i = 0; i < productTemplete.length; i++) {
									if(productTemplete[i].type == 1){
										var item = {
												prdFeatureTemplateName:	productTemplete[i].prdFeatureTemplateName,
												value:	productTemplete[i].value
										};
										
										type1.push(productTemplete[i]);
										type1Html += '<option value='+ JSON.stringify(item) +'>'+ item.prdFeatureTemplateName + ':' + item.value + '</option>'
									} else {
										type2 = {
												prdFeatureTemplateName:	productTemplete[i].prdFeatureTemplateName,
												value:	productTemplete[i].value
										};
									}
								}
								if($('#' + formId + ' .form-group').hasClass('hide')) {
									$('#' + formId + ' .form-group').removeClass('hide');
									//初始化chosen插件
									$('#batchForm .dis-select').chosen({
										allow_single_deselect: true, //删除选项
										max_selected_options: 999, //多选上限
										disable_search:true, //禁用搜索框
										placeholder_text_multiple:'请选择产品特性' //提示值
									}); 
									
									/* $('#batchForm .dis-select').on('chosen:ready', function(e, params) {
										$(".dis-select").val(whiteList)//设置值  
										$('.dis-select').trigger('chosen:updated');//更新选项  
										console.log('准备就绪')
									}); */
									
									//获取select的内容
									$('#batchForm .dis-select').on('change', function(e, params) {
									 console.log($(this).val())
									});
								} 
								
								$('#' + formId + ' input[name="product"]').val(JSON.stringify(proObj));
								$('#' + formId + ' input[name="productName"]').val(proObj.productName);
								$('#' + formId + ' input[name="product"]').attr('data-id', proObj.id);
								$('#' + formId + ' input[name="packageAmount"]').val(type2.value);
								$('#' + formId + ' span[name="packageAmountName"]').html(type2.value);
								$('#' + formId + ' select[name="productTemplete"]').html(type1Html);
								$('#' + formId + ' select[name="productTemplete"]').trigger("chosen:updated")
								layer.close(lindex);
							} else {
								layer.msg('请选择一个产品');
							}
							return false;
						}
					});
					$('#' + idPrefix + 'Chosen .i-checks').iCheck({
					    checkboxClass: 'icheckbox_square-green',
					    radioClass: 'iradio_square-green',
					});
					if(dataId) {
						$('#' + idPrefix + 'Chosen .iradio_square-green.checked').removeClass('checked');
					}
					$('#' + idPrefix + 'Chosen input[data-id="' + dataId + '"]').parent().addClass('checked');
					$('#' + idPrefix + 'Chosen .filter').attr('id', idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').attr('data-filter', '#' + idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').footable();
					$('#' + idPrefix + 'Chosen #product-view').attr('id-prefix', idPrefix);
				});
			}
		}
	});
	
}

/**
 * 选择一条产品线
 * @param self 本体
 * @param idPrefix 唯一标识符前缀
 */
function choseAProductionLine(self, idPrefix, formId){
	var dataId = $(self).attr('data-id'),
		formId = formId ? formId : $(self).parent().parent().parent().attr('id'),
		productionLineTpl = productionLineDatas.innerHTML,//数据模板
		productionLineView = document.getElementById('productionLine-view');//数据视图容器
	
	loading = layer.load();	
	$.ajax({
		url: basePath + 'productionLine/loadProductionLine',
		type: 'get',
		success:function(result){
			layer.close(loading);
			if (result.success) {
				laytpl(productionLineTpl).render(result.data, function(html){
					productionLineView.innerHTML = html;
					//form的id只能实时生成，单页面应用需要保证id的唯一性。
					layer.open({
						type : 1,
						title : '选择一条产品生产线',
						skin : 'layui-layer-rim', //加上边框
						area : [ '60%', '480px' ], //宽高
						content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#productionLineChosenTpl').html() + '</div>',
						btn : [ '已选择', '取消' ],
						yes : function(lindex, layero) {
							var productionLine = $('#' + idPrefix + 'Chosen .iradio_square-green.checked').find('input[name="chosenProductionLine"]').val(),
								productionLineObj = (productionLine && productionLine.length > 0) ? JSON.parse(productionLine) : null;
								
							if (productionLineObj) {
								$('#' + formId + ' input[name="productionLine"]').val(productionLine);
								$('#' + formId + ' input[name="productionLineName"]').val(productionLineObj.name);
								$('#' + formId + ' input[name="productionLineName"]').attr('data-id', productionLineObj.id);
								layer.close(lindex);
							} else {
								layer.msg('请选择一条产品线');
							}
							return false;
						}
					});

					$('#' + idPrefix + 'Chosen .i-checks').iCheck({
					    checkboxClass: 'icheckbox_square-green',
					    radioClass: 'iradio_square-green',
					});
					if(dataId) {
						$('#' + idPrefix + 'Chosen .iradio_square-green.checked').removeClass('checked');
					}
					$('#' + idPrefix + 'Chosen input[data-id="' + dataId + '"]').parent().addClass('checked');
					$('#' + idPrefix + 'Chosen .filter').attr('id', idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').attr('data-filter', '#' + idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').footable();	
					$('#' + idPrefix + 'Chosen #productionLine-view').attr('id-prefix', idPrefix);
				});
			}
		}
	});
}
/***
 * 选择一个二维码规则
 * @param self
 * @param idPrefix
 * @param formId
 */
function choseACodeRule(self, idPrefix, formId){
	var dataId = $(self).attr('data-id'),
		formId = formId ? formId : $(self).parent().parent().parent().attr('id'),
		codeRuleTpl = codeRuleDatas.innerHTML,//数据模板
		codeRuleView = document.getElementById('codeRule-view');//数据视图容器

	loading = layer.load();	
	$.ajax({
		url: basePath + 'codingCenter/loadAllCodeRule',
		type: 'get',
		success:function(result){
			layer.close(loading);
			if (result.success) {
				laytpl(codeRuleTpl).render(result.codes, function(html){
					codeRuleView.innerHTML = html;
					
					//form的id只能实时生成，单页面应用需要保证id的唯一性。
					layer.open({
						type : 1,
						title : '选择二维码生成规则',
						skin : 'layui-layer-rim', //加上边框
						area : [ '60%', '460px' ], //宽高
						content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#codeRuleChosenTpl').html() + '</div>',
						btn : [ '已选择', '取消' ],
						yes : function(lindex, layero) {
							var selectEl = $('.iradio_square-green.checked').find('input[name="chosenCodeRule"]'),
								codeId = selectEl.attr('data-id'),
								codeRuleName = selectEl.attr('data-name');
								
							if (codeId) {
								$('#' + formId + ' input[name="codeRuleId"]').val(codeId);
								$('#' + formId + ' input[name="codeRuleName"]').val(codeRuleName);
								$('#' + formId + ' input[name="codeRuleId"]').attr('data-id', codeId);
								console.log('选择了一个规则',codeRuleName)
								layer.close(lindex);
							} else {
								layer.msg('请选择一个生成规则',{icon:5});
							}
							return false;
						}
					});
					$('#' + idPrefix + 'Chosen .i-checks').iCheck({
					    checkboxClass: 'icheckbox_square-green',
					    radioClass: 'iradio_square-green',
					});
					if(dataId) {
						$('#' + idPrefix + 'Chosen .iradio_square-green.checked').removeClass('checked');
					}
					$('#' + idPrefix + 'Chosen input[data-id="' + dataId + '"]').parent().addClass('checked');
					$('#' + idPrefix + 'Chosen .filter').attr('id', idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').attr('data-filter', '#' + idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').footable();
					$('#' + idPrefix + 'Chosen #codeRule-view').attr('id-prefix', idPrefix);
				});
			}
		}
	});
}

/**
 * 选择一个工控机
 * @param self 本体
 * @param idPrefix 唯一标识符前缀
 */
function choseAIpc(self, idPrefix, formId){
	var dataId = $(self).attr('data-id'),
		formId = formId ? formId : $(self).parent().parent().parent().attr('id'),
		ipcTpl = ipcDatas.innerHTML,//数据模板
		ipcView = document.getElementById('ipc-view');//数据视图容器
	
	loading = layer.load();	
	$.ajax({
		url: basePath + 'ipc/loadIpc',
		type: 'get',
		success:function(result){
			layer.close(loading);
			if (result.success) {
				laytpl(ipcTpl).render(result.data.resultList, function(html){
					ipcView.innerHTML = html;
					//form的id只能实时生成，单页面应用需要保证id的唯一性。
					layer.open({
						type : 1,
						title : '选择一个工控机',
						skin : 'layui-layer-rim', //加上边框
						area : [ '60%', '400px' ], //宽高
						content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#ipcChosenTpl').html() + '</div>',
						btn : [ '已选择', '取消' ],
						yes : function(lindex, layero) {
							var ipc = $('#' + idPrefix + 'Chosen .iradio_square-green.checked').find('input[name="chosenIpc"]').val(),
								ipcObj = (ipc && ipc.length > 0) ? JSON.parse(ipc) : null;
								
							if (ipcObj) {
								$('#' + formId + ' input[name="ipcId"]').val(ipcObj.id);
								$('#' + formId + ' input[name="ipcId"]').attr('data-id', ipcObj.id);
								layer.close(lindex);
							} else {
								layer.msg('请选择一个工控机',{icon:5});
							}
							return false;
						}
					});

					$('#' + idPrefix + 'Chosen .i-checks').iCheck({
					    checkboxClass: 'icheckbox_square-green',
					    radioClass: 'iradio_square-green',
					});
					if(dataId) {
						$('#' + idPrefix + 'Chosen .iradio_square-green.checked').removeClass('checked');
					}
					$('#' + idPrefix + 'Chosen input[data-id="' + dataId + '"]').parent().addClass('checked');
					$('#' + idPrefix + 'Chosen .filter').attr('id', idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').attr('data-filter', '#' + idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').footable();	
					$('#' + idPrefix + 'Chosen #ipc-view').attr('id-prefix', idPrefix);
				});
			}
		}
	});
}
/***
 * 选择一个产品码
 * @param self 本体
 * @param idPrefix 唯一标识符前缀
 * @param productID 产品码id
 * @param batchID 批次id
 * @param productCount 预计生产数量
 */
function choseAProductCode(self, idPrefix, formId, productID, batchID, productCount){
	var dataId = $(self).attr('data-id'),
		formId = formId ? formId : $(self).parent().parent().parent().attr('id'),
		ipcTpl = productCodeDatas.innerHTML,//数据模板
		ipcView = document.getElementById('productCode-view');//数据视图容器
	
	loading = layer.load();	
	$.ajax({
		url: basePath + 'qrcodeExport/loadProductCode',
		type: 'get',
		data:{
			productId: productID
		},
		success:function(result){
			result =JSON.parse(result);
			layer.close(loading);
			
			if (result.success) {
				laytpl(ipcTpl).render(result.data, function(html){
					ipcView.innerHTML = html;
					//form的id只能实时生成，单页面应用需要保证id的唯一性。
					layer.open({
						type : 1,
						title : '选择一个产品码',
						skin : 'layui-layer-rim', //加上边框
						area : [ '60%', '400px' ], //宽高
						content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#productCodeChosenTpl').html() + '</div>',
						btn : [ '确认导出', '取消' ],
						yes : function(lindex, layero) {
							var productCode = $('#' + idPrefix + 'Chosen .iradio_square-green.checked').find('input[name="chosenProductCode"]').val(),
								productCodeObj = (productCode && productCode.length > 0) ? JSON.parse(productCode) : null,
								genCount = $('#' + idPrefix + 'Chosen .genCount').val();
								
							if (productCodeObj) {
								productCodeObj.batchId = batchID ? batchID : null;
								if(!genCount){
									layer.msg('请输入二维码导出数量',{icon: 5});
								} else {
									if (genCount > 0) {
										var url = basePath + '/codingCenter/generateCodes';
										
										productCodeObj.genCount = genCount;
										console.log("productCodeObj",productCodeObj)
										url = url+'?ruleId=' + productCodeObj.ruleId + '&codeName=' + productCodeObj.codeName + '&batchId=' + productCodeObj.batchId + '&genCount=' + productCodeObj.genCount;
										console.log("genCount:",genCount,";productCount:",productCount,parseInt(genCount) > parseInt(productCount))
										if(parseInt(genCount) > parseInt(productCount)){
											layer.alert('导出数量('+ parseInt(genCount) +')大于预计生产量('+ productCount +'),继续导出?',{icon: 3}, function(){
												layer.closeAll();
												window.location.href =  url;
					            			});
										} else {
											layer.alert('确认导出'+ parseInt(genCount) +'个产品码?',{icon: 3}, function(){
												layer.closeAll();
												window.location.href =  url;
					            			});
											
										}
									} else {
										layer.msg('请输入大于0的整数',{icon: 5});
									}
								}
							} else {
								layer.msg('请选择一个产品码',{icon: 5});
							}
							return false;
						}
					});

					$('#' + idPrefix + 'Chosen .i-checks').iCheck({
					    checkboxClass: 'icheckbox_square-green',
					    radioClass: 'iradio_square-green',
					});
					if(dataId) {
						$('#' + idPrefix + 'Chosen .iradio_square-green.checked').removeClass('checked');
					}
					$('#' + idPrefix + 'Chosen input[data-id="' + dataId + '"]').parent().addClass('checked');
					$('#' + idPrefix + 'Chosen .filter').attr('id', idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').attr('data-filter', '#' + idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').footable();	
					$('#' + idPrefix + 'Chosen #productCode-view').attr('id-prefix', idPrefix);
				});
			}
		},
		error:function(){
    		setTimeout(function(){
    			layer.alert('抱歉，资源访问失败',{
    				closeBtn: 0
    			},
    			function(){
	    			layer.close(loading);
	   				history.go(-1);
	    			layer.closeAll('dialog');
    			}
    		);
    		},1000)
    	}
	});
}

/***
 * 选择一个设备
 * @param self 本体
 * @param idPrefix 唯一标识符前缀
 */
function choseADevice(self, idPrefix, formId){
	var dataId = $(self).attr('data-id'),
		formId = formId ? formId : $(self).parent().parent().parent().attr('id'),
		deviceTpl = deviceDatas.innerHTML,//数据模板
		deviceView = $('#device-view');//数据视图容器
	
	
	loading = layer.load();	
	$.ajax({
		url: basePath + 'device/loadDevice',
		type: 'get',
		success:function(result){
			layer.close(loading);
			if (result.success) {
				var modeDevicesList = getProductionModeDevices();
					renderData = [];
				for(var i = 0; i < result.data.resultList.length; i++){
					var isChosen = false;
					for(var j = 0; j<modeDevicesList.length; j++){
						if(modeDevicesList[j].device.id == result.data.resultList[i].id){
							if(result.data.resultList[i].id != dataId){
								isChosen = true;
							}
						}
					}
					if(!isChosen) {
						renderData.push(result.data.resultList[i])
					}
				}
				console.log('modeDevicesList',modeDevicesList);
				laytpl(deviceTpl).render(renderData, function(html){
					deviceView.html(html);
					//form的id只能实时生成，单页面应用需要保证id的唯一性。
					layer.open({
						type : 1,
						title : '选择一个设备',
						skin : 'layui-layer-rim', //加上边框
						area : [ '60%', '400px' ], //宽高
						content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#deviceChosenTpl').html() + '</div>',
						btn : [ '已选择', '取消' ],
						yes : function(lindex, layero) {
							var device = $('#' + idPrefix + 'Chosen .iradio_square-green.checked').find('input[name="chosenDevice"]').val(),
								deviceObj =  (device && device.length > 0) ? JSON.parse(device) : null;
							console.log('deviceObj',deviceObj)
							console.log('formId',formId)
							if (deviceObj) {
								$('#' + formId + ' input[name="deviceName"]').val(deviceObj.deviceNumber+'  '+deviceObj.model);
								$('#' + formId + ' input[name="deviceName"]').attr('data-id', deviceObj.id);
								$('#' + formId + ' input[name="device"]').val(device);
								layer.close(lindex);
							} else {
								layer.msg('请选择一个设备');
							}
							return false;
						}
					});
					$('#' + idPrefix + 'Chosen .i-checks').iCheck({
					    checkboxClass: 'icheckbox_square-green',
					    radioClass: 'iradio_square-green',
					});
					console.log('dataId',dataId)
					if(dataId) {
						$('#' + idPrefix + 'Chosen .iradio_square-green.checked').removeClass('checked');
					}
					$('#' + idPrefix + 'Chosen input[data-id="' + dataId + '"]').parent().addClass('checked');
					$('#' + idPrefix + 'Chosen .filter').attr('id', idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').attr('data-filter', '#' + idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').footable();
					$('#' + idPrefix + 'Chosen #shop-view').attr('id-prefix', idPrefix);
				});
			}
		}
	});
}

/***
 * 选择一个生产模式
 * @param self 本体
 * @param idPrefix 唯一标识符前缀
 */
function choseAProductionMode(self, idPrefix, formId, params){
	var dataId,
		formId = formId ? formId : $(self).parent().parent().parent().attr('id'),
		productionModeTpl = productionModeDatas.innerHTML,//数据模板
		productionModeView = $('#productionMode-view');//数据视图容器
	
	loading = layer.load();
	if(params.modeType == 1){
		dataId = $('#' + formId + ' input[name="printProductionModeName"]').attr('data-id');
	} else if(params.modeType == 2){
		dataId = $('#' + formId + ' input[name="scanProductionModeName"]').attr('data-id');
	}
	$.ajax({
		url: basePath + 'productionMode/findProductionMode',
		type: 'get',
		data: {
			productionLineId:params.productionLineId,
			modeType:params.modeType
		},
		success:function(result){
			layer.close(loading);
			if(typeof result == 'string'){
				result = JSON.parse(result);
			}
			if (result.success) {
				laytpl(productionModeTpl).render(result.data, function(html){
					productionModeView.html(html);
					
					//form的id只能实时生成，单页面应用需要保证id的唯一性。
					layer.open({
						type : 1,
						title : params.modeType==1?'选择一个打印_生产模式':'选择一个校验_生产模式',
						skin : 'layui-layer-rim', //加上边框
						area : [ '60%', '480px' ], //宽高
						content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#productionModeChosenTpl').html() + '</div>',
						btn : [ '已选择', '取消' ],
						yes : function(lindex, layero) {
							var productionMode = $('#' + idPrefix + 'Chosen .iradio_square-green.checked').find('input[name="chosenProductionMode"]').val(),
								productionModeObj =  (productionMode && productionMode.length > 0) ? JSON.parse(productionMode) : null;
							if (productionModeObj) {
								if(params.modeType == 1){
									$('#' + formId + ' input[name="printProductionModeName"]').val(productionModeObj.productionModeName);
									$('#' + formId + ' input[name="printProductionModeName"]').attr('data-id', productionModeObj.id);
									$('#' + formId + ' input[name="printProductionMode"]').val(productionModeObj.id);
								} else if(params.modeType == 2) {
									$('#' + formId + ' input[name="scanProductionModeName"]').val(productionModeObj.productionModeName);
									$('#' + formId + ' input[name="scanProductionModeName"]').attr('data-id', productionModeObj.id);
									$('#' + formId + ' input[name="scanProductionMode"]').val(productionModeObj.id);
								}
								layer.close(lindex);
							} else {
								layer.msg('请选择一个生产模式');
							}
							return false;
						}
					});
					$('#' + idPrefix + 'Chosen .i-checks').iCheck({
					    checkboxClass: 'icheckbox_square-green',
					    radioClass: 'iradio_square-green',
					});
					if(dataId) {
						$('#' + idPrefix + 'Chosen .iradio_square-green.checked').removeClass('checked');
					}
					$('#' + idPrefix + 'Chosen input[data-id="' + dataId + '"]').parent().addClass('checked');
					$('#' + idPrefix + 'Chosen .filter').attr('id', idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').attr('data-filter', '#' + idPrefix + 'Filter');
					$('#' + idPrefix + 'Chosen .footable').footable();
					$('#' + idPrefix + 'Chosen #shop-view').attr('id-prefix', idPrefix);
				});
			} else {
				layer.msg(result.message?result.message:'获取数据失败',{icon:5});
			}
		}
	});
}



