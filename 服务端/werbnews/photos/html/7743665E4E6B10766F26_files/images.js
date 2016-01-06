(function(window, Util, PhotoSwipe){
			
			Util.Events.domReady(function(e){
				
				var instance;

				var lastindex = 0;

				var laststartx = 0;

				var swipeleft = false;
				
				instance = PhotoSwipe.attach(
					imagedata,
					{
						target: window.document.querySelectorAll('#PhotoSwipeTarget')[0],
						captionAndToolbarHide: true,
						enableMouseWheel: false,
						preventHide: true,
						getImageSource: function(obj){
							return obj.url;
						},
						getImageCaption: function(obj){
							return obj.caption;
						},
						loop:false
					}
				);
				instance.show(0);

				instance.addEventHandler(PhotoSwipe.EventTypes.onDisplayImage, function(e){
						$("#description").html(instance.getCurrentImage().caption);
						$("#currentNum").html(e.index+1);
						$("#currentImageUrl").val(instance.getCurrentImage().src);
						
						lastindex = e.index;
					});
				instance.addEventHandler(PhotoSwipe.EventTypes.onTouch, function(e){
					if( e.action=='touchStart' )
						laststartx = e.point.x;
					if( ( e.action=='touchMoveEnd' && e.point.x<laststartx ) || e.action == 'swipeLeft' ){
						if( (lastindex+1)== imagedata.length ){
							$("#moreimages").css("top","0px");
							$("#moreimages").css("left",$("#icontent").css("width"));
							//$("#moreimages").css("width",$("#icontent").css("width"));
							$("#moreimages").css("height","99%");
							$("#moreimages").show();
							
							 $("#moreimages").animate({left:"0px"},"fast");
						}
					}
				});
				
			});
			
			
		}(window, window.Code.Util, window.Code.PhotoSwipe));



	var fixgeometry = function() {
		scroll(0, 0);
		var footer = $("ifooter");
		var contentiframe = $("#PhotoSwipeTarget");
		var viewport_height = $(window).height();
		var content_height = viewport_height  - 90;
		var iframe_height = content_height -10;
		contentiframe.css("height", iframe_height + "px");
		
		var width = $(window).width();
		var numwidth = $("#totalNum").outerWidth();
		$("#description").css("width",width-numwidth-3+"px");
	};

//jquery mobile 横竖变化
$(window).on("orientationchange", function(event) {
	fixgeometry();
});

var maxtitle = 18;

$(function() {
	$( "body" ).append( "<input type='hidden' id='currentImageUrl' value='"+imagedata[0].url+"'/>" );
	fixgeometry();
	var u = window.location.search;
	var num=u.indexOf("?");
	if( num>=0 ){
		str=u.substr(num+1,3);
		if( str=='out' )
			$("#ifooter").append("<div style='width:100%;padding:30px 0;margin:0;text-align:center;'><a class='button' style='width:80%' href='http://zhbj.qianlong.com/static/download.htm'>&nbsp;&nbsp;<img style='margin:0;vertical-align:middle' border='0' width='25' src='http://zhbj.qianlong.com/logo.png'/>&nbsp;<span style='vertical-align:middle'>下载智慧北京客户端</span>&nbsp;&nbsp;</a></div>");
	}


	var morediv=$("<div class='ui-grid-a' style='text-align:center;padding:3px 0' id='moreimages'></div>");
	morediv.append("<div id='moreheader'>推荐</div>");
	$("body").append(morediv);
	var u = window.location.pathname;
	$.getJSON("/static/api/news/newestimages.json",function(result){
		var count = 0;
		var maxcount=6;
		result.data.news.sort(function(){ return 0.5 - Math.random(); }); 
		for(i=0;i<result.data.news.length;i++){
			var field = result.data.news[i];
			if(count>=maxcount)
				break;
			if( field.url.indexOf(u)>0 )
				continue;
			var column;
			if(count%2==0){
				column = $("<div class='ui-block-a' style='text-align:center;padding:3px 0'></div>");
			}else{
				column = $("<div class='ui-block-b' style='text-align:center;padding:3px 0'></div>");
			}
			au = $("<a href='javascript:;' style='transparent;position: relative;'></a>");
			au.append("<img height='"+($(window).height()*3/10-6)+"' src='"+field.smallimage+"'/>");
			//column.data("url",field.url);
			title = field.title;
			if(title.length>maxtitle)
				title = title.substring(0,maxtitle)+"...";
			//column.append("<div class='moretitle'>"+title+"</div>");
			au.append("<div class='moretitlep'>"+title+"</div>");
			column.append(au);
			au.data("url",field.url);
			au.click(function(){
				self.location = $(this).data("url");
			});
			$("#moreimages").append(column);
			count++;
		}
	});
	$("#moreimages").bind("swiperight",function(){
		$(this).animate({left:$("#icontent").css("width")},"fast",function(){
			$(this).hide();
		});
	});

});