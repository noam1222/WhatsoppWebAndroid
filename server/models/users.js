const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const User = new Schema({
   username: {
      type: String,
      required: true
   },
   displayName: {
      type: String,
      required: true
   },
   profilePic: {
      type: String,
      required: true
   }
});


const UserPass = new Schema({
   username: {
      type: String,
      required: true
   },
   password: {
      type: String,
      required: true
   }
});


const UserPassName = new Schema({
   username: {
      type: String,
      required: true,
      unique: true
   },
   password: {
      type: String,
      required: true
   },
   displayName: {
      type: String,
      required: true
   },
   profilePic: {
      type: String,
      required: true
   }
});

const user = mongoose.model('User', User);
const userPass = mongoose.model('UserPass', UserPass);
const userPassName = mongoose.model('UserPassName', UserPassName);

module.exports = {
   user,
   userPass,
   userPassName
};


