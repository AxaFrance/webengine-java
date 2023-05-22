function openSelectedTab(idTab, idContentTab) {
    hideElement("class-container-tab");
    unSelectedAllTab("tablink");
    selectedTabById(idTab,idContentTab);
}

function selectedTabById(idTab,idContentTab){
    showElement(idContentTab);
    addClassToElement(idTab);
}

function openSelectedLineInTree() {
    var i;
    var toggler = document.getElementsByClassName("caret");

    for (i = 0; i < toggler.length; i++) {
        toggler[i].addEventListener("click", function() {
            this.parentElement.querySelector(".nested").classList.toggle("active");
            this.classList.toggle("caret-down");
        });
    }
}

function showElement(idTab){
    document.getElementById(idTab).style.display = "block";
}

function hideElement(className){
    var i;
    var tabArray = document.getElementsByClassName(className);
    for (i = 0; i < tabArray.length; i++) {
        tabArray[i].style.display = "none";
    }
}

function unSelectedAllTab(className){
    var i;
    var tablinks = document.getElementsByClassName(className);
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace(" w3-border-red", "");
    }
}

function addClassToElement(idTab) {
    document.getElementById(idTab).classList.add("w3-border-red");
}

function changeColorOfSelectedElement(event){
    var tablinks = document.getElementsByClassName("selected-line");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace('selected-line','');
    }
    event.currentTarget.className += " selected-line";
}