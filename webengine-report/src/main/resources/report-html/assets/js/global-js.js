function showTabByTreeId(id){
    hideElement("body-right-container");
    showElement(id);
}

function openSelectedTab(evt, idTab) {
    hideElement("class-container-tab");
    unSelectedAllTab("tablink");
    selectedTabById(evt,idTab);
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

function showElement(id){
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

function selectedTabById(evt,idTab){
    showElement(idTab);
    evt.currentTarget.firstElementChild.className += " w3-border-red";
}

