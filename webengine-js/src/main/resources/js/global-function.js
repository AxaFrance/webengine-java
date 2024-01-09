function getLastChild(el) {
    if (!(el instanceof Element)){
        return null;
    }

    return el.lastChild;
}