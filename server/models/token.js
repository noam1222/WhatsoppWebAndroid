const jwt = require('jsonwebtoken');
const User = require('./users');
const key = require('../middleware.js')
const mongoose = require('mongoose');

// Create a map of usernames and tokens
const userTokenMap = new Map();

const getToken = async(username, password) => {
    
    try {
        // Find the user by username and password
        const user = await User.userPassName.findOne({ username: username, password: password });

        if (!user) {
          return null;
        };
        
        // Create a payload for the token (you can customize it based on your needs)
        const payload = {
          username: user.username,
        };
        // Generate a token with the payload and a secret key
        const token = jwt.sign(payload, key.key, { expiresIn: '1h' });
    
        // Return the generated token
        return token;
      } catch (error) {
        // Handle any errors that occur during the operation
        return null;
      }

}

const saveToken = async(username, token) => {
  try {
    userTokenMap.set(username, token);
    return "success";
  } catch (error) {
     // Handle any errors that occur during the operation
     return null;
  }

}

module.exports = { getToken, saveToken, userTokenMap };

