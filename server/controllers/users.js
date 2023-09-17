const registerModel = require('../services/users.js')

//receive username (GET)
const returnUser = async (req, res) => {
 //   console.log(req.params.username)
    const result = await (registerModel.findUser(req.params.username));
    if (result) {
        res.status(200).json(result);
    } else {
        res.status(409).send('Conflict');
    }

}

//receive username and password (POST)
const addUser = async (req, res) => {
    const {username, password, displayName, profilePic } = req.body;
    const newUser = {
        username: username,
        password: password,
        displayName: displayName,
        profilePic: profilePic,
    };
    try {
        const result = await (registerModel.registerUser(newUser));
    if (result) {
        res.status(200).json(result);
    } else {
        res.status(409).send('Conflict');
    }
} catch (error) {
    res.status(500).send('Conflict');
}
}

module.exports = {
    returnUser,
    addUser
};
