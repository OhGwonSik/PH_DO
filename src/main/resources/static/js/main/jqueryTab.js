/**
 * JQuery mainnavigationjtab
 */
var ggActivPG = 'Home';
$(document).ready(function() {

	$(document).on( "click", "#tabs a.tab", function( ) {  
		// Get the tab name
        const gprogrid = $(this).attr("id"); //a테크 아이디 
        //console.log('[js/main/1/jqueryTab.js] event : tab a.tab click id='+mid);
        const contentname = gprogrid;
        const mainHeaderKey = $(this).attr("mnhdkey");
		
        // hide all other tabs
        allHiddenTab();
        if(mainHeaderKey !== $('.main-menu>li.active').attr('id')) { 
        	$('.sidebar-menu-wrapper>details:not([class])').remove();
			$('details:not([class])').remove();
			displayMenu(mainHeaderKey);
		}
		$('.details-submenu>li.active').removeClass('active');
		setDetails(mainHeaderKey, gprogrid); 

        $('#home_btn_area').attr('class', 'ic-tab-home-btn');
		$('#home_btn_area img').attr('class', 'ic-tab-home');

        // show current tab
        $(".tab-contents.on").removeClass("on");
        $(".tab-contents." + contentname).show();
        $(".tab-contents." + contentname).addClass("on");
        $('.tab-menu.on').removeClass('on');
        $("#tabli_" + gprogrid ).addClass("on");

		const activeProgramList = JSON.parse(sessionStorage.activeProgramList);
		for(const activeProgram of activeProgramList) {
			activeProgram.active = activeProgram.progrid === gprogrid;
		}
		sessionStorage.activeProgramList = JSON.stringify(activeProgramList);
		
		history.pushState(null, null, rPath + $('li[progrid="' + gprogrid +'"]').attr('prgmurl'));
	    console.log(' -------------------------------  '  );
	    console.log('(click) : Active Program : ' + gprogrid );
	    ggActivPG = gprogrid;
	    console.log(' -------------------------------  '  );

	    // Tab bookmark ReSize
	    settabs();
	});
	
	$(document).on( "click", "#tabs img.remove", async function( ) {
    	// Get the tab name
        const gprormid = $(this).parent().find(".tab").attr("id");
        
        // remove tab and related content
        const contentname = gprormid;
        $(".tab-contents." + contentname).remove();
        $(this).parent().remove();
	    $('.details-submenu>li.active').removeClass('active');
		const activeProgramList = JSON.parse(sessionStorage.activeProgramList);

        // if there is no current tab and if there are still tabs left, show the first one
        if ($("#tabs li.on").length == 0 && $("#tabs li").length > 0) {
			
        	// find the first tab
            const firsttab = $("#tabs li:first-child");
	        const gprogrid = $(firsttab).find("a.tab").attr("id");
	        $('.tab-contents.'+ gprogrid).css('display',''); 
	        const firsttabHeaderKey = (firsttab).find("a.tab").attr("mnhdkey");
	        if(firsttabHeaderKey !== $('.main-menu>li.active').attr('id')) { 
				$('details:not([class])').remove();
				displayMenu(firsttabHeaderKey);
			} 
	    	setDetails(firsttabHeaderKey, gprogrid);
            firsttab.addClass("on");
            const findFirstActive = activeProgramList.find(function(data) { 
				return data.progrid === gprogrid;
			});
			findFirstActive.active = true;

            // get its link name and show related content

		    console.log(' -------------------------------  '  );
		    console.log('(Remove) : Active Program : ' + gprogrid );
		    console.log(' -------------------------------  '  );
	    	ggActivPG = gprogrid;
	    	history.pushState(null, null, rPath + $('li[progrid="' + gprogrid +'"]').attr('prgmurl'));
		}
		//tab length 가 0이 될 경우 main이 보여야 함으로 location 초기화(+ F5 이후 main 미존재 시 추가)
		if($('#tabs li').length < 1){
			$('.nav-location').html('');
			if(!$('.tab-contents.main').length) {
				const mainHtml = await getPageHtml('/main', 'main');
				$('.content-wrapper').append(mainHtml);
			}
			$('details:not([class])[open]').prop('open', false);
			setMainContent();
			history.pushState(null, null, rPath + '/main');
		}
		
	    sessionStorage.activeProgramList = JSON.stringify(activeProgramList);
        const newActiveProgramList = activeProgramList.filter(function(data) {
			return data.progrid !== contentname;
	    });
	    sessionStorage.activeProgramList = JSON.stringify(newActiveProgramList);
	    // Tab bookmark ReSize
	    settabs();
		
		if(window.eventSource && gprormid === 'doe053') {
			window.eventSource.close();  // 연결 종료
			window.eventSource = null;   // 객체를 null로 설정하여 더 이상 사용하지 않도록
		}
	});
});

function allHiddenTab() {
    $(".tab-contents").hide();
    $('.tab-contents').attr('disabled', true);
    $("#tabs li").removeClass("on");
    $(".content.on").removeClass("on");
}

var tabContainerWidth = 0;
var tabListWidth = 0;
var tabCount = 0;
var tabCount2 = 0;
function settabs(){
	//var tabs = $('.tabs');
	if($('.tabs').length === 0) return;
	//var tabLen = $('.tabs').find('li').length;
	 tabContainerWidth = $('.tabs').parent().width();
	 tabListWidth = 0;
	//var prevBtn = $('.tab-prev');
	//var nextBtn = $('.tab-next');

	$('#tabs').find('li').each(function(idx){
		if( idx === 0) return;
		tabListWidth = tabListWidth + $(this).outerWidth();
	})
	//console.log('settabs :  tabListWidth=' + tabListWidth);
	if( tabContainerWidth > tabListWidth ) {
		$('.tab-prev').addClass('disabled');
		$('.tab-next').addClass('disabled');
	} else{
		$('.tab-prev').on('click', tabToLeft);
		$('.tab-next').on('click', tabToRight);

		$('.tab-prev').removeClass('disabled');
		$('.tab-next').removeClass('disabled');
	}
	$('.tab-close').on('click', tabAllClose);
}


function tabToLeft(){
	//var tabs = $('.tabs');
	if($('.tabs').length === 0) return;

	//var tabLen = $('.tabs').find('li').length;
	tabContainerWidth = $('.tabs').parent().width();

	$('.tab-next').removeClass('disabled');
	var marginRight = 0;
	++tabCount2;
	marginRight += $('.tabs').find('li').eq(tabCount).outerWidth();

	//console.log('tabToLeft  : margin-left='+parseInt($(tabs).css('margin-left')) + ' marginRight=' + marginRight);
	if( parseInt($(tabs).css('margin-left'))+ marginRight > 0 ){
		return;

	} else if( parseInt($(tabs).css('margin-left'))+ marginRight + 50 === 0 ){
		 $('.tab-prev').addClass('disabled');
	}

	$('.tabs').css('margin-left', parseInt($(tabs).css('margin-left'))+ marginRight);

	--tabCount;

}

function tabAllClose(){
	let tabs = $(this).parent().find(".tab");
	let tabid;
	let contentname;
	if($('.tabs').length === 0) return;
	for (var i = 0; i < tabs.length-1; i++) {
		tabid = tabs[i].id;
        contentname = tabid + "_content";
        $("#" + contentname).remove();
        //$("#" + contentname).parent().remove();
        $("#tabli_" + tabid).remove();
        //$("#tabli_" + tabid).parent().remove();

	}

    if ($("#tabs li.on").length == 0 && $("#tabs li").length > 0) {
    	// find the first tab
        var firsttab = $("#tabs li:first-child");

        //firsttab.removeClass("default"); //ksw
        firsttab.addClass("on");
        // get its link name and show related content
        var gprogrid = $(firsttab).find("a.tab").attr("id");
        $("#" + gprogrid + "_content").show();

	    console.log(' -------------------------------  '  );
	    console.log('(allClose) : Active Program : ' + gprogrid );
	    ggActivPG = gprogrid;
	    console.log(' -------------------------------  '  );
	}
}

function tabToRight(){

	//var tabs = $('.tabs');
	if($('.tabs').length === 0) return;

	//var tabLen = $('.tabs').find('li').length;
	tabContainerWidth = $('.tabs').parent().width();

	$('.tab-prev').removeClass('disabled');
	var marginLeft = 0;

	++tabCount;
	//console.log('tabToRight : count='+tabCount + ' tabListWidth=' + tabListWidth);
	for (var i = 1; i < tabCount+1; i++) {
		marginLeft +=  $('.tabs').find('li').eq(i).outerWidth();
		if( tabListWidth - marginLeft < tabContainerWidth ){
			$('.tab-next').addClass('disabled');
		}
		$('.tabs').css('margin-left', - marginLeft);
	}
}

window.addEventListener('popstate', function(){
	popstateEvent();
})

async function popstateEvent() {
	//뒤로가기, 앞으로가기 이벤트 처리.
	const activeProgramList = JSON.parse(sessionStorage.activeProgramList);				// 세션 내 저장된 active 되었던 프로그램 리스트.
	const historyUrl = location.pathname.substring(9);									// 뒤로가기, 앞으로가기 시 url 내의 page 접근 url.
	const programID = historyUrl === '/main' ? historyUrl : historyUrl.substring(9);	// 해당 url 내의 프로그램 ID.
	if(programID === '/main') {															// 이벤트 시 접근 url이 메인일 시.
		if(!document.querySelector('.content-wrapper>.tab-contents.main')) { 			// F5시 이후 홈 버튼 클릭 시 메인화면이 사라지므로 추가를 위함.
			const mainHtml = await getPageHtml('/main', 'main');
			document.querySelector('.content-wrapper').appendChild(mainHtml);
		}			
		document.querySelectorAll('.content-wrapper>.tab-contents:not(.main)').forEach(function(element) {	
			element.style.display = 'none';												// 추가된 모든 탭 메뉴의 display = 'none' 처리
		});
		setMainContent();
		document.querySelector('.tab-menu.on')?.classList.remove('on');
		document.querySelector('details li.active')?.classList.remove('active');
		window.ggActivPG = 'Home';
		return;
	}
	document.querySelectorAll('.tab-contents').forEach(function(element) {
		element.style.display  = 'none';
	}); 
	
	let hasBackData = false;															// 삭제 후 뒤로가기 시 처리를 위한 플래그.
	for(const backData of activeProgramList){											// 세션 스토리지 내 탭 정보들 확인.
		if(backData.progrid === programID){												// popstate 이벤트 트리거 시 url 내의 프로그램 ID가 세션스토리지 내 존재할시에 대한 처리.
			backData.active = true;
			hasBackData = true;
			document.querySelector('#home_btn_area').className = 'ic-tab-home-btn';
			document.querySelector('#home_btn_area img').className = 'ic-tab-home';
			document.querySelector('.tab-contents.' + programID).style.display = '';
			document.querySelector('.tab-menu.on')?.classList.remove('on');
			document.querySelector('.tab-contents.'+ backData.progrid).style.display = '';
			document.querySelector('#tabli_' + backData.progrid).classList.add('on');
			$('.details-submenu>li.active').removeClass('active');
			if (backData.mnhdkey !== $('.main-menu>li.active').attr('id')) {
				$('.sidebar-menu-wrapper>details:not([class])').remove();
				$('details:not([class])').remove();
				displayMenu(backData.mnhdkey);
			}
			setDetails(backData.mnhdkey, backData.progrid); 
		}else if(backData.active){														// 활성화 된 탭 내 비활성화 플래그 삽입.
			backData.active = false;
		}
	}
	if(!hasBackData) {																	// popstate 이벤트 발생 시 url 내의 프로그램이 존재하지 않을 시 (사유 : 탭 remove 이후 트리거)
		 const menuInfo = userMenuList.find(function(data) {							// 프로그램 정보를 얻기 위함.
            return data.progrid === programID && data.menugbn != 'BOOKMARK';
         });
         const menuObject = {...menuInfo};              		 						// userMenuList 원본은 건드리지 않음.
         menuObject.active = true;
         menuObject.prgmurl = historyUrl;
		 document.querySelector('details li.active')?.classList.remove('active');
         document.querySelector('.tab-menu.on')?.classList.remove('on');
         await addTab(menuObject);
         setDetails(menuObject.mnhdkey, menuObject.progrid);
         activeProgramList.push(menuObject);
	}
	
	sessionStorage.activeProgramList = JSON.stringify(activeProgramList);
}