var productionModeTpl = productionModeDeviceDatas.innerHTML,
		productionModeView;
	
	function modeDevices(idPrefix,p){
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
					title : '【'+p.productionLineName+'-'+p.productionModeName+'】校验生产模式设备明细',
					skin : 'layui-layer-rim', //加上边框
					area : [ '60%', '400px' ], //宽高
					content : '<div class="wrapper-content" id="' + idPrefix + 'Chosen">' + $('#productionModeDeviceTpl').html() + '</div>',
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
				productionModeView = $('#' + idPrefix + 'Chosen #productionModeDevice-view');
				renderDevices(modeDevicesList);
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
					savePMItem(addObj,list);
				})
				$('#' + idPrefix + 'Chosen .reset').on('click',function(){//重置
					$('#' + idPrefix + 'Chosen form select[name="mateGroup"]').val('');
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
	//新增或编辑产品码
	function savePMItem(saveObj,deviceList) {
		var el = '#productionModeDevicesChosen',
			appendEl = null,
			appendHtml = '',
			wuliumaLen = 0,
			deviceDoubleIndex = -1,//设备重复时，存放这一项的index
			group = [];//存放与添加项相同的分组
			
		//信息不完整的判断
		if (saveObj.mateGroup.trim().length == 0) {
			layer.msg('请填写分组',{icon:5})
			return false;	
		}
		if (saveObj.device.trim().length == 0) {
			layer.msg('请选择设备',{icon:5})
			return false;
		}
		saveObj.device = JSON.parse(saveObj.device);
		
		for(var i = 0; i < deviceList.length; i++){
			if(deviceList[i].device.id == saveObj.device.id){
				deviceDoubleIndex = i;
			}
			if(saveObj.mateGroup == deviceList[i].mateGroup){
				group.push(deviceList[i])
			}
		}
		if(deviceDoubleIndex == -1){
			if(group.length == 0){
				deviceList.push(saveObj);
				renderDevices(deviceList);
			} else {
				if(saveObj.number == 1){//主码
					var oneIndex = -1;
					for(var i =0; i < deviceList.length; i++){
						if(deviceList[i].number == 1 && deviceList[i].mateGroup==saveObj.mateGroup){
							oneIndex = i;
						}
					}
					if(oneIndex == -1){
						deviceList.push(saveObj)
					} else {
						if(saveObj.index == deviceList[oneIndex].index){
							deviceList[oneIndex] = saveObj;
						} else {
							layer.msg('每一组只能有一个主码',{icon:5})
						}
					}
					
				} else {
					if(saveObj.index == ''){
						deviceList.push(saveObj);	
					} else {
						deviceList[saveObj.index-1] = saveObj;
					}
					renderDevices(deviceList);
				}	
			}
			
		} else {
			if(saveObj.index != deviceList[deviceDoubleIndex].index){
				layer.msg('设备重复，请重新配置',{icon:5})
				return false;
			} else {
				deviceList[deviceDoubleIndex] = saveObj;
				renderDevices(deviceList)
			}
		}
	}
	//渲染生产模式设备的列表
	function renderDevices(data){
		var length  = data.length,
			groupId = 0,
			result = [],
			item,
			list,
			el = '#productionModeDevicesChosen';
		if(length > 0){
			for(var i = 0; i < length; i++) {
				data[i].deviceStr = JSON.stringify(data[i].device);
				
				if(result.length > 0){
					var tempIndex = -1
					for(var n=0; n < result.length; n++){
						if(result[n].mateGroup == data[i].mateGroup){
							tempIndex = n;
							break;
						}
					}
					if(tempIndex != -1){
						result[tempIndex].list.push(data[i])	
					} else {
						item = {
								mateGroup:data[i].mateGroup,
								list:[data[i]]
						}
						result.push(item)
					}
				} else {
					item = {
							mateGroup:data[0].mateGroup,
							list:[data[0]]
					}
					result.push(item)
				}	
			}
			laytpl(productionModeTpl).render(result, function(html){
				productionModeView.html(html);
				$(el + ' .edit').on('click',function(){//绑定编辑事件
					var item = $(this).parent().parent(),
						data = {
							id:item.attr('data-id'),
							index:item.attr('index'),
							mateGroup:item.attr('mateGroup'),
							device:JSON.parse(item.attr('device')),
							number:item.attr('number')
						};
					editProductionModeDevice(data)
				})
				
				$(el + ' .remove').on('click',function(){//绑定删除事件
					var item = $(this).parent().parent();
					layer.confirm('确认删除?', {
				      icon: 3,
				      btn: ['确认','取消'] //按钮
				    }, function(lcindex){
				    	item.remove();
				    	layer.close(lcindex);
				 
				    	renderDevices(getProductionModeDevices())
				    }, function(){
				    });
				})
			})
			
		} else {
			productionModeView.html('<tr><td>暂无数据</td></tr>');
		}
		$(el +'.footable').removeClass('footable-loaded');
		$(el +'.footable').footable();
		initPMManageForm({len:result.length})
	}
	//编辑
	function editProductionModeDevice(data){
		var el = '#productionModeDevicesChosen';
		$(el + ' form input[name="id"]').val(data.id);
		$(el + ' form input[name="index"]').val(data.index);
		$(el + ' form input[name="device"]').val(JSON.stringify(data.device));
		$(el + ' form input[name="deviceName"]').val(data.device.deviceNumber + "  " + data.device.model);
		$(el + ' form input[name="deviceName"]').attr('data-id',data.device.id);
		$(el + ' form select[name="mateGroup"]').val(data.mateGroup);
		$(el + ' form .change').html('确认编辑')
	}
	//获取弹框中的生产模式设备列表
	function getProductionModeDevices(productionModeId) {
		var i = 0,
			el = '#productionModeDevicesChosen',
			trEls = $(el + ' table tbody tr.datas'),
			list = [];
		
		if (trEls.length > 0) {
			for (i = 0; i < trEls.length; i++) {
				list.push({
					id: $(trEls[i]).attr('data-id'),
					index: $(trEls[i]).attr('index'),
					mateGroup: $(trEls[i]).attr('mateGroup')?$(trEls[i]).attr('mateGroup'):'',
					device: $(trEls[i]).attr('device')?JSON.parse($(trEls[i]).attr('device')):'',
					number: $(trEls[i]).attr('number'),
					productionMode: {id:(productionModeId?productionModeId:$(trEls[i]).attr('productionModeId'))}
				});
			}
		}
		return list;
	}
	//设备明细form渲染
	function initPMManageForm(params){
		var el = '#productionModeDevicesChosen',
			html = '<option value="">请选择分组</option>';
		for(var i = 1; i <= params.len; i++) {
			html += '<option value="' + i + '">第'+ i +'组</option>'
		}
		html += '<option value="' + (params.len+1) + '">新建分组</option>'
		$(el + ' form select[name="mateGroup"]').html(html);
		$(el + ' form select[name="mateGroup"]').val('');
		$(el + ' form input[name="device"]').val('');
		$(el + ' form input[name="deviceName"]').val('');
		$(el + ' form input[name="deviceName"]').removeAttr('data-id');	
		$(el + ' form .change').html('添加')
	}
	
	//保存生产模式设备明细
	function saveProductionModeDevices(data){
		var option = {
				data:{jsonStr:JSON.stringify(data)},
				url:base+'productionModeDevice/saveProductionModeDevice'
			};
		ajaxRequest(option, function(result){
			if(typeof result == 'string'){
				result = JSON.parse(result)
			}
			if(result.success){
				layer.closeAll();
				layer.msg('保存成功',{icon:6});
				goPage('index');
			} else {
				layer.msg(result.message?result.message:'保存失败',{icon:5})
			}
		})
	}
	//向上移动设备
	function deviceMoveUp(index,mateGroup){
		var list = getProductionModeDevices(),
			groupList = [],
			firstIndex = -1,
			item;
			
		index -= 1;
		
		for(var i=0; i < list.length; i++){
			if(mateGroup == list[i].mateGroup ){
				if(firstIndex == -1){
					firstIndex = i
				}
				groupList.push(list[i])
			}
		}
		item = groupList[index];
		
		list[index+firstIndex] = list[index+firstIndex-1];
		list[index+firstIndex-1] = item;
		renderDevices(list)
	}
	//向下移动设备
	function deviceMoveDown(index,mateGroup){
		var list = getProductionModeDevices(),
			groupList = [],
			firstIndex = -1,
			item ;
		index -= 1;
		for(var i=0; i < list.length; i++){
			if(mateGroup == list[i].mateGroup ){
				if(firstIndex == -1){
					firstIndex = i
				}
				groupList.push(list[i])
			}
		}
		item = groupList[index];
		list[index+firstIndex] = list[index+firstIndex+1];
		list[index+firstIndex+1] = item;
		renderDevices(list)
	}
	
	//发布生产模式
	function publishproductionMode(id){
		var option = {
				data:{id:id},
				url:base+'productionMode/publishProductionMode'
		};
		ajaxRequest(option, function(result){
			if(typeof result == 'string'){
				result = JSON.parse(result)
			}
			if(result.success){
				layer.msg('发布成功',{icon:6});
				goPage('index');
			} else {
				layer.msg(result.message?result.message:'发布失败',{icon:5})
			}
		})
		
	}
	