function openSelectedTab(idTab, contentIdTab) {
    hideElement("class-container-tab");
    unSelectedAllTab("tablink");
    selectedTabById(idTab,contentIdTab);
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

function selectedTabById(idTab,contentIdTab){
    showElement(contentIdTab);
    // evt.currentTarget.firstElementChild.className += " w3-border-red";
    document.getElementById(idTab).classList.add("w3-border-red");

}