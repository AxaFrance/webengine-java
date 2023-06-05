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
        tablinks[i].className = tablinks[i].className.replace(" w3-border-blue", "");
    }
}

function addClassToElement(idTab) {
    document.getElementById(idTab).classList.add("w3-border-blue");
}

function changeColorOfSelectedElement(event){
    var tablinks = document.getElementsByClassName("selected-line");
    for (i = 0; i < tablinks.length; i++) {
        tablinks[i].className = tablinks[i].className.replace('selected-line','');
    }
    event.currentTarget.className += " selected-line";
}

function displayImage(imgToDisplay){

    // Get the modal
    var modal = document.getElementById("myModal");
    // Get the image and insert it inside the modal - use its "alt" text as a caption
    var img = document.getElementById(imgToDisplay);
    var modalImg = document.getElementById("img01");
    var captionText = document.getElementById("caption");

    modal.style.display = "block";
    modalImg.src = img.src;
    captionText.innerHTML = img.alt;

    // Get the <span> element that closes the modal
    var span = document.getElementsByClassName("close")[0];

    // When the user clicks on <span> (x), close the modal
    span.onclick = function() {
        modal.style.display = "none";
    }
}
