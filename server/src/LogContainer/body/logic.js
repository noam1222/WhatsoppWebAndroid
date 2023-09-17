function isDigit(c) {
    return c >= '0' && c <= '9';
}

export function isValidPassword(pass) {
    if (pass.length < 8) {
        return false;
    }
    let digitsCounter = 0;
    for (let char of pass) {
        if (isDigit(char)) {
            digitsCounter++;
        }
    }
    return digitsCounter === 1 || digitsCounter === 2;
}

export function isValidDisplayName(username) {
    return username.length >= 4;
}

export function isValidNewUsername(username) {
    if (username.length < 4) {
        return "At least 4 characters long.";
    }
    return "";
}