const express = require('express')
const getAllChats = require('../controllers/chats.js')
const addChat = require('../controllers/chats.js')
const getSingleChat = require('../controllers/chats.js')
const deleteChat = require('../controllers/chats.js')
const sendMessage = require('../controllers/chats.js')
const showMessages = require('../controllers/chats.js')
const isLoggedIn = require('../middleware.js')

const router = express.Router();


router.get('/', isLoggedIn.isLoggedIn, getAllChats.getAllChats);
router.post('/',  isLoggedIn.isLoggedIn, addChat.addChat);
router.get('/:id', isLoggedIn.isLoggedIn, getSingleChat.getSingleChat);
router.delete('/:id', isLoggedIn.isLoggedIn, deleteChat.deleteChat);
router.post('/:id/Messages', isLoggedIn.isLoggedIn, sendMessage.sendMessage);
router.get('/:id/Messages', isLoggedIn.isLoggedIn, showMessages.showMessages);

module.exports = router;