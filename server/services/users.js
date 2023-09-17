const User = require('../models/users');


const findUser = async(username, token) => {
    try {
       // Find the user by username
       const user = await User.userPassName.findOne({ username: username });
      
       if (!user) {
        return null;
       }
     //  console.log(user);
       // Return the user details
       const returnUser = {
        username: user.username,
        displayName: user.displayName,
        profilePic: user.profilePic
       }
       return returnUser;
     } catch (error) {
       // Handle any errors that occur during the operation
       return null;
     }
 
 }

// REGISTERS NEW USER INTO THE DATABASE
const registerUser = async(userData) =>{
    try {
    
       // Create a new instance of the User model with the provided data
       const newUser = new User.userPassName(userData);
       // Save the new user to the database
       const savedUser = await newUser.save();
       // Return the user model without the password field
       const returnUser = {
        username: newUser.username,
        displayName: newUser.displayName,
        profilePic: newUser.profilePic
       }
       return returnUser;
     } catch (error) {
       // Handle any errors that occur during registration
       return null;
     }
 
 }

 module.exports = { registerUser, findUser };