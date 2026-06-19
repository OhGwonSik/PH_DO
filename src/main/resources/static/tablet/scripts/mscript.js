$(function(){
	getPageName();
	getUserName();
	//tablet mainSideBar
	document.querySelector('#tabletLogout').addEventListener('click', function(){
		logout();
	});

	document.querySelectorAll('.menu-bar a, .menu-body a').forEach(function (element){
		element.addEventListener('click', function(){
			let url = rPath + '/page/tablet';
			url += '/' + element.dataset.progurl;
			url += '?pagename=' + element.dataset.title;
			location.href=url;
		});
	});
    //사이드 메뉴

    $(".btn-menu").on('click',function(){
        $("#modal-background").fadeIn(300);
        $("#mainMenu").animate({left: '0'},300);
        $("body").css("overflow", "hidden");
    });

    //사이드 메뉴 닫기

    $(".modal-close").on('click',function(){
        $("#modal-background").fadeOut(300);
        $(".modal-con").animate({left: '-360px'},300);
        $('body').css('overflow', 'overlay');
    });

     $('#pdaConfirmCancel').on('click', function(){
		$('.popup').css('top', '-100%');
		$('.bg').hide();
	 });

	function getPageName(pageName){

		/*const activeLocation = window.location.toString();
		const pageId = activeLocation.substring(activeLocation.lastIndexOf('/')+1);

		// main화면 가르기 위함
		if(!pageId.startsWith('d')){
			return;
		}

		const pageNameObj = userMenu.find(function(element){
			return element.progrid === pageId;
		});

		document.querySelector('.title').innerHTML = pageNameObj.menunam;*/
	}

	function getUserName(){
		document.querySelector('#userName').innerHTML = userAttr.usernam;
	}
})
