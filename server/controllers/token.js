const tokenModel = require('../models/token.js');

const returnToken = async(req, res) => {
    const result = await tokenModel.getToken(req.body.username, req.body.password);
    // console.log(result)
    if (result) {
        res.status(200).type("text").send(result);
    } else {
        res.status(404).send('Conflict');
    }
}

const saveFireToken = async(req, res) => {
    const result = await tokenModel.saveToken(req.body.username, req.body.token);
    if (result) {
        res.status(200).type("text").send(result);
    } else {
        res.status(404).send('Conflict');
    }
}

module.exports = { returnToken, saveFireToken };
