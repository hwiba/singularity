var noteTargetDate = getTargetDate();
var groupName = document.body.querySelector("#hiddenGroupName").value;
window.addEventListener("load", function() {
	document.body.querySelector('#noteTargetDate').value = noteTargetDate;
});

var el = document.querySelector("#temp-note-list");
var button = el.querySelector("a[data-toggle='dropdown']");
var menu = el.querySelector(".dropdown-menu");
var arrow = el.querySelector(".fa-sort-desc");

button.onclick = function(event) {
	if(arrow.className === "fa fa-sort-desc") {
		menu.setAttribute("style", "display: ");
		arrow.setAttribute("class", "fa fa-sort-up");
	} else {
		menu.setAttribute("style", "display: none");
		arrow.setAttribute("class", "fa fa-sort-desc");
	}
};

function getTargetDate() {
	var elTargetDate = document.body.querySelector("#hiddeNoteTargetDate");
	if (null === elTargetDate) {
		return "";
	}
	return new Date(elTargetDate.value);
}

window.addEventListener('load', function() {
	document.querySelector(".searchForm").setAttribute("style","display: block");
	var partyName = document.querySelector('#group-name').textContent;
	document.title = partyName;
	document.querySelector('#group-name').innerHTML = partyName;
	
	document.querySelector("#noteTargetDate").value = new Date();
	if(noteTargetDate !== "")
		document.querySelector("#noteTargetDate").value = noteTargetDate;
	datepickr('#calendar', {
		dateFormat : 'Y-m-d',
		altInput : document.querySelector('#noteTargetDate')
	});
    
	var textBox = document.querySelector("#noteTextBox");
	textBox.addEventListener('keyup', loadPreviewText, false);

    function loadPreviewText() {
        var md = document.querySelector('#noteTextBox').value;
        previewText(markdown.toHTML(md, 'Maruku'));
    }

    loadPreviewText();
    
    function previewText(markdown) {
        var previewBox = document.querySelector('#previewBox');
		previewBox.innerHTML = markdown;
    }

    textBox.addEventListener('keydown', tabKeyHandler, false);
}, false);

function tabKeyHandler(e) {
    var TABKEY = 9;
    if(e.keyCode === TABKEY) {
        e.preventDefault();
        insertAtCaret(e.target, '\t');
    }
}

function insertAtCaret(target, text) {
    var scrollPos = target.scrollTop;
    var strPos = 0;
    var br = ((target.selectionStart || target.selectionStart == '0') ? "ff" : (document.selection ? "ie" : false ) );
    if (br == "ie") {
        target.focus();
        var range = document.selection.createRange();
        range.moveStart ('character', -txtarea.value.length);
        strPos = range.text.length;
    } else if (br == "ff") strPos = target.selectionStart;
    var front = (target.value).substring(0,strPos);
    var back = (target.value).substring(strPos,target.value.length);
    target.value = front + text + back;
    strPos = strPos + text.length;
    if (br == "ie") {
        target.focus();
        var range = document.selection.createRange();
        range.moveStart ('character', -target.value.length);
        range.moveStart ('character', strPos);
        range.moveEnd ('character', 0);
        range.select();
    } else if (br == "ff") {
        target.selectionStart = strPos;
        target.selectionEnd = strPos;
        target.focus();
    }
    target.scrollTop = scrollPos;
}
