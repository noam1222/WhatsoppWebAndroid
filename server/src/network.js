const apiUrl = "/api/";

export async function createUser(username, pass, displayName, selectedImage) {
    const newUserDetails = {
        "username": username,
        "password": pass,
        "displayName": displayName,
        "profilePic": selectedImage
    }

    const res = await fetch(apiUrl + "Users", {
        "method": "post",
        "headers": {
            "Content-Type": 'application/json',
        },
        'body': JSON.stringify(newUserDetails)
    });

    return res.status;
}

export async function createToken(currentUsername, password) {
    const userDetails = {
        "username": currentUsername,
        "password": password
    }
    const res = await fetch(apiUrl + "Tokens", {
        "method": "post",
        "headers": {
            "Content-Type": 'application/json',
        },
        'body': JSON.stringify(userDetails)
    });
    return res;
}

export async function getChats(token) {
    const res = await fetch(apiUrl + "Chats", {
        "method": "get",
        "headers": {
            "Content-Type": "application/json",
            "Authorization": "bearer " + token,
        },
    });
    const json = await res.json();
    return json;
}

export async function getUserDetails(username, token) {
    const res = await fetch(apiUrl + "Users/" + username, {
        "method": "get",
        "headers": {
            "Content-Type": "application/json",
            "Authorization": "bearer " + token,
        },
    });
    const json = await res.json();
    return json;
}

export async function addContact(token, contactName) {
    const res = await fetch(apiUrl + "Chats", {
        "method": "post",
        "headers": {
            "Content-Type": "application/json",
            "Authorization": "bearer " + token,
        },
        "body": JSON.stringify({"username": contactName}),
    });
    let ret = {
        "status": res.status,
        "json": null,
        "text": null,
    };
    if (res.status !== 200) 
        ret.text = await res.text()
    else 
        ret.json = await res.json()
    return ret;
}

export async function getChat(token, chatId, setChats) {
    const res = await fetch(apiUrl + "Chats/" + chatId.toString(), {
        "method": "get",
        "headers": {
            "Content-Type": "application/json",
            "Authorization": "bearer " + token,
        },
    });
    if (res.status !== 200) {
        return null;
    }
    const json = await res.json();
    return json;
}

export async function sendMessageTo(token, message, chatId) {
    const res = await fetch(apiUrl + "Chats/" + chatId.toString() + "/Messages", {
        method: "post",
        "headers": {
            "Content-Type": "application/json",
            "Authorization": "bearer " + token,
        },
        body: JSON.stringify({ "msg": message }),
    });
    if (res.status !== 200) {
        return null;
    }
    const json = await res.json()
    return json;
}

export async function getChatMessages(token, chatId) {
    const res = await fetch(apiUrl + "Chats/" + chatId.toString() + "/Messages", {
        "method": "get",
        "headers": {
            "Content-Type": "application/json",
            "Authorization": "bearer " + token,
        }
    });
    if (res.status !== 200)
        return null;
    const json = await res.json();
    return json;

}

export async function deleteChatId(token, chatId) {
    const res = await fetch(apiUrl + "Chats/" + chatId.toString(), {
        "method": "delete",
        "headers": {
            "Accept": "*/*",
            "Authorization": "bearer " + token,
        },
    });
    if (res.status === 204) {
        return true;
    }
    return false;
}