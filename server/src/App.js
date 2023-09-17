import LogContainer from "./LogContainer/LogContainer";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Chats from "./Chats/Chats";
import { useState } from "react";
import io from 'socket.io-client';
var socket = io("http://127.0.0.1:5000");


function App() {  
  const [token, setToken] = useState(null);
  const [username, setUsername] = useState(null);

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LogContainer type="login" setToken={setToken} setUsername={setUsername} />}></Route>
        <Route path="/signup" element={<LogContainer type="signup" />}></Route>
        <Route path="/chats" element={<Chats token={token} username={username} socket={socket}/>}></Route>
      </Routes>
    </BrowserRouter>
  );
}

export default App;
