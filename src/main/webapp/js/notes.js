/**
 * notes.html에서 가져온 내용. 정리 필요.
 */

const partyId = window.location.pathname.split("/")[2];

window.addEventListener("load", function() {
	document.querySelector(".searchForm").setAttribute("style", "display: block");
	var partyImage = document.body.querySelector("#group-image").value;
	if (partyImage !== "") {
		window.document.body.querySelector("#backImg").style.backgroundImage = "url('/img/group/" + partyImage + "')";
	}

	document.querySelector("#addMemberForm input[name='partyId']").value = partyId;
	document.querySelector("#addMemberForm").addEventListener("submit", function(e) {
		e.preventDefault();
		addMember();
	}, false);
	
	var leaveGroupBtn = document.body.querySelector(".leaveGroup > span");
	leaveGroupBtn.addEventListener('click', function(){
		var partyName = document.body.querySelector("#group-name").textContent;
		guinness.util.alert(partyName, "그룹을 탈퇴하시겠습니까?", function() {
			var sessionUserId = document.querySelector("#sessionUserId").value;
			guinness.restAjax({
				method : "put",
				url : "/party/"+partyId+"/members/"+sessionUserId+"/leave",
				statusCode : {
					200 : function(res) {
						window.location.href = "/";
					},
					406 : function(res) {
						guinness.util.alert('경고', res);
					}
				}
			});},
			function() {});
	}, false);
	
	guinness.restAjax({
		method : "get",
		url : "/party/"+partyId+"/note/",
		statusCode : {
			200 : function (res) {
				var json = JSON.parse(res);
				appendNoteList(json);
				//getDateExistNotes();
			}
		}
	});
	readMember(partyId);
	var elCreateBtn = document.querySelector("#create-new-button");
	if (document.querySelector("#member-template") !== null) {
		memberTemplate = document.querySelector("#member-template").content;
	}
	
}, false);

window.addEventListener("scroll", function() {
	infiniteScroll();
	sideMenuFlow();
	resizeSideMenu();
	refreshCalendar();
}, false);

window.addEventListener('resize', function() {
	resizeSideMenu();
}, false);

$(function() {
	$("#defaultCalendar").daterangepicker(
			{
				singleDatePicker : true,
				showDropdowns : false
			},
			function(start, end, label) {
				$("#reportrange span").html(
						start.format("MMMM D, YYYY") + " - "
								+ end.format("MMMM D, YYYY"));
			});
	var allShowButton = guinness.createElement({
		name : "input",
		attrs : {
			id : "allShow",
			class : "inputBtn",
			type : "submit",
			value : "모두보기",
			onclick : "readNoteList()"
		}
	});
	$("#calendar-container").append(allShowButton);
});

document.querySelector("#calendar-container").addEventListener("click", function(e) {
	if (e.target.getAttribute("class") === null || e.target.getAttribute("class").indexOf("available") === -1
							|| e.target.getAttribute("class").indexOf(
									"existNote") === -1)
						return;
					var noteTargetDate = $("#defaultCalendar").data(
							"daterangepicker").startDate._d.toISOString()
							.substring(0, 10)
							+ " 23:59:59";
					readNoteList(noteTargetDate);
				}, false);

var sideMenuContainers = document.querySelectorAll(".side-menu-container");
function sideMenuFlow() {
	if (window.scrollY > 70) {
		sideMenuContainers[0].style.top = sideMenuContainers[1].style.top = (window.scrollY - 70)
				+ "px";
	} else {
		sideMenuContainers[0].style.top = sideMenuContainers[1].style.top = "0px";
	}
}

var prevDay = "";
function refreshCalendar() {
	var noteDates = document.querySelectorAll("div.note-date");
	var currDay;
	for (var i = 0; i < noteDates.length; i++) {
		if (window.scrollY > noteDates[i].parentNode.offsetTop
				&& window.scrollY < noteDates[i].parentNode.offsetTop
						+ noteDates[i].parentNode.clientHeight) {
			currDay = noteDates[i].textContent;
		}
	}
	if (prevDay !== currDay && currDay !== undefined) {
		var date = currDay.split("-");
		// yearChange
		if ($(".calendar.first .yearselect option[selected='selected']").attr(
				"value") !== date[0]) {
			$(".calendar.first .yearselect").val(date[0]).trigger("change");
		}
		// monthChange
		if ($(".calendar.first .monthselect option[selected='selected']").attr(
				"value") !== date[1] - 1 + "") {
			$(".calendar.first .monthselect").val(date[1] - 1)
					.trigger("change");
		}
		// dayChange
		$(".calendar.first table tbody td.active").removeClass("active");
		var days = $(".calendar.first table tbody td.existNote");
		for (var i = 0; i < days.length; i++) {
			var day = (date[2] < 10) ? date[2].substring(1, 2) : date[2];
			if (days[i].textContent === day) {
				days[i].className += " active";
			}
		}
	}
}

var nullCheckMonth;

function getDateExistNotes(year, month) { // ;; select null exist notes day
	var noteTargetYear = $("#defaultCalendar").data("daterangepicker").startDate._d
			.toISOString().substring(0, 4);
	var noteTargetMonth = $("#defaultCalendar").data("daterangepicker").startDate._d
			.toISOString().substring(5, 7);
	if (year !== undefined)
		noteTargetYear = year;
	if (month !== undefined)
		noteTargetMonth = month + 1;
	var lastDate = (new Date(noteTargetYear, noteTargetMonth, 1)).toISOString()
			.substring(0, 10)
			+ " 23:59:59";
	guinness.ajax({
		method : "get",
		url : "/notes/getNullDay/" + partyId + "/" + lastDate,
		success : function(req) {
			var json = JSON.parse(req.responseText);
			nullCheckMonth = json.objectValues;
			if (json.success === true) {
				setNullCheck(nullCheckMonth);
			}
		}
	});
}

function setNullCheck(nullCheckMonth) {
	var td = document.querySelectorAll(".available");
	var flagStart = false;
	var i = 0;
	for (t in td) {
		if (td[t].innerText === "1") {
			flagStart = true;
		}
		if (flagStart === true) {
			if (nullCheckMonth[i] === true) {
				td[t].className = td[t].className + " noNote";
			} else {
				td[t].className = td[t].className + " existNote";
			}
			i++;
			if (nullCheckMonth.length === i)
				break;
		}
	}
}