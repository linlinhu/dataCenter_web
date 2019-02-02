var cateFilter, cateTreeWrap;
function showCateTree() {
	var select = $('#' + cateFilter);
	var selectOffset = select.position();
	
	console.info(select.position().left); 
	$('#' + cateTreeWrap).css({left:selectOffset.left + "px", top:selectOffset.top + select.outerHeight() + "px"}).slideDown("fast");
	$("body").bind("mousedown", onBodyDown);
	
}

function hideCateTree() {
	$('#' + cateTreeWrap).fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);

}

function onBodyDown(event) {
	if (!(event.target.id == cateTreeWrap  || event.target.id == cateFilter || $(event.target).parents('#' + cateTreeWrap).length>0)) {
		hideCateTree();
	}
}
