let mainHeaderList = mainHeader;
let userMenuList = userMenu;

/*
사용자 권한이 있는 프로그램 목록 가져오기
 */
async function getUserMenuList() {
    let headerHtml = '';
    for(const header of mainHeaderList) {
		headerHtml += '<li id="' + header.mnhdkey + '"><a>'+ header.mnhdnam + '</a></li>';
     }
    document.querySelector('.main-menu').insertAdjacentHTML('afterbegin', headerHtml);				// 헤더 구성.
    document.querySelectorAll('.main-menu>li').forEach(function(element) {
		element.addEventListener('click', function() {
			headerClickEvent(element.getAttribute('id'));
		});
    });

    const programID = location.pathname.split('/').pop();									// url 내 프로그램 ID.
    document.querySelector('.tab-contents').classList.add(programID, 'on');							// 새로고침 시 기준이 되는 탭을 설정하기 위함.

    const activeProgramList = sessionStorage.activeProgramList ? JSON.parse(sessionStorage.activeProgramList) : [];
    let index;																						// 기준이 되는 프로그램의 헤더 및 사이드바 설정을 for문 종료 이후 하기 위함.
    if(activeProgramList.length) {
	    let isPreviousMenu = true;																	// 기준이 되는 프로그램의 이전/이후를 체크하기 위함.
        for(let idx=0; idx<activeProgramList.length; idx++) {
			if(activeProgramList[idx].progrid !== programID) {										// 기준 페이지가 아닌 추가가 필요한 페이지 addTab.
                activeProgramList[idx].active = false;
            	await addTab(activeProgramList[idx]);												// 미존재 시 addTab
                document.querySelector('.tab-contents.' + activeProgramList[idx].progrid).style.display = 'none';
            }else {
				index = idx;																		// 세션스토리지 내 해당 프로그램 ID 존재 시(새로고침 및 기존 추가된 탭 URL 입력)
                activeProgramList[idx].active = true;
                isPreviousMenu = false;
                await setBookMarkAndTabList(activeProgramList[index]);
                await setLocation(activeProgramList[index].progrid);
            }
            activeProgramList[idx].previousMenu = isPreviousMenu;
        }
    }

    if(programID === 'main') {
		setMainContent();
    }else if(index !== undefined) {
		await displayMenu(activeProgramList[index].mnhdkey);										// 세션스토리지 내 미존재 시 URL 입력으로 인한 신규 페이지 구성.
        setDetails(activeProgramList[index].mnhdkey, activeProgramList[index].progrid);
        sessionStorage.activeProgramList = JSON.stringify(activeProgramList);
    }else{
		const menuInfo = userMenuList.find(function(data) {
			return data.progrid === programID && data.menugbn != 'BOOKMARK';
	    });
        const menuObject = {...menuInfo};               											// userMenuList 원본은 건드리지 않음.
        menuObject.active = true;
        await displayMenu(menuObject.mnhdkey);
        setDetails(menuObject.mnhdkey, menuObject.progrid);
        menuObject.prgmurl = '/page' + menuObject.prgmurl;
        await setBookMarkAndTabList(menuObject);
        await setLocation(menuObject.progrid);
        activeProgramList.push(menuObject);
    }
    sessionStorage.activeProgramList = JSON.stringify(activeProgramList);
}

/*
사이드바 메뉴 목록 구성(북마크 및 폴더/프로그램 메뉴)
 */
async function displayMenu(mainHeaderKey) {
	const hasBookMarkDOM = document.querySelector('ul.fa-list>li');
	for(const menu of userMenuList) {
		if(menu.menugbn === 'BOOKMARK' && !hasBookMarkDOM) {
			const bookMarkHtml = '<li id="bookMark_' + menu.menukey + '" onclick="addTab(this)"' + ' prgmurl="' + pagePath + menu.prgmurl
								+ '" progrid="'+ menu.progrid + '" mnhdkey="' + menu.mnhdkey +'"><a>' + menu.menunam + '</a></li>';
			document.querySelector('ul.fa-list').insertAdjacentHTML('beforeend', bookMarkHtml);
		}else if(menu.menugbn === 'FLD' && mainHeaderKey === menu.mnhdkey) {
			const folderHtml = '<details><summary><span class="submenu-title"><img>'
								+ menu.menunam + '</span><img class="ic-submenu-open"></summary>'
								+ '<ul class="details-submenu" id="' + menu.menukey + '"></ul></details>';
			document.querySelector('.sidebar-menu-wrapper').insertAdjacentHTML('beforeend', folderHtml);
		}else if(menu.menugbn === 'PGM') {
			const programHtml = '<li id="' + menu.menukey + '" onclick="addTab(this)"' + ' prgmurl="' + pagePath + menu.prgmurl
								+ '" progrid="'+ menu.progrid + '" mnhdkey="' + menu.mnhdkey +'"><a>' + menu.menunam + '</a></li>';
			if(document.querySelector('#' + menu.menufid)) {
				document.querySelector('#'+ menu.menufid).insertAdjacentHTML('beforeend', programHtml);
			}
		}
	}
}

/*
추가 탭 화면 구성
 */
async function addTab(menu) {
	const isDOM = menu instanceof Element;													// 파라미터의 DOM 여부를 판단하여 새로고침, URL입력과 프로그램 클릭으로 인한 탭 추가 판별.
	const menukey = menu.id || menu.menukey;
	const programName = menu.innerText || menu.menunam;
	const programID = isDOM ? menu.getAttribute('progrid') : menu.progrid;
	const programUrl = isDOM ? menu.getAttribute('prgmurl') : menu.prgmurl;
	const headerKey = isDOM ? menu.getAttribute('mnhdkey') : menu.mnhdkey;
	const activeProgramList = sessionStorage.activeProgramList ? JSON.parse(sessionStorage.activeProgramList) : [];

   	if(document.querySelector('.tab-contents.' + programID) && isDOM) {						// 사이드바 메뉴 클릭 시 해당 프로그램이 기존에 추가된 탭 화면에 존재하는 케이스.
		for(const activeProgram of activeProgramList) {
			if(programID === activeProgram.progrid) {
				activeProgram.active = true;
				if(headerKey !== document.querySelector('ul>li.active').getAttribute('id')) {
					document.querySelectorAll('details:not([class])').forEach(function(element) {
						element.remove();													// 기존 사이드 바의 메뉴 재구성을 위함.
					});
					displayMenu(headerKey);													// 대상 탭 화면에 해당하는 사이드 바 구성.
					document.querySelector('ul>li.active').classList.remove('active');
					document.querySelector('#' + headerKey).classList.add('active');
				}
				setDetails(headerKey, programID);
				window.ggActivPG = programID;
				document.querySelector('#tabli_' + programID).classList.add('on');
				document.querySelector('.tab-contents.' + programID).style.display = '';
    			history.pushState(null, null, rPath + activeProgram.prgmurl);				// URL 세팅
			}else{
				activeProgram.active = false;
				document.querySelectorAll('.tab-contents:not(.' + programID + ')').forEach(function(element) {
					element.style.display = 'none';											// 존재하는 모든 TAB 화면 display = 'none' 처리
				});
				document.querySelector('#tabli_' + activeProgram.progrid)?.classList?.remove('on');
				document.querySelector('#home_btn_area').className = 'ic-tab-home-btn';
				document.querySelector('#home_btn_area img').className = 'ic-tab-home';
				document.querySelector('details:not([class]) li[progrid="' + activeProgram.progrid + '"]')?.classList.remove('active');
			}
		}
		sessionStorage.activeProgramList = JSON.stringify(activeProgramList);
		return;
	}else if(document.querySelector('.content-wrapper .tab-contents:not(.main)') && isDOM) { // 사이드 바 메뉴 클릭 시 해당 프로그램 추가 여부에 관계 없이 추가되어 있는 탭 화면이 존재하는 케이스
	    const hasAddPage = activeProgramList.find(function(data) {
			return data.active === true;
		});
		if(hasAddPage) {
			hasAddPage.active = false;
			document.querySelector('.tab-contents.' + hasAddPage.progrid).style.display = 'none';
		}
		document.querySelector('#tabs>li.on')?.classList.remove('on');
	}

	const menuObject = {																	 // 세부 설정을 위한 Object 구성
		menukey : menukey,
		progrid : programID,
		mnhdkey : headerKey,
		prgmurl : programUrl,
		menunam : programName,
		active : isDOM ? true : menu.active
	};

	const tabContents = await getPageHtml(programUrl, programID);							 // 탭 추가를 위한 html response
	if(!tabContents) {
		return;
	}

	//위치 변경.
	if(menuObject.active) {																	 // active인 메뉴의 url 세팅
	    history.pushState(null, null, rPath + menuObject.prgmurl);
	    window.ggActivPG = menuObject.progrid;
	}

	if(menu.previousMenu) {																	 // F5 이벤트 시 기준 위치의 전 요소로 삽입하기 위함.
		document.querySelector('#content .content-wrapper').insertBefore(tabContents, document.querySelector('.tab-contents.on'));
	}else{
		document.querySelector('#content .content-wrapper').appendChild(tabContents);
	}

    // DOM요소 insert 시 자바스크립트에서는 스크립트를 실행되지 않기에 새로운 요소 생성하여 처리함.
    const scripts = tabContents.querySelectorAll('script'); 
    scripts.forEach(script => {
	    const newScript = document.createElement('script');
	    if (script.src) {
	        newScript.src = script.src;
	    } else {
	        newScript.textContent = script.textContent;
	    }
	    document.body.appendChild(newScript).parentNode.removeChild(newScript);
	});

	await setBookMarkAndTabList(menuObject);

	if(isDOM) {
	    activeProgramList.push(menuObject);
	    sessionStorage.activeProgramList = JSON.stringify(activeProgramList);
	    document.querySelector('details:not([class]) li.active')?.classList.remove('active');
	    setDetails(headerKey, programID);
	}

	await setLocation(programID);
}
//location setting
async function setLocation(programID) {
	const menuObject = userMenuList.find(function(data) {
		return data.progrid === programID && data.menugbn !== 'BOOKMARK';
	});
	const headerName = document.querySelector('#' + menuObject.mnhdkey).innerText;
	const folderObject = userMenuList.find(function(data) {
		return data.menukey === menuObject.menufid && data.menugbn === 'FLD';
	});

	let locationDiv = '<div class="nav-home" id="home"><img class="ic-navi-home"></div>';
	locationDiv += '<span class="nav-location">' + ' > '+ headerName + ' > ' + folderObject.menunam + ' > ' + menuObject.menunam +'</span>';
	document.querySelector('.' + programID +' .location').insertAdjacentHTML('beforeend', locationDiv);
}

async function setBookMarkAndTabList(menuObject) {
	document.querySelector('.main-menu>li.active')?.classList.remove('active');
    document.querySelector('#' + menuObject.mnhdkey).classList.add('active');
    const sidebarList = document.querySelector('details:not([class])>ul>li');
    if(sidebarList && sidebarList.getAttribute('mnhdkey') !== menuObject.mnhdkey) {
		document.querySelectorAll('.sidebar-menu-wrapper>details:not([class])').forEach(function(element) {
			element.remove();
		});
		await displayMenu(menuObject.mnhdkey);
	}
    document.querySelector('#home_btn_area').className = 'ic-tab-home-btn';
	document.querySelector('#home_btn_area img').className = 'ic-tab-home';

    const isBookMarkMenu = userMenuList.find(function(data) {
		return data.progrid === menuObject.progrid && data.menugbn === 'BOOKMARK';
	});
    const tabListHtml = '<li id="tabli_' + menuObject.progrid + '" class="tab-menu' + (menuObject.active ? ' on' : '') + '"><img class="ic-bookmark ' + (isBookMarkMenu ? 'on' : '') + '">'
    					+ '<a menukey="'+ menuObject.menukey + '" mnhdkey="' + menuObject.mnhdkey +'" class="tab" id="' + menuObject.progrid + '" title="' + menuObject.menunam + '">' + menuObject.menunam + '</a>'
                       	+ '<img class="ic-tab-close remove"></li>';

	document.querySelector('#tabs').insertAdjacentHTML('beforeend', tabListHtml);
}

function headerClickEvent(mainHeaderKey) {
	document.querySelectorAll('.sidebar-menu-wrapper>details:not([class])').forEach(function(element) {
		element.remove();
	});
	document.querySelector('.main-menu>li.active').classList.remove('active');
	document.querySelector('#' + mainHeaderKey).classList.add('active');
	displayMenu(mainHeaderKey);
}

function setDetails(headerkey, programID) {
	document.querySelector('.main-menu>li.active')?.classList.remove('active');
	document.querySelector('.main-menu>li#' + headerkey).classList.add('active');
	const programList = document.querySelectorAll('details:not([class])');
	programList.forEach(function(element) {
		const detailElement = element.querySelector('li[progrid="' + programID + '"]');
		if(detailElement) {
			element.open = true;
			detailElement.classList.add('active');
		}
	});
}

async function setMainContent() {
    document.querySelectorAll('details:not([class])').forEach(function(element) {
	   element.remove();
    });
    const firstActive = document.querySelector('.main-menu>li');
	    await displayMenu(firstActive.getAttribute('id'));
	    document.querySelector('.main-menu>li.active')?.classList.remove('active');
	    firstActive.classList.add('active');
	    document.querySelector('.tab-contents.main').style.display = '';
	    document.querySelector('#home_btn_area').className = 'ic-tab-home-btn-click';
	    document.querySelector('#home_btn_area img').className = 'ic-tab-home-click';
}

async function getPageHtml(programUrl, programID) {
    const pageDOM = await fetchUtil('GET', programUrl);
    if(pageDOM.status !== 200) {
		const tabContent = document.querySelector('.tab-contents.' + window.ggActivPG);
		const tabLi = document.querySelector('#tabli_' + window.ggActivPG);

		if(tabContent !== null){
			document.querySelector('.tab-contents.' + window.ggActivPG).style.display = '';
		}

		if(tabLi !== null){
			document.querySelector('#tabli_' + window.ggActivPG).classList.add('on');
		}

		return;
	}

    const pageDOMText = await pageDOM.text();
    const parsePage= new DOMParser().parseFromString(pageDOMText, 'text/html');
    const tabContents = parsePage.querySelector('.tab-contents');
    tabContents?.classList?.add(programID);
    if(document.querySelector('.tab-contents.main')) {
		document.querySelector('.tab-contents.main').style.display = 'none';
	}

	return tabContents;
}

//북마크 추가
async function bookMarkAdd() {
	const progrid = location.pathname.substring(18);
	if(!progrid) {
		return;
	}
	const isBookMark = document.querySelector('details .fa-list>li[progrid="' + progrid + '"]');
	const httpMethod = isBookMark ? 'DELETE' : 'POST';
	const param = {
		userkey : userAttr.userkey,
		progrid : progrid,
		useract : userAttr.useract
	};
	const responseData = await fetchUtil(httpMethod, '/sy/bookmark/' + httpMethod.toLowerCase(), param);
	if(responseData.status !== 200) {
		return;
	}

	if(!isBookMark) {
		const menu = document.querySelector('details:not([class]) li[progrid="' + progrid + '"]');
		const html = '<li id="bookMark_' + menu.id + '" onclick="addTab(this)"' + ' prgmurl="' + pagePath + menu.getAttribute('prgmurl')
					+ '" progrid="'+ menu.getAttribute('progrid') + '" mnhdkey="' + menu.getAttribute('mnhdkey') +'"><a>' + menu.innerText + '</a></li>';
		document.querySelector('ul.fa-list').insertAdjacentHTML('beforeend', html);
		document.querySelector('#tabli_' + menu.getAttribute('progrid') + ' img').classList.add('on');
	}else{
		isBookMark.remove();
		document.querySelector('#tabli_' + progrid + ' img').classList.remove('on');
	}

	alert(isBookMark ? deleteSuccessMsg : saveSuccessMsg);
	const setSessionData = await fetchUtil('GET', '/session/role-menu');
	if(setSessionData.status != 200) {
		return;
	}
	const setSession = await setSessionData.json();
	mainHeaderList = setSession.mainHeaderList;
	userMenuList = setSession.userMenuList;
}

//syu03 권한등록에서 호출(권한별 메뉴 변경 후 변경된 메뉴로 세팅하는 함수)
async function setChangeMenu(roleMenuChangeDataList){
	// roleMenuChangeDataList에는 폴더는 없고, 메뉴만 들어있다.

	for(const roleMenuChangeData of roleMenuChangeDataList){
		const folderElement = document.querySelector('#' + roleMenuChangeData.menufid);
		const menuElement = document.querySelector('#' + roleMenuChangeData.menukey);
		const isAddTabActive = document.querySelector('#' + roleMenuChangeData.mnhdkey).classList.contains('active');
		if(roleMenuChangeData.state){
			if(isAddTabActive){
				if(!folderElement){
					const findMenuFolder = userMenuList.find(function(element){
						return element.menukey === roleMenuChangeData.menufid;
					});
					const folderHtml = '<details><summary><span class="submenu-title"><img>'
										+ findMenuFolder.menunam + '</span><img class="ic-submenu-open"></summary>'
										+ '<ul class="details-submenu" id="' + findMenuFolder.menukey + '"></ul></details>';
					document.querySelector('.sidebar-menu-wrapper').insertAdjacentHTML('beforeend', folderHtml);
				}
				const programHtml = '<li id="' + roleMenuChangeData.menukey + '" onclick="addTab(this)"' + ' prgmurl="' + pagePath + roleMenuChangeData.prgmurl
									+ '" progrid="'+ roleMenuChangeData.progrid + '" mnhdkey="' + roleMenuChangeData.mnhdkey +'"><a>' + roleMenuChangeData.menunam + '</a></li>';

				if(document.querySelector('#' + roleMenuChangeData.menufid)) {
					document.querySelector('#'+ roleMenuChangeData.menufid).insertAdjacentHTML('beforeend', programHtml);
				}
			}
		}else{
			if(isAddTabActive){
				menuElement.remove();
				//메뉴 삭제 후 폴더 안 메뉴(ui > li)가 없을 경우 폴더도 제거
				if(!folderElement.children.length){
					folderElement.closest('details').remove();
				}
			}
		}
	}
}