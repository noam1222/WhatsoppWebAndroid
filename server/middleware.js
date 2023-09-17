const jwt = require('jsonwebtoken');

const isLoggedIn = (req, res, next) => {
    if (req.headers.authorization) {
       const token = req.headers.authorization.split(" ")[1];
       try {
          const data = jwt.verify(token, key);
          return next();
       } catch (error) {
          return res.status(401).send("Invalid Token");
       }
    } else {
       return res.status(403).send('Token required');
    }
 };
 
 const key = "your-secret-key";
 module.exports = { key, isLoggedIn };