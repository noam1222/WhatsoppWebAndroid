const express = require('express');
const { returnUser, addUser } = require('../controllers/users.js');
const isLoggedIn = require('../middleware');

const router = express.Router();

router.get('/:username',isLoggedIn.isLoggedIn, returnUser);
router.post('/', addUser);

module.exports = router;