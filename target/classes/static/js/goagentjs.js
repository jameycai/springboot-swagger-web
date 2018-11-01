// JavaScript Document

$(document).ready(function(){//页面加载完之后，自动执行该方法
	"use strict";
	var H = $(window).height();
	$(".content").css("min-height", H-100);
});
$(function(){
	"use strict";
	$(".golevel1 > a").click(function(){
		$(this).parents(".golevel1").toggleClass("active");
		if($(this).parents(".golevel1").hasClass("active")===true){
			$(this).children(".fa").removeClass("fa-angle-left").addClass("fa-angle-down");
		}else{
			$(this).children(".fa").removeClass("fa-angle-down").addClass("fa-angle-left");
		}
	});
	$(".golevel2 > li").click(function(){
		$(this).addClass("current");
		$(this).siblings("li").removeClass("current");
		$(this).parents(".golevel2").parents(".golevel1").siblings(".golevel1").children(".golevel2").children("li").removeClass("current");
	});
});


$(function(){
	"use strict";
	$(".select>p>a").click(function(){
		$(this).parent("p").next("ul").toggleClass("show");
		if($(this).hasClass("fa-angle-down")){
			$(this).removeClass("fa-angle-down").addClass("fa-angle-up");
		}else{
			$(this).removeClass("fa-angle-up").addClass("fa-angle-down");
		}
		
	});
	$(".selectlst>li").click(function(){
		$(this).addClass("current");
		$(this).siblings("li").removeClass("current");
		var V = $(this).html();
		/*alert(V);*/
		V = V.replace("^<*span>*</span>",null);
		/*new RegExp("^<*span>*</span>").test(V);*/
		var l = V.indexOf("<");
		if(l !== -1){
			V = V.substr(0,l);
		}
		/*alert(V);*/
		if($(this).parent("ul").parent("div").hasClass("selectbtn-box")===true){
			var oNav_span=$(this).parent("ul").parent("div").siblings("p").find("span").length;
			if(oNav_span==1){
				$(this).parent("ul").parent("div").siblings("p").children("span").html(V);
			}else{
				$(this).parent("ul").parent("div").siblings("p").children("input").val(V);
			}
			$(this).parent("ul").parent("div").removeClass("show");
			$(this).parent("ul").parent("div").siblings("p").children("a").removeClass("fa-angle-up").addClass("fa-angle-down");	
		}
		var oNav_span=$(this).parent("ul").siblings("p").find("span").length;
		if(oNav_span==1){
			$(this).parent("ul").siblings("p").children("span").html(V);
		}else{
			$(this).parent("ul").siblings("p").children("input").val(V);
		}
		$(this).parent("ul").removeClass("show");
		$(this).parent("ul").siblings("p").children("a").removeClass("fa-angle-up").addClass("fa-angle-down");
	});

});
$(function(){
	"use strict";
	$(".select>p>a").click(function(){
		$(this).parent("p").next("div").toggleClass("show");	
		
	});
	$(".selectbtn").click(function(){
		$(this).parent("div").removeClass("show");
		$(this).parent("div").siblings("p").children("a").removeClass("fa-angle-up").addClass("fa-angle-down");
		
	});
	$(".adversearch >p>a").click(function(){
		$(this).children(".adverbox").toggleClass("show");	
		
	});
});

$(function(){
	"use strict";
	$(".check-box").click(function(){
		$(this).toggleClass("checked");	
		
	});
	$(".choice > li > .ico-choice").click(function(){
		$(this).addClass("checked");
		$(this).parent("li").siblings("li").children(".ico-choice").removeClass("checked");
	});
});