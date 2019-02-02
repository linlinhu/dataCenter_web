var configDeviceRender = [],
	mainCode = null;

$("#printPlanForm").steps({
    bodyTag: "fieldset",
    onStepChanging: function (event, currentIndex, newIndex) {
    	var formObj = $('#printPlanForm').serializeObject(),
    		params = {};
    	if(newIndex == 1) {
    		if (formObj.deviceType == "") {
    			layer.msg('请选择用途后再进行下一步', {icon: 5});
    			return false;
    		}
    	}
    	if(newIndex == 2) {
   			//获取产品信息和生产线的信息
   			if (formObj.productName == "" || formObj.productionLineName == "") {
       			layer.msg('请完善必要因素后再进行下一步', {icon: 5});
       			return false;
   			}else{
   				//loading = layer.load();
   	   			formObj.product = JSON.parse(formObj.product);
   	   			formObj.productionLine = JSON.parse(formObj.productionLine);
   	   			params = {
   	   					deviceType: formObj.deviceType,
   	   					productNumber: formObj.product.productNumber,
   	   					lineNumber: formObj.productionLine.lineNumber
   	   			}
   	   			$.ajax({
   	   		    	url: basePath + 'productPrintPlan/getroductPrintPlanCount',
   	   		    	async: false,
   	   		    	data: params,
   	   		    	type: "post",
   	   		    	success:function(data){
   	   		    		layer.close(loading);
   	   		    		console.log('查询结果',data)
   	   		    		if (!data.success){
   	   		    			layer.msg(data.message ? data.message + "，请再次尝试！": '查询失败，请再次尝试！', {icon: 5});
   	   		    			return false;
   	   		    		} else {
   	   		    			if (data.count > 0) {
	   	   		    			layer.confirm(formObj.deviceType == 1 ? '打码方案存在，是否要删除？': '扫码方案存在，是否要删除？', {
	   	   		    				btn: ['确定','取消'], //按钮
	   	   		    				closeBtn: 0
		   	   		    		}, function(){
			   	   		    		$.ajax({
		   	   		   	   		    	url: basePath + 'productPrintPlan/delProductPrintPlan',
		   	   		   	   		    	async: false,
		   	   		   	   		    	data: params,
		   	   		   	   		    	type: "post",
		   	   		   	   		    	success:function(data){
		   	   		   	   		    		layer.close(loading);
		   	   		   	   		    		console.log('删除查询结果',data)
		   	   		   	   		    		if (!data.success){
		   	   		   	   		    			layer.msg(data.message ? data.message : '删除当前方案失败', {icon: 5});
		   	   		   	   		    			return false;
		   	   		   	   		    		} else {
		   	   		   	   		    			layer.msg(data.message ? data.message : '删除当前方案成功', {icon: 6});
		   	   		   	   		    			getProductCode(formObj);
		   	   		   	   		    			//$("#printPlanForm").steps({bodyTag: "fieldset",next:true})
		   	   		   	   		    		}
		   	   		   	   		    	}
		   	   		   	   		    });
		   	   		    		}, function(){
		   	   		    			layer.closeAll('dialog');
		   	   		    			layer.msg(formObj.deviceType == 1 ? '打码方案存在，请返回上一步重新选择': '扫码方案存在，请返回上一步重新选择', {icon: 5});
		   	   		    			return false;
		   	   		    		});
   	   		    			} else {
   	   		    				getProductCode(formObj);
   	   		    				//$("#printPlanForm").steps({bodyTag: "fieldset",next:true})
   	   		    			}
   	   		    		}
   	   		    	}
   	   		    });
   			}
    	}
    	return true;
    },
    onFinished: function (event, currentIndex) {
    	submitPrintPlan();
    },
    labels: {
        finish: "保存配置"
    }
       
});

function printCode(){
	var configDeviceTpl = configDeviceDatas.innerHTML,//数据模板
		configDeviceView = document.getElementById('configDevice-view');//数据视图容器
	
	laytpl(configDeviceTpl).render(configDeviceRender, function(configDeviceHtml){
		configDeviceView.innerHTML = configDeviceHtml;
		if (!$('#codingCenter-scanCode').hasClass('hide')) {
				$('#codingCenter-scanCode').addClass('hide');
		}

		if ($('#codingCenter-printCode').hasClass('hide')) {
				$('#codingCenter-printCode').removeClass('hide');
			
		}
	});
}

function scanCode(){
	$('#codingCenter-scanCode .verify-group-lst').html('');
	if ($('#codingCenter-scanCode').hasClass('hide')) {
		$('#codingCenter-scanCode').removeClass('hide');
	}

	if (!$('#codingCenter-printCode').hasClass('hide')) {
			$('#codingCenter-printCode').addClass('hide');
		
	}
	loadScanCodes(configDeviceRender);
}

function loadScanCodes(codes) {
	var scanPcTpl = scanProductCodesData.innerHTML,//数据模板
	scanPcView = document.getElementById('scanProductCodes-view');//数据视图容器

	laytpl(scanPcTpl).render(codes, function(html){
		scanPcView.innerHTML = html;
	});
}

function submitPrintPlan(){
	var i= 0,
		trEl = null,
		trLen = 0,
		curDeviceName = null,
		submitArr = [],
		formObj = $('#printPlanForm').serializeObject(),
		deviceType = formObj.deviceType,
		productionLine = formObj.productionLine,
		productCode = null,
		device = null,
		ppObj = null;
	
	if (deviceType == 1) {
		trEl = $('#configDevice-view tr');
		trLen = trEl.length;
	    if (trLen == 0) {
	    	layer.msg('无配置信息，不能提交！', {icon: 5})
	    	return false;
	    }
	    if (formObj.packCount == '' || formObj.packCount < 0) {
	    	layer.msg('请输入packCount', {icon: 5});
	    	return false;
	    }
	    for (i = 0; i < trLen; i++) {
	    	curDeviceName =  $('#configDevice-view tr input[name="deviceName' + i + '"]').val();
	    	if (curDeviceName == "") {
	        	layer.msg('请完善方案配置！', {icon: 5})
	    		return false;
	    	}
	    }
	
	    for (i = 0; i < trLen; i++) {
	    	productCode = JSON.parse($(trEl[i]).attr('productCode'));
	    	device = JSON.parse($(trEl[i]).attr('device'));
	    	ppObj = {
	    		deviceType: deviceType,
	    		productCode: productCode,
	    		device: device,
	    		productionLine: JSON.parse(productionLine),
	    		packCount: productCode.codeType == 2 ? formObj.packCount : 0
	    	};
	    	submitArr.push(ppObj);
	    }
	    loading = layer.load();
		$.ajax({
	    	url:  basePath + 'productPrintPlan/saveProductPrintPlan',
	    	data: {
	    		jsonStr: JSON.stringify(submitArr)
	    	},
	    	type: "post",
	    	success:function(data){
	    		layer.close(loading);
	    		if (!data.success){
	    			layer.msg(data.message ? data.message : '保存失败！', {icon: 5});
	    		} else {
	    			layer.msg('保存成功！', {icon: 6});
	    			goPage('index');
	    		}
	    	}
	    });
	} else {
		var j = 0,
			groupEls = $('.verify-group-lst .verify'),
			groupLen = groupEls.length,
			eachProductCode = null,
			isMain = 0,
			mainCodeConfig = null;
		
		if (groupLen == 0) {
			layer.msg('请至少添加一个校验分组！', {icon: 5});
			return false;
		}
		
		if ($('#codingCenter-scanCode .oper').length > 0) {
			var groupTitle = $($('#codingCenter-scanCode .oper').parent().parent()).find('.title').html();
			layer.msg('请保存【' + groupTitle + '】！', {icon: 5});
			return false;
		}
		for (i = 0; i < groupLen; i++) {
			trEls = $(groupEls[i]).find('tr');
			for (j = 0; j < trEls.length; j++) {
				
				if (i > 0 && j == 0) {
					if (mainCodeConfig) {
						mainCodeConfig.verifyGroup = i + 1;
						submitArr.push(mainCodeConfig);
					}
				}
				
				if (i == 0 || j > 0) {
					eachProductCode = $($(trEls[j]).find('select.scan-chose-code')[0]).val();
					isMain = $($(trEls[j]).find('select[name="isMain"]')[0]).val();
					productCode = JSON.parse($($(trEls[j]).find('select.scan-chose-code option[value="' + eachProductCode + '"]')[0]).attr('productcode'));
					device = JSON.parse($($(trEls[j]).find('input[class="device"]')[0]).val());
					if (isMain == 1) {
						mainCodeConfig = {
								deviceType: deviceType,
		    	        		productCode: productCode,
		    	        		isMain: isMain,
		    	        		device: device,
		    	        		productionLine: JSON.parse(productionLine)
						}
					}
					submitArr.push({
		        		deviceType: deviceType,
		        		productCode: productCode,
		        		isMain: isMain,
		        		device: device,
		        		productionLine: JSON.parse(productionLine),
		        		verifyGroup: i + 1
		        	});
				}
				
			}
		}
			
		if (!mainCodeConfig) {
			layer.msg('未生成主码，不能保存！', {icon: 5});
			return false;
		}
		console.dir(submitArr);
		if (submitArr.length < configDeviceRender.length) {
			layer.msg('请完善该产品所有的产品码配置！', {icon: 5});
			return false;
		}
		save({jsonStr: JSON.stringify(submitArr)}, 'productPrintPlan');
	}
}
/***
 * 获取产品码
 */
function getProductCode(formObj) {
	loading = layer.load();	
	$.ajax({
		url: basePath + 'productPrintPlan/getProductCode',
		data: {
			productId: formObj.product.id
		},
		type: 'get',
		success:function(result){
			layer.close(loading);
			result = JSON.parse(result);
			if (result.success) {
				var i = 0,
					codingCenterCodes = result.data,
					curCode = null;
				configDeviceRender = [];
				if (codingCenterCodes.length == 0) {
					layer.msg('当前产品无码可配置，请移步产品管理添加产品码，或者重新选择一个产品', {icon: 0});
				}
				for (i = 0; i < codingCenterCodes.length; i++) {
					curCode = codingCenterCodes[i];
					if(formObj.deviceType == 2 && curCode.codeType != 2) {
						configDeviceRender.push({
							lineName: formObj.productionLine.name,
							productName: formObj.product.productName,
							productCodeName: curCode.codeName,
							productCodeId: curCode.id,
							productCode: JSON.stringify(curCode),
							disabled: 0
						})
					}
					if(formObj.deviceType == 1) {
						configDeviceRender.push({
							lineName: formObj.productionLine.name,
							productName: formObj.product.productName,
							productCodeName: curCode.codeName,
							codeType: curCode.codeType == 1 ? '物联码' : '物流码',
							productCodeId: curCode.id,
							productCode: JSON.stringify(curCode),
							disabled: 0
						})
					}
				}
				if (formObj.deviceType == 1) {
					printCode();
				}
				if (formObj.deviceType == 2) {
					scanCode();
				}
			} else {
				layer.msg(result.message ? result.message : '加载产品码失败！', {icon: 5});
			}
		}
	});
}

function validateProductPlanConfig(formObj) {
	var params = {
		productId: formObj.product.id,
		productionLineId: formObj.productionLine.id
	};
	loading = layer.load();	
	$.ajax({
		url: basePath + 'productPrintPlan/validate',
		data: params,
		type: 'get',
		success:function(result){
			layer.close(loading);
			if (result.success) {
    			getProductCode(formObj);
			} else {
				layer.confirm('该产品线和产品的配置，在数据库中已经存在，是否重新配置？', {
			      	icon: 2,
			      	btn: ['确认','取消'] //按钮
			    }, function(){
			    	loading = layer.load();
			    	$.ajax({
			        	url:  basePath + 'productPrintPlan/deleteRepeat',
			        	data: params,
			        	type:"post",
			        	success:function(data){
			        		layer.close(loading);
			        		if (!data.success){
			        			layer.msg(data.message ? data.message : '重置失败！', {icon: 5});
			        		} else {
			        			layer.msg('重置成功！', {icon: 6});
			        			getProductCode(formObj);
			        		}
			        	}
			        });
			    },function(){
					layer.close(loading);
			    });
			}
		}
	});
}



function choosePrintPlanUse(self, use) {
	$('.print-plan-use button').each(function(){
		if ($(this).hasClass('btn-primary')) {
			$(this).removeClass('btn-primary').addClass('btn-default');
		}
	})
	$(self).addClass('btn-primary').removeClass('btn-default');
	$('#printPlanForm input[name="deviceType"]').val(use);
}

function resetDeviceConfig(index) {
	$('#printPlanForm input[name="device' + index + '"]').val('');
	$('#printPlanForm input[name="deviceName' + index + '"]').val('');
	$('#printPlanForm input[name="deviceName' + index + '"]').attr('data-id', '');
}

function setScanCodeMain(self) {
	var val = $(self).val();
	
	if(val == 1) {
		var trs = $($(self).parent().parent().parent().find('tr'));
		for (var i = 0; i < trs.length; i++) {
			$(trs[i]).find('select[name="isMain"]').val(0);
		}
		$(self).val(1);
	} 
}

//添加校验分组
function addVerifyGroup() {
	var el = '#codingCenter-scanCode dl.verify-group-lst',
		ddLen = $(el + ' dd').length;
	if (ddLen > 0) {
		if (configDeviceRender.length <= 2) {
			layer.msg('产品码不足以构成多组校验!', {icon: 5});
			return false;
		}
		
		if (mainCode == null) {
			layer.msg('请完善第一组校验信息!', {icon: 5});
			return false;
		}

	}
	
	if ($('#codingCenter-scanCode .oper').length > 0) {
		var groupTitle = $($('#codingCenter-scanCode .oper').parent().parent()).find('.title').html();
		layer.msg('请保存【' + groupTitle + '】！', {icon: 5});
		return false;
	}
	
	if ($('#scanProductCodes-view select[name="codeName"] option').length <= 1) {
		if (mainCode == null) {
			layer.msg('无发添加，产品码配置项为空！', {icon: 5})
		} else {
			layer.msg('您已经完成配置，请保存配置！', {icon: 5});
		}
		return false;
	}
	$(el).append($('#verifyGroupTemplate').html());

	ddLen = $(el + ' dd').length;
	$($(el + ' dd')[ddLen - 1]).attr('index', ddLen);
	$(el + ' dd[index="' + ddLen + '"] .title').html('校验分组-第' + ddLen + '组');
	if($(el + ' dd').length > 0) {//自动生成主码
		$(el + ' dd[index=' + $(el + ' dd').length + '] .verify').html('<tr index=1>' +
		    			'<td><select class="form-control" disabled="disabled" name="codeName">' +
		    			'<option value="wusuoweile">' + mainCode.codeText + '</option></select></td>' +
		    			'<td><select class="form-control" name="isMain" disabled="disabled"><option value="1">是</option></select></td>'+
		    			'<td><input class="form-control deviceName" readonly="" disabled="disabled" type="text" value="' + mainCode.deviceName + '"></td>'+
				'</tr>');
	}

}
//添加一个校验
function addAVerify(self) {
	var el = '#codingCenter-scanCode dl.verify-group-lst',
		index = $(self).parent().parent().parent().parent().parent().attr('index'),
		verifyEl = $(el + ' dd[index = ' + index + '] .verify'),
		verifyTrIndex = verifyEl.find('tr').length + 1,
		switchClassName = 'vgl-' + index + '-switch-' + verifyTrIndex;
	
	filterRepeatCodeChosen(index);
	if ($('#scanProductCodes-view select[name="codeName"] option').length <= 1) {
		layer.msg('已没有产品码可配置！', {icon: 5})
		return false;
	}
	if (verifyTrIndex > 1) {
		//验证信息完整
		var completeIndex = verifyTrIndex-1,
			completeTr = verifyEl.find('tr[index="' + completeIndex + '"]'),
			productCode = $(completeTr).find('select[name="codeName"]').val(),
			isMain = $(completeTr).find('select[name="isMain"]').val(),
			deviceName = $(completeTr).find('input.deviceName').val();
			
		if(!productCode||!deviceName){
			layer.msg('请完善校验信息!', {icon: 5});
			return false;
		}
		$(completeTr).find('select.scan-chose-code').attr('disabled','true');
		$(completeTr).find('input.deviceName').attr('disabled','true');
	} 
	verifyEl.append('<tr index="' + verifyTrIndex + '">' +
						'<td index="' + index + verifyTrIndex + '">' +
							$('#scanProductCodes-view').html() +
						'</td>' +
						'<td>' +
							'<select class="form-control" name="isMain" onchange="setScanCodeMain(this)"><option value="0">否</option><option value="1">是</option></select>' +
						'</td>' +
						'<td>' +
							'<input type="hidden" class="device" name="device' + index + verifyTrIndex + '">' +
                  	'<input class="form-control deviceName" type="text" name="deviceName' + index + verifyTrIndex + '" onclick="configADevice(this, ' + index + verifyTrIndex + ', \'scan\')" readonly>'+
						'</td>' +
					'</tr>');
	
	if (mainCode) {
		verifyEl.find('tr[index="' + verifyTrIndex + '"] select[name="isMain"]').attr('disabled', 'true');
		verifyEl.find('tr[index="' + verifyTrIndex + '"] select[name="isMain"]').attr('onchange', '');
	}
	
}

function delAVerify(self) {
	var el = '#codingCenter-scanCode dl.verify-group-lst',
		index = $(self).parent().parent().parent().parent().parent().attr('index'),
		verifyEl = $(el + ' dd[index = ' + index + '] .verify'),
		verifyTrEls = verifyEl.find('tr');
	$(verifyTrEls[verifyTrEls.length-1]).remove();
	
}
//删除本组
function delVerifyGroup(self) {
	var index = $(self).parent().parent().parent().parent().parent().attr('index');
	layer.confirm('是否确定删除本组校验？', {
      icon: 2,
      btn: ['确认','取消'] //按钮
    }, function(lcindex){
		$(self).parent().parent().parent().parent().parent().remove();
		var i = 0,
			els = $('#codingCenter-scanCode dl.verify-group-lst dd');
		if (els.length > 0) {
			for (i = 0; i < els.length; i++) {
				$(els[i]).attr('index', i + 1);
			}
		} else {
			for (i = 0; i < configDeviceRender.length; i++) {
				configDeviceRender[i].disabled = 0;
			}
			loadScanCodes(configDeviceRender);
			mainCode = null;
		}

		filterRepeatCodeChosen(index);
		layer.close(lcindex);
	},function(lcindex){
		layer.close(lcindex);
	});
}
function confirmVerifyGroup(self) {
	var i = 0,
		el = '#codingCenter-scanCode dl.verify-group-lst',
		index = $(self).parent().parent().parent().parent().parent().attr('index'),
		verifyEl = $(el + ' dd[index = ' + index + '] .verify'),
		verifyTrIndex = verifyEl.find('tr').length + 1;
	
	if (verifyTrIndex < 2) {
		layer.msg('至少需要包含一组校验信息!', {icon: 5});
		return false;
	}
	
	if (configDeviceRender.length > 1 && verifyTrIndex < 3) {
		layer.msg('产品码个数大于2，每组配置不得小于2!', {icon: 5});
		return false;
	}
	
	//验证信息完整
	var completeIndex = verifyTrIndex-1,
		completeTr = verifyEl.find('tr[index="' + completeIndex + '"]'),
		productCode = $(completeTr).find('select[name="codeName"]').val(),
		isMain = $(completeTr).find('select[name="isMain"]').val(),
		deviceName = $(completeTr).find('input.deviceName').val();
		
	if(completeTr && (!productCode||!deviceName)){
		layer.msg('请完善校验信息!', {icon: 5});
		return false;
	}
	
	if (mainCode==null) {
		var isMainEls = $(verifyEl.find('select[name="isMain"]')),
			mainIndex = 0;
		for (i = 0; i < isMainEls.length; i++) {
			if($(isMainEls[i]).val() == 1 ) {
				mainIndex = $($(isMainEls[i]).parent().parent()).attr('index');
			}
		}
		if(mainIndex == 0) {
			layer.msg('初次保存分组，必须要选择一个主码 !', {icon: 5});
			return false;
		}
	}

	layer.confirm('保存当前组的配置，保存后不可编辑修改，确认继续吗？', {
      icon: 0,
      btn: ['确认','取消'] //按钮
    }, function(lcindex){
		layer.close(lcindex);
		
		$(completeTr).find('select.scan-chose-code').attr('disabled','true');
		$(completeTr).find('input.deviceName').attr('disabled','true');
		if (index == 1) {
			
			for (i = 0; i < isMainEls.length; i++) {
				$(isMainEls[i]).attr('disabled','true');
			}
			var mainObjEl = $(verifyEl.find('tr[index="' + mainIndex + '"]'));
			mainCode = {};
			mainCode.codeText = mainObjEl.find('select.scan-chose-code option[value="' + mainObjEl.find('select.scan-chose-code').val() + '"]').text();
			mainCode.deviceName = mainObjEl.find('input.deviceName').val();
		}
		filterRepeatCodeChosen(index);
		$(self).parent().parent().find('.oper').remove();
	},function(lcindex){
		layer.close(lcindex);
	});
	
}





//过滤重复选择
function filterRepeatCodeChosen(index) {
	var i = 0,
		j = 0,
		codes = configDeviceRender,
		delCodes = [],
		codeEls = $('.verify-group-lst .scan-chose-code'),
		codeElVals = [],
		trEls =  $('.verify-group-lst dd[index="' + index + '"] .verify tr'),
		curIndex = $(trEls[trEls.length-1]).attr('index');
	
	for (j = 0; j < codes.length; j++) {
		for (i = 0; i < codeEls.length; i++) {
			if (codes[j].productCodeId == $(codeEls[i]).val()) {
				delCodes.push(j);//将要删除的码
			}
		}
	}
	for (i = 0; i < delCodes.length; i++) {
		j = delCodes[i];
		codeElVals.push(codes[j].productCodeId);
		codes[j].disabled = 1;
	}
	loadScanCodes(codes);

	
	if (curIndex) {
		for (i = 0; i < codes.length; i++) {
			codes[i].disabled = 0;
		}
		
		if (codeEls.length > 1 && mainCode == null) {
			codeEls.each(function(index){
				for(i = 0; i < codeElVals.length; i++) {
					if (i != index && curIndex != index) {
						var oldValue = 	$(codeEls[index]).val();
						for (j = 0; j < codes.length; j++) {
							if (codes[j].productCodeId == codeElVals[i]) {
								codes[j].disabled = 1;
							}
						}
						var tipOptionsHtml = '<option value="">-选择产品码-</option>',
							optionsHtml = '';
						for (j = 0; j < codes.length; j++) {
							if (codes[j].disabled == 0) {
								optionsHtml += '<option value="' + codes[j].productCodeId + 
								'" productCode=\'' + codes[j].productCode + 
								'\'>' + codes[j].productCodeName + 
								'</option>';
							}
						}
						if (optionsHtml == '') {
							$(codeEls[index]).parent().parent().remove();
						} else {
							$(codeEls[index]).html(tipOptionsHtml + optionsHtml);
							$(codeEls[index]).val(oldValue);

						}
					}
				}

				for (j = 0; j < codes.length; j++) {
					codes[j].disabled = 0;
				}
				
			});
		}
	}
}

function configADevice(self, index, flag){
	var dataId = $(self).attr('data-id');
		deviceTpl = deviceDatas.innerHTML,//数据模板
		deviceView = document.getElementById('device-view');//数据视图容器

	loading = layer.load();	
	$.ajax({
		url: basePath + 'device/loadDevice?keyword='+flag,
		type: 'get',
		success:function(result){
			layer.close(loading);
			if (result.success) {
				laytpl(deviceTpl).render(result.data.resultList, function(html){
					deviceView.innerHTML = html;
					//form的id只能实时生成，单页面应用需要保证id的唯一性。
					layer.open({
						type : 1,
						title : '选择一个设备',
						skin : 'layui-layer-rim', //加上边框
						area : [ '60%', '400px' ], //宽高
						content : '<div class="wrapper-content" id="ppForm-deviceChosen">' + $('#deviceChosenTpl').html() + '</div>',
						btn : [ '已选择', '取消' ],
						yes : function(lindex, layero) {
							var	i = 0,
								disabledEls = $('#ppForm-deviceChosen .iradio_square-green.disabled');
							for (i = 0; i < disabledEls.length; i++) {
								$(disabledEls[i]).removeClass('checked');
							}
							
							var device = $('#ppForm-deviceChosen .iradio_square-green.checked').find('input[name="chosenDevice"]').val(),
								deviceObj = (device && device.length > 0) ? JSON.parse(device) : null;
								
							if (deviceObj) {
								$('#printPlanForm input[name="deviceName' + index + '"]').val(deviceObj.deviceNumber);
								$('#printPlanForm input[name="deviceName' + index + '"]').attr('data-id', deviceObj.id);
								if (flag == 'print') {
									$('#configDeviceTable tbody tr[index="' + index + '"]').attr('device', device);
								}
								if (flag == 'scan') {
									$('#printPlanForm input[name="device' + index + '"]').val(device);
								}
								layer.close(lindex);
							} else {
								layer.msg('请选择一个设备', {icon: 5});
							}
							return false;
						}
					});
					$('#ppForm-deviceChosen .i-checks').iCheck({
					    checkboxClass: 'icheckbox_square-green',
					    radioClass: 'iradio_square-green',
					});
					var dnEls = null;
					if (flag == 'print') {
						dnEls = $('#configDevice-view .deviceName');
					} else {
						dnEls = $('#codingCenter-scanCode .deviceName');
					}
					
					for (i = 0; i < dnEls.length; i++) {
						dataId = $(dnEls[i]).attr('data-id');
						if (dataId) {
							$('#ppForm-deviceChosen input[data-id="' + dataId + '"]').parent().addClass('disabled');
						}
					}

					$('#ppForm-deviceChosen .filter').attr('id', 'ppFormDeviceFilter' + flag);
					$('#ppForm-deviceChosen .footable').attr('data-filter', '#ppFormDeviceFilter' + flag);
					$('#ppForm-deviceChosen .footable').footable();
					$('#ppForm-deviceChosen #device-view').attr('id-prefix', 'ppForm-device');
				});
			}
		}
	});
}