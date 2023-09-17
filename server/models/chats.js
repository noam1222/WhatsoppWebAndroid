const User = require('../models/users');

const mongoose = require('mongoose');
const Schema = mongoose.Schema;


const MessageSchema = new Schema({
    id: {
      type: Number,
      required: false
    },
    created: {
      type: Date,
      default: Date.now
    },
    sender: {
      type: [User.user.schema], // Use ObjectId type
      required: true
    },
    content: {
      type: String,
      required: true
    }
  });

  const ChatSchema = new Schema({
    id: {
      type: Number,
      required: false
    },
    users: {
      type: [User.user.schema],
      required: true,
    },
    messages: {
      type: [MessageSchema],
      required: false
    }
  });


  const message = mongoose.model('Message', MessageSchema)
  const chat = mongoose.model('Chat', ChatSchema)
// Export all schemas
module.exports = {
   chat, message
  };