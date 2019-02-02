/**
 * 初始化分类树
 */
function initCateTree(params, callback) {
	var diyDom = params.diyDom ? params.diyDom : null,
		setting = {
			view: {
				fontCss: function(treeId, treeNode) {
					return (!!treeNode.highlight) ? {color:"#5a98de", "font-weight":"bold"} : {color:"#333", "font-weight":"normal"};
				},
				addDiyDom: diyDom
			},
			data: {
				key: {
					title: "name"
				},
				simpleData: {
					enable: true
				}
			},
			callback: {
				onClick: function (event, treeId, treeNode, clickFlag) {
					callback(zTree, treeNode);
				},
				onCheck: function(event, treeId, treeNode, clickFlag) {
					callback(zTree, treeNode);
				},
				onAsyncSuccess: function (event, treeId, treeNode, msg) {
					expandNodes(zTree.getNodes());
				}
			}
		},
		zTree = null,
		searchNode = null,
		searchNodeList = [],
		updateNodeHighlight = null,
		checkList = params.checkList ? params.checkList : [];
	/**
	 * 展开节点
	 */
	function expandNodes(nodes) {
		for (var i = 0; i < nodes.length; i++) {
			for (var j = 0; j < checkList.length; j++) {
				if (nodes[i].id == checkList[j].id) {
					zTree.checkNode(nodes[i], true);
				}
			}
            zTree.expandNode(nodes[i], true); 
            if (nodes[i].isParent && nodes[i].zAsync) {//存在子级
            	expandNodes(nodes[i].children);//递归
            }
		} 
	}
	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
		}
		return childNodes;
	}

	
	if (params.check) {
		setting.check = params.check;
	}
	if (params.addDiyDom) {
		setting.view.addDiyDom = params.addDiyDom;
	}
	
	searchNode = function(val) {
		updateNodeHighlight(false);
		searchNodeList = zTree.getNodesByParamFuzzy('name', val, null);
		if (val != "") {
			updateNodeHighlight(true);
		}
	};
	
	updateNodeHighlight = function(isHighlight) {
		
		for( var i=0, l=searchNodeList.length; i<l; i++) {
			searchNodeList[i].highlight = isHighlight;
			zTree.updateNode(searchNodeList[i]);
		}
	}
	if (params.id && params.url) { 
		setting.async = {
			enable: true,
			url: params.url,
			autoParam:["id=parentId"],
			dataFilter: filter	
		}
		$.fn.zTree.init($("#" + params.id), setting);	
		zTree = $.fn.zTree.getZTreeObj(params.id);
		if (params.keyword) {
			$('#' + params.keyword).on('input', function(){
				searchNode($(this).val());
			}).on('propertychange', function(){
				searchNode($(this).val());
			});
		}
		
	}
	
}


function concatNodeName(ztree, node, showTitle) {
	if (node.parentTId) {
		node = ztree.getNodeByTId(node.parentTId);
		showTitle = node.name + '&nbsp;&gt;&nbsp;' + showTitle;
		return concatNodeName(ztree, node, showTitle)
	} else {
		return showTitle;
	}
}
