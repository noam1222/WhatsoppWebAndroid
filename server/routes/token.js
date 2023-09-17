const express =  require('express')
const isLoggedIn = require('../middleware.js')
const returnToken = require('../controllers/token.js')
const router = express.Router();

router.post('/', returnToken.returnToken);
router.post('/fire_token', returnToken.saveFireToken);

module.exports = router;
