const chatModel = require('../services/chats.js');

const getAllChats = async (req, res) => {
    const authorizationHeader = req.headers.authorization;

    if (authorizationHeader) {
        const token = authorizationHeader.split(' ')[1]; // Extract the token from the "Bearer <token>" format
        const chats = await chatModel.allChats(token);
        if (chats) {
            res.status(200).json(chats);
        } else {
            res.status(409).send('Conflict');
        }
    } else {
        // No authorization token provided
        res.status(401).json({ error: 'Unauthorized' });
    }
}

const getSingleChat = async (req, res) => {
    const result = await chatModel.oneChat(req.params.id);
    if (result) {
        res.status(200).json(result);
    } else {
        res.status(409).send('Conflict');
    }
}

const addChat = async (req, res) => {
    const authorizationHeader = req.headers.authorization;

    if (authorizationHeader) {
        const token = authorizationHeader.split(' ')[1]; // Extract the token from the "Bearer <token>" format
        //  console.log(req.body.username);
        const result = await chatModel.newChat(req.body.username, token); // send token as well to make sure user doesn't open chat with himself
        //  console.log(result)
        if (result) {
            res.status(200).json(result);
        } else {
            res.status(409).send('Invalid Contact');
        }
    } else {
        // No authorization token provided
        res.status(401).json({ error: 'Unauthorized' });
    }


}

//delete specific chat
const deleteChat = async (req, res) => {
    const authorizationHeader = req.headers.authorization;

    if (authorizationHeader) {
        const token = authorizationHeader.split(' ')[1]; // Extract the token from the "Bearer <token>" format
        //  console.log(req.body.username);
    //  console.log(req.params.id)
    const deleted = await chatModel.deleteChat(req.params.id, token);
    if (deleted) {
        res.status(204).send('ok');
    } else {
        res.status(409).send('Conflict');
    }
}
}

const sendMessage = async (req, res) => {
    const authorizationHeader = req.headers.authorization;

    if (authorizationHeader) {
        const token = authorizationHeader.split(' ')[1]; // Extract the token from the "Bearer <token>" format
        const result = await chatModel.addMessage(req.params.id, req.body, token);
        if (result) {
            res.status(200).json(result);
        } else {
            res.status(409).send('Conflict');
        }
    } else {
        res.status(401).json({ error: 'Unauthorized' });
    }
}

const showMessages = async (req, res) => {
    const messages = await chatModel.getMessages(req.params.id);
    if (messages) {
        res.status(200).json(messages);
    } else {
        res.status(409).send('Conflict');
    }
}

module.exports = { getAllChats, getSingleChat, addChat, deleteChat, sendMessage, showMessages };
