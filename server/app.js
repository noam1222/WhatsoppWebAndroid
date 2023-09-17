const express = require('express');
const app = express();

const http = require("http");
const server = http.createServer(app);
const { Server } = require("socket.io");
const io = new Server(server, {
   cors: {
      origin: '*'
   }
});

const { setIO, updateWebClients } = require("./socketHandler.js")
setIO(io);

const cors = require('cors');
app.use(cors());

const bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

app.use(express.json({ limit: "1000mb" }));

const mongoose = require('mongoose');
mongoose.connect("mongodb://127.0.0.1:27017", {
   useNewUrlParser: true,
   useUnifiedTopology: true
});

const isLoggedIn = require('./middleware.js');
const userRouter = require('./routes/users.js')
const tokenRouter = require('./routes/token.js')
const chatsRouter = require('./routes/chats.js')

app.use(express.static('public'));
app.use('/api/Users', userRouter);
app.use('/api/Tokens', tokenRouter);
app.use('/api/Chats', isLoggedIn.isLoggedIn, chatsRouter);


io.on("connection", (socket) => {
   socket.on("newMessage", (messageDetails) => {
      socket.broadcast.emit("newMessage", messageDetails);
   });

   socket.on("newChat", (username) => {
      socket.broadcast.emit("newChat", username);
   });

   socket.on("deleteChat", (deleteDetails) => {
      socket.broadcast.emit("deleteChat", deleteDetails);
   });
});

server.listen(5000);