window.addEventListener('load', function() {
	document.querySelector(".searchForm").setAttribute("style","display: block");
	guinness.restAjax({
		method : "get",
		url : "/groups",
		statusCode: {
  			200: function(res) {	// 그룹 리스트 받아오기 성공  
  				appendGroups(JSON.parse(res));
				//TODO 알람 관련 세팅이 끝난 뒤 주석 해제 loadGroupAlarm();
  			}
  		}
	});
	document.querySelector('#create-new').addEventListener('mouseup', createGroup, false);
}, false);

function loadGroupAlarm() {
    guinness.ajax({
        method:"get",
        url:"/alarms/count",
        success : function(req) {
            setGroupAlarm(JSON.parse(req.responseText));
        }
    })
}
                  
function setGroupAlarm(json) {
    var group = document.body.querySelectorAll('#group-container > a > li > input[type="hidden"]');
    var js = json.mapValues;
    for (var i in group) {
        for (var j in js) {
            if( group[i].value === js[j].partyId) {
                var elCount = document.createElement("div");
                elCount.className="alarm-count";
                elCount.style.display="block"
                elCount.innerText = js[j].groupAlarmCount; 
                group[i].parentElement.appendChild(elCount);
            }
        }
    }
}

function createGroup() {
	var bodyTemplate = document.importNode(document.querySelector("#create-group-template").content, true);
	guinness.util.modal({
		header : "새 그룹 만들기",
		body: bodyTemplate,
		defaultCloseEvent: false
	});
    
    guinness.util.setModalPosition();

	document.querySelector('.modal-close-btn').addEventListener('click', function(e){
		cancelGroupCreate();
	}, false);
	
	document.querySelector('.modal-cover').addEventListener('click', function(e){
		if (e.target.className==='modal-cover') {
			cancelGroupCreate();
		}
	}, false);
	
	document.querySelector('.modal-cover').setAttribute('tabindex',0);
	document.querySelector('.modal-cover').addEventListener('keydown',function(e){
		if(e.keyCode === 27){
			cancelGroupCreate();
		}
	},false);

	document.querySelector('#create-group-form').addEventListener('submit', function(e){
		e.preventDefault();
		var form = document.querySelector('#create-group-form');

		if(document.querySelector('.modal-cover input[name="partyName"]').value != ""){
			var status = document.querySelector('input[name=status]:checked');
			var param = "partyName="+form.partyName.value+"&status="+status.value;
			guinness.restAjax({
				method : "post",
				url : "/groups",
				param: param,
				statusCode: {
		  			201: function(res) {	// 그룹 생성 성공
		  				appendGroup(JSON.parse(res));
		  				document.querySelector('.modal-cover').remove();
		  			},
		  			412: function(res) {	// 그룹명 15자 이상 시 실패
		  				document.querySelector('.modal-cover').remove();
		  				guinness.util.alert("경고!", res);
		  			}
		  		}
			});
			return;
		}
		guinness.util.alert("경고!","그룹 이름을 입력하세요!");
	}, false);
}

function cancelGroupCreate() {
	document.querySelector('.modal-cover').remove();
}

function appendGroup(obj) {
	var el = document.querySelector('#group-container');
	var template = document.querySelector("#group-card-template").content;
	var newEl;
	var partyName = (obj.partyName.replace(/</g, "&lt;")).replace(/>/g, "&gt;");
	document.cookie = obj.partyId + "=" + encodeURI(obj.partyName);
	newEl = document.importNode(template, true);
	newEl.querySelector(".group-card").setAttribute("id", obj.partyId);
	newEl.querySelector(".group-card").setAttribute("href", "/groups/" + obj.partyId);
	newEl.querySelector(".group-name").innerHTML = partyName;
	newEl.querySelector('.leaveGroup-btn').addEventListener("click",
		function(e) {
			e.preventDefault();
			var partyId = e.currentTarget.parentElement.parentElement.getAttribute("href").split("/")[2];
			var partyName = e.currentTarget.parentElement.querySelector(".group-name").innerHTML;
			guinness.confirmLeave(partyId, partyName);
		}, false);
	if (obj.status === 'T') {
		newEl.querySelector('.fa-lock').setAttribute('class','fa fa-unlock');
	}
	newEl.querySelector('input').setAttribute("value", obj.partyId);
	el.appendChild(newEl);
}

function appendGroups(json) {
	var el = document.querySelector('#group-container');
	var template = document.querySelector("#group-card-template").content;
	var obj = null;
	var newEl;

	var length = json.length;
	for (var i = 0; i < length; i++) {
		appendGroup(json[i])
	}
}
