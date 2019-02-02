	var printProductionModeDeviceTpl = printProductionModeDeviceDatas.innerHTML,
		printProductionModeView;
	
	function printModeDevices(idPrefix,p){
		var modeDevicesList = [],
			option = {
				url:base+'productionModeDevice/findProductionModeDevice',
				data:{productionModeId:p.id}
			};
		ajaxRequest(option, function(result){
			if(typeof result == 'string'){
				result = JSON.parse(result)
			}
			if(result.success){
				modeDevicesList = result.data;
				layer.open({
					type : 1,
					title : '【'+p.productionLineName+'-'+p.productionModeName+'】打印生产模式设备明细',
					skin : 'layui-layer-rim', //加上边框
					area : [ '60%', '400px' ], //宽高
					content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#printProductionModeDeviceTpl').html() + '</div>',
					btn : [ '保存', '取消' ],
					yes : function(lindex, layero) {
						modeDevicesList = getProductionModeDevices(p.id);
						if (modeDevicesList.length > 0) {
							layer.confirm('确认保存?', {
							      icon: 3,
							      btn: ['确认','取消'] //按钮
							    }, function(lcindex){
							    	saveProductionModeDevices(modeDevicesList);
							    }, function(){
							  });
							
						} else {
							layer.msg('请设置模式设备',{icon:5});
						}
						return false;
					}
				});
				printProductionModeView = $('#' + idPrefix + 'Chosen #printProductionModeDevice-view');
				renderPrintDevices(modeDevicesList);
				$('#' + idPrefix + 'Chosen form input[name="productionModeId"]').val(p.id);
				$('#' + idPrefix + 'Chosen .filter').attr('id', idPrefix + 'Filter');
				$('#' + idPrefix + 'Chosen .footable').attr('data-filter', '#' + idPrefix + 'Filter');
				$('#' + idPrefix + 'Chosen .footable').footable();
				
				$('#' + idPrefix + 'Chosen .change').on('click',function(){//添加
					var addObj = submitObj =$('#' + idPrefix + 'Chosen form').serializeObject()
						list = getProductionModeDevices();
					addObj.productionMode = {
						id:addObj.productionModeId
					}
					console.log('printModeDevices addObj',addObj)
					savePrintItem(addObj,list);
				})
				$('#' + idPrefix + 'Chosen .reset').on('click',function(){//重置
					$('#' + idPrefix + 'Chosen form input[name="device"]').val('');
					$('#' + idPrefix + 'Chosen form input[name="deviceName"]').val('');
					$('#' + idPrefix + 'Chosen form input[name="deviceName"]').removeAttr('data-id');
					$('#' + idPrefix + 'Chosen .change').html('添加')
				})
				
			} else {
				layer.msg(result.message?result.message:'获取生产模式设备失败',{icon:5});
			}
		})	 
	}
	
	//新增或编辑打印生产模式
	function savePrintItem(saveObj,deviceList) {
		var el = '#productionModeDevicesChosen',
			appendEl = null,
			appendHtml = '',
			wuliumaLen = 0,
			deviceDoubleIndex = -1;//设备重复时，存放这一项的index
			
		if (saveObj.device.trim().length == 0) {
			layer.msg('请选择设备',{icon:5})
			return false;
		}
		saveObj.device = JSON.parse(saveObj.device);
		
		for(var i = 0; i < deviceList.length; i++){
			if(deviceList[i].device.id == saveObj.device.id){
				deviceDoubleIndex = i;
			}
		}
		console.log('saveObj',saveObj)
		console.log('saveObj',saveObj)
		if(deviceDoubleIndex == -1){	
			if(saveObj.index == ''){
				deviceList.push(saveObj);	
			} else {
				deviceList[saveObj.index-1] = saveObj;
			}
			renderPrintDevices(deviceList);	
		} else {
			if(saveObj.index != deviceList[deviceDoubleIndex].index){
				layer.msg('设备重复，请重新配置',{icon:5})
				return false;
			} else {
				deviceList[deviceDoubleIndex] = saveObj;
				renderPrintDevices(deviceList)
			}
		}
	}
	
	//渲染打印生产模式设备的列表
	function renderPrintDevices(data){
		var el = '#productionModeDevicesChosen';
		if(data.length > 0){
			for(var i = 0; i < data.length; i++) {
				data[i].deviceStr = JSON.stringify(data[i].device);
			}
			laytpl(printProductionModeDeviceTpl).render(data, function(html){
				printProductionModeView.html(html);
				$(el + ' .edit').on('click',function(){//绑定编辑事件
					var item = $(this).parent().parent(),
						data = {
							id:item.attr('data-id'),
							index:item.attr('index'),
							device:JSON.parse(item.attr('device')),
							number:item.attr('number')
						};
					editPrintProductionModeDevice(data)
				})
				
				$(el + ' .remove').on('click',function(){//绑定删除事件
					var item = $(this).parent().parent();
					layer.confirm('确认删除?', {
				      icon: 3,
				      btn: ['确认','取消'] //按钮
				    }, function(lcindex){
				    	item.remove();
				    	layer.close(lcindex);
				 
				    	renderPrintDevices(getProductionModeDevices())
				    }, function(){
				    });
				})
			})
			
		} else {
			printProductionModeView.html('<tr><td>暂无数据</td></tr>');
		}
		$(el +'.footable').removeClass('footable-loaded');
		$(el +'.footable').footable();
		initPPMManageForm()
	}
	//编辑
	function editPrintProductionModeDevice(data){
		var el = '#productionModeDevicesChosen';
		$(el + ' form input[name="id"]').val(data.id);
		$(el + ' form input[name="index"]').val(data.index);
		$(el + ' form input[name="device"]').val(JSON.stringify(data.device));
		$(el + ' form input[name="deviceName"]').val(data.device.deviceNumber + "  " + data.device.model);
		$(el + ' form input[name="deviceName"]').attr('data-id',data.device.id);
		$(el + ' form .change').html('确认编辑')
	}
	
	//设备明细form渲染
	function initPPMManageForm(params){
		var el = '#productionModeDevicesChosen';
			html = '<option value="">请选择分组</option>';
		$(el + ' form input[name="device"]').val('');
		$(el + ' form input[name="deviceName"]').val('');
		$(el + ' form input[name="deviceName"]').removeAttr('data-id');	
		$(el + ' form .change').html('添加')
	}
	
	//向上移动设备
	function printMoveUp(index,mateGroup){
		var list = getProductionModeDevices(),
			item = list[index-1];
			
		index -= 1;
		list[index] = list[index-1];
		list[index-1] = item;
		renderPrintDevices(list)
		
	}
	//向下移动设备
	function printMoveDown(index,mateGroup){
		var list = getProductionModeDevices(),
			item = list[index-1];
			
		index -= 1;
		list[index] = list[index+1];
		list[index+1] = item;
		renderPrintDevices(list)
	}