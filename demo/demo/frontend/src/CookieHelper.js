export function getCookie(cookieName) {
    let name = cookieName + "=";
    let decodedCookie = decodeURIComponent(document.cookie);
    let ca = decodedCookie.split(";");
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }

    return "";
}


export function appendToCookie(cookieName, value) {

    let cookieValues = getCookie(cookieName).split(",");
    cookieValues.push(value);

    document.cookie = `${cookieName + "=" + cookieValues.join(',')};path=/`

}

export function setCookie(cookieName, value) {
    document.cookie = `${cookieName + "=" + value};path=/`
}

export function clearCookie(cookieName) {
    document.cookie = `${cookieName + "="};path=/`
}

export function Getlcrn(cookieName) {

    var astring;
    let ca = getCookie(cookieName).split(",");
    for (let i = 0; i < ca.length; i+3) {
        astring += ca[i] + ",";
    }
    return astring;
}

export function Getlcourse(cookieName) {

    var astring;
    let ca = getCookie(cookieName).split(",");
    for (let i = 2; i < ca.length; i+3) {
        astring += ca[i] + ",";
    }
    return astring;
}

export function Getunlcrn(cookieName) {

    var astring;
    let ca = getCookie(cookieName).split(",");
    for (let i = 0; i < ca.length; i+2) {
        astring += ca[i] + ",";
    }
    return astring;
}

export function Getunlcourse(cookieName) {

    var astring;
    let ca = getCookie(cookieName).split(",");
    for (let i = 1; i < ca.length; i+2) {
        astring += ca[i] + ",";
    }
    return astring;
}