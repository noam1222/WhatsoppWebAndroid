const Chat = require('../models/chats');
const UserPassName = require('../models/users');
const tokenMap = require('../models/token');
const jwt = require('jsonwebtoken');
const firebase = require('firebase-admin');
const serviceAccount = require('../whatsopandorid-firebase-adminsdk-hla5s-1d33397ff4.json');
const { setIo, updateWebClients } = require("../socketHandler");

firebase.initializeApp({
    credential: firebase.credential.cert(serviceAccount)
});

// Function to extract the username from a JWT token
const getUsernameFromToken = async (token) => {
    try {
        const decodedToken = jwt.verify(token, 'your-secret-key'); // Verify and decode the JWT token
        const username = decodedToken.username; // Extract the username from the decoded token
        return username;
    } catch (error) {
        console.error('Error decoding JWT token:', error);
        return null;
    }
}
// id for chats and messages
async function updateChatId(chat) {
    const maxIdChat = await Chat.chat.findOne().sort({ id: -1 }).select('id').lean();
    const maxId = maxIdChat ? maxIdChat.id : 0;
    return (maxId + 1);
}

const addChatFromFirebase = async (token, user) => {
    const message = {
        notification: {
          title: user,
          body: "Chat Added",
        },
        token: token,
      };
              
    // Send the message to Firebase
   await firebase.messaging().send(message)
      .then((response) => {
        console.log('Message sent successfully:', response);
      })
      .catch((error) => {
        console.error('Error sending message:', error);
      });

}

const newChat = async (newUsername, token) => {
    try {
        // Verify the token and perform any necessary authentication/validation
        // Get the currently logged-in user
        const temp = await getUsernameFromToken(token);

        const loggedInUser = await UserPassName.userPassName.findOne({ username: temp });

        // Check if the username is the same as the logged-in user
        if (newUsername === temp) {
            return null; // User cannot create a chat with themselves
        }
        // Check if the user with the given username exists
        const existingUser = await UserPassName.userPassName.findOne({ username: newUsername });
        if (!existingUser) {
            // User does not exist
            return null;
        }
        // Create a new chat document
        const chat = new Chat.chat({
            users: [{
                username: loggedInUser.username,
                displayName: loggedInUser.displayName,
                profilePic: loggedInUser.profilePic
            },
            {
                username: existingUser.username,
                displayName: existingUser.displayName,
                profilePic: existingUser.profilePic
            }]
        });
        const idChat = await updateChatId();
        chat.id = idChat;
        // Save the chat to the database
        await chat.save();

        //notify web users
        if (tokenMap.userTokenMap.has(temp)) {
            updateWebClients("newChat", newUsername);
        }

        //notify android users
        if (tokenMap.userTokenMap.has(newUsername)) {
            const token1 = tokenMap.userTokenMap.get(newUsername);
            await addChatFromFirebase(token1, newUsername);
        }

        // console.log(chat);
        // Return the chat ID and user details as a JSON object
        return {
            id: idChat,
            user: {
                username: existingUser.username,
                displayName: existingUser.displayName,
                profilePic: existingUser.profilePic
            }
        };

    } catch (error) {
        console.error('Error adding chat:', error);
        return null;
    }

}
const deleteChatFromFirebase = async (recipient, token, user) => {
    const message = {
        notification: {
          title: user,
          body: "Chat deleted",
        },
        token: token,
      };
              
    // Send the message to Firebase
   await firebase.messaging().send(message)
      .then((response) => {
        console.log('Message sent successfully:', response);
      })
      .catch((error) => {
        console.error('Error sending message:', error);
      });

}


const deleteChat = async (chatId, token) => {
    // Get the currently logged-in user
    const temp = await getUsernameFromToken(token);
    const loggedInUser = await UserPassName.userPassName.findOne({ username: temp });
    try {
        //  console.log(chatId);
        const chatDetails = await Chat.chat.findOne({ id: chatId });
        let chatParticipant = await chatDetails.users[1].username;
        if (chatParticipant === temp) {
            chatParticipant = await chatDetails.users[0].username;
        }
        const result = await Chat.chat.findOneAndDelete({ id: chatId });
        if (result.n != 0) { // Chat deleted succesfully
            //delete from firebase
            // get recipient token
    if (tokenMap.userTokenMap.has(chatParticipant)) {
        const token = tokenMap.userTokenMap.get(chatParticipant);
            await deleteChatFromFirebase(chatParticipant, token, loggedInUser.displayName);
    }
    //notify web users
    if (tokenMap.userTokenMap.has(temp)) {
        updateWebClients("deleteChat", {
            usernameOfDeletedChat: chatParticipant,
            deleteChatId: chatId,
        });
    }
            return true;
        } else {
            return null; // Chat not found or delete operation failed
        }
    } catch (error) {
        //   console.log("ariariari");
        return null;
    }

}
   
const sendMessageToFirebase = async (sender, token, messageContent) => {
    const message = {
        notification: {
          title: sender.displayName,
          body: messageContent.msg,
        },
        token: token,
      };
              
    // Send the message to Firebase
   await firebase.messaging().send(message)
      .then((response) => {
        console.log('Message sent successfully:', response);
      })
      .catch((error) => {
        console.error('Error sending message:', error);
      });
  }

const addMessage = async (chatId, message, token) => {
    // Get the currently logged-in user
    const temp = await getUsernameFromToken(token);
    const loggedInUser = await UserPassName.userPassName.findOne({ username: temp });

    try {
        // Find the chat by its ID
        const chat = await Chat.chat.findOne({ id: chatId });
        // user that receives message
        let userRecipient = await chat.users[1].username;
        if (userRecipient === temp)
            userRecipient = await chat.users[0].username;
        if (!chat) {
            // Chat not found
            return null;
        }
        // Get the maximum ID of existing messages
        const maxId = await chat.messages.reduce((max, message) => (message.id > max ? message.id : max), 0);
        // Create a new message document
        const newMessage = new Chat.message({
            id: (maxId + 1),
            created: new Date(),
            sender: {
                username: loggedInUser.username,
                displayName: loggedInUser.displayName,
                profilePic: loggedInUser.profilePic
            },
            content: message.msg
        });

        // Add the new message to the chat's messages array
        chat.messages.push(newMessage);
        // Save the updated chat to the database
        await chat.save();
        // check if user recipient is in the userToken Map, 
        // and if so tell firebase to send a message to his token
        if (tokenMap.userTokenMap.has(temp)) {
            updateWebClients("newMessage", {
                chatId: chatId,
                sendToUsername: userRecipient,
            });
        }
        if (tokenMap.userTokenMap.has(userRecipient)) {
                const token = tokenMap.userTokenMap.get(userRecipient);
                sendMessageToFirebase(loggedInUser, token, message);
              } 
           
        // Return the added message
        return {
            id: newMessage.id,
            created: newMessage.created,
            sender: {
                username: loggedInUser.username,
                displayName: loggedInUser.displayName,
                profilePic: loggedInUser.profilePic
            },
            content: newMessage.content
        }
    } catch (error) {
        console.error('Error adding message:', error);
        return null;
    }
}

const getMessages = async (chatId) => {
    try {
        // Find the chat by its ID
        const chat = await Chat.chat.findOne({ id: chatId });

        if (!chat) {
            // Chat not found
            return null;
        }
        // Extract the desired details from each message
        const messageObjects = chat.messages.map(message => {
           // console.log(message.sender);
            return {
                id: message.id,
                created: message.created,
                sender: {
                    username: message.sender[0].username
                },
                content: message.content
            };
        });

        return messageObjects;
    } catch (error) {
        console.error('Error retrieving chat messages:', error);
        return null;
    }
}

function chatReturnFormat(chat) {
    chat = chat.toObject();
    delete chat._id;
    delete chat.__v;
    chat.users.shift();
    return chat;
}

const oneChat = async (chatId) => {
    try {
        const chat = await Chat.chat.findOne({ id: chatId })
        // Chat not found
        if (!chat) {
            return null;
        }
        chatFormat = await chatReturnFormat(chat);
        //return chat object
        return chatFormat;
    } catch (error) {
        console.error('Error retrieving chat:', error);
        return null;
    }

}

const allChats = async (token) => {
    // Get the currently logged-in user
    const loggedInUser = await getUsernameFromToken(token);
    try {
        // Find the user by username
        const user = await UserPassName.userPassName.findOne({ username: loggedInUser });
        if (!user) {
            // User not found
            return null;
        }
        // Find all chats where the user is a participant
        const chats = await Chat.chat.find({
            'users.username': loggedInUser
        });

        const chatObjects = [];

        for (const chat of chats) {
            // find last message
            let lastMessage = null;
            if (chat.messages.length > 0) {
                lastMessage = await chat.messages[chat.messages.length - 1];
            }
            let singleUser = await chat.users.find(user => user.username !== loggedInUser);
            singleUser = singleUser.toObject();
            delete singleUser._id;
            const chatObject = {
                id: chat.id,
                user: singleUser,
                lastMessage: lastMessage ? {
                    id: lastMessage.id,
                    created: lastMessage.created,
                    content: lastMessage.content
                } : null
            };
            chatObjects.push(chatObject);
        }
        return chatObjects;
    } catch (error) {
        console.error('Error retrieving chats:', error);
        return null;
    }

}

module.exports = { deleteChat, getMessages, addMessage, newChat, oneChat, allChats };