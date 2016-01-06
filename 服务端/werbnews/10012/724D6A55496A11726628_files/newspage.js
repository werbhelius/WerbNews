(function($,window,document,undefined){var $window=$(window);$.fn.lazyload=function(options){var elements=this;var $container;var settings={threshold:0,failure_limit:0,event:"scroll",effect:"show",container:window,data_attribute:"original",skip_invisible:true,appear:null,load:null};function update(){var counter=0;elements.each(function(){var $this=$(this);if(settings.skip_invisible&&!$this.is(":visible")){return}if($.abovethetop(this,settings)||$.leftofbegin(this,settings)){}else if(!$.belowthefold(this,settings)&&!$.rightoffold(this,settings)){$this.trigger("appear");counter=0}else{if(++counter>settings.failure_limit){return false}}})}if(options){if(undefined!==options.failurelimit){options.failure_limit=options.failurelimit;delete options.failurelimit}if(undefined!==options.effectspeed){options.effect_speed=options.effectspeed;delete options.effectspeed}$.extend(settings,options)}$container=(settings.container===undefined||settings.container===window)?$window:$(settings.container);if(0===settings.event.indexOf("scroll")){$container.bind(settings.event,function(event){return update()})}this.each(function(){var self=this;var $self=$(self);self.loaded=false;$self.one("appear",function(){if(!this.loaded){if(settings.appear){var elements_left=elements.length;settings.appear.call(self,elements_left,settings)}$("<img />").bind("load",function(){$self.hide().attr("src",$self.data(settings.data_attribute))[settings.effect](settings.effect_speed);self.loaded=true;var temp=$.grep(elements,function(element){return!element.loaded});elements=$(temp);if(settings.load){var elements_left=elements.length;settings.load.call(self,elements_left,settings)}}).attr("src",$self.data(settings.data_attribute))}});if(0!==settings.event.indexOf("scroll")){$self.bind(settings.event,function(event){if(!self.loaded){$self.trigger("appear")}})}});$window.bind("resize",function(event){update()});if((/iphone|ipod|ipad.*os 5/gi).test(navigator.appVersion)){$window.bind("pageshow",function(event){if(event.originalEvent.persisted){elements.each(function(){$(this).trigger("appear")})}})}$(window).load(function(){update()});return this};$.belowthefold=function(element,settings){var fold;if(settings.container===undefined||settings.container===window){fold=$window.height()+$window.scrollTop()}else{fold=$(settings.container).offset().top+$(settings.container).height()}return fold<=$(element).offset().top-settings.threshold};$.rightoffold=function(element,settings){var fold;if(settings.container===undefined||settings.container===window){fold=$window.width()+$window.scrollLeft()}else{fold=$(settings.container).offset().left+$(settings.container).width()}return fold<=$(element).offset().left-settings.threshold};$.abovethetop=function(element,settings){var fold;if(settings.container===undefined||settings.container===window){fold=$window.scrollTop()}else{fold=$(settings.container).offset().top}return fold>=$(element).offset().top+settings.threshold+$(element).height()};$.leftofbegin=function(element,settings){var fold;if(settings.container===undefined||settings.container===window){fold=$window.scrollLeft()}else{fold=$(settings.container).offset().left}return fold>=$(element).offset().left+settings.threshold+$(element).width()};$.inviewport=function(element,settings){return!$.rightoffold(element,settings)&&!$.leftofbegin(element,settings)&&!$.belowthefold(element,settings)&&!$.abovethetop(element,settings)};$.extend($.expr[':'],{"below-the-fold":function(a){return $.belowthefold(a,{threshold:0})},"above-the-top":function(a){return!$.belowthefold(a,{threshold:0})},"right-of-screen":function(a){return $.rightoffold(a,{threshold:0})},"left-of-screen":function(a){return!$.rightoffold(a,{threshold:0})},"in-viewport":function(a){return $.inviewport(a,{threshold:0})},"above-the-fold":function(a){return!$.belowthefold(a,{threshold:0})},"right-of-fold":function(a){return $.rightoffold(a,{threshold:0})},"left-of-fold":function(a){return!$.rightoffold(a,{threshold:0})}})})(jQuery,window,document);

function trim(str){
	return str.replace(/(^\s*)|(\s*$)/g, "");
}

$(document).ready(function() {
	var u = window.location.search;
			var num=u.indexOf("?");
			if( num>=0 ){
				str=u.substr(num+1,3);
				if( str=='out' )
					$("body").append("<div style='width:100%;padding:30px 0;margin:0;text-align:center;'><a class='button' style='width:80%' href='http://zhbj.qianlong.com/static/download.htm'>&nbsp;&nbsp;<img style='margin:0;vertical-align:middle' border='0' width='25' src='http://zhbj.qianlong.com/logo.png'/>&nbsp;<span style='vertical-align:middle'>下载智慧北京客户端</span>&nbsp;&nbsp;</a></div>");
			}
	$( "body" ).append( "<input type='hidden' id='currentImageUrl'/>" );
	$("img").each(function() {
		var img = $(this);
		
		if(typeof(img.data("original")) == "undefined"){
			var source = img.attr("src");
			img.data("original",source);
			img.attr("src","http://zhbj.qianlong.com/images/noimage.png");
		}
		img.on("load", function(e){
		if (img.width() >= $(window).width()*0.92) {
			img.width("100%");
		}
		});

		img.click(function(){
			$("#currentImageUrl").val(img.data("original"));
		});
	});
	$("img").lazyload();
	var wcount=0;
	var needHide=false;
	$("p").each(function(){
		if(wcount>800 && needHide==false){
			needHide = true;
			$('#content').after("<div onclick='showHiddens();' id='expanda' style='text-align:center;margin:30px 0;padding:6px 0'><img style='vertical-align:middle' src='http://zhbj.qianlong.com/images/expand.png' /><span style='vertical-align:middle'>&nbsp;点击展开全文</span></div>");
		}

		var txt = $(this).html();
		var txt1 = $(this).text();
		var imgnum = $(this).find("img").size();
		if( txt=="&nbsp;" || (imgnum<1 && (txt1.length==1 && txt1.charCodeAt(0)==160|| trim(txt1)=="")))
			$(this).remove();
		else{
			$(this).css("padding","3px 0");
			if(txt1.length==0 && imgnum>0){
				$(this).css("text-indent","0");
			}
		}
		if(needHide)
			$(this).hide();
		wcount+=txt1.length;
	});
	if(wcount<=1000)
		showHiddens();
	
});

function showHiddens(){
	$("p").each(function(){
		$('#expanda').hide();
		$(this).show();
	});
}


	(function(window, PhotoSwipe){
		
		document.addEventListener('DOMContentLoaded', function(){
//			if( $("img").size()==1 ){
//				tmp = $("img").get(0);
//				var img = $(tmp);
//				var newimg = $('<img>',{'data-original':img.data("original"),'style':'display:none'});
//				$("body").append(newimg);
//				var newimg1 = $('<img>',{'data-original':img.data("original"),'style':'display:none'});
//				$("body").append(newimg1);
//			}else if( $("img").size()==2 ){
//				tmp0 = $("img").get(0);
//				var img0 = $(tmp0);
//				var newimg = $('<img>',{'data-original':img0.data("original"),'style':'display:none'});
//				$("body").append(newimg);
//
//				tmp1 = $("img").get(1);
//				var img1 = $(tmp1);
//				var newimg1 = $('<img>',{'data-original':img1.data("original"),'style':'display:none'});
//				$("body").append(newimg1);
//			}
			var options = {
				getImageSource: function(el){
					return el.getAttribute('data-original');
				},
				loop:false,
				allowUserZoom:false,
				autoStartSlideshow:false,
				captionAndToolbarHide:true,
				captionAndToolbarAutoHideDelay:0,
				captionAndToolbarOpacity:1,
				captionAndToolbarShowEmptyCaptions:false,
				captionAndToolbarFlipPosition:true,
				captionAndToolbarAutoHideDelay:4000
			},
			instance = PhotoSwipe.attach( window.document.querySelectorAll('#content img'), options );
			pswipe = instance;
			instance.addEventHandler(PhotoSwipe.EventTypes.onDisplayImage, function(e){
				$("#currentImageUrl").val(instance.getCurrentImage().src);
			});
			instance.addEventHandler(PhotoSwipe.EventTypes.onHide, function(e){
				$("#currentImageUrl").val("");
			});
			instance.addEventHandler(PhotoSwipe.EventTypes.onBeforeHide, function(e){
				var doms=$("p img");
				doms.css("pointer-events","none");
				window.setTimeout(function(){
					doms.css("pointer-events","auto");
				},500);
			});
			
		}, false);
	}(window, window.Code.PhotoSwipe));

