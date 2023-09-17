import AddContact from "./Addcontact";
import "./Chats.css";
import LeftPannel from "./leftPannel/LeftPannel";
import RightPannel from "./rightPannel/RightPannel";
import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { getChats, getUserDetails } from "../network";


function Chats({ token, username, socket }) {
    const [activeChatDetails, setActiveChatDetails] = useState(null);
    const [chats, setChats] = useState([]);
    const [userDetails, setUserDetails] = useState({});
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        let mounted = true;

        const fetchData = async () => {
            setLoading(true);
            const chatData = await getChats(token);
            const usrDtls = await getUserDetails(username, token);
            if (mounted) {
                setChats(chatData);
                setUserDetails(usrDtls);
            }
            setLoading(false);
        };

        fetchData();

        socket.on("newChat", async (newChatUsername) => {
            if (newChatUsername === username) {
                const newChats = await getChats(token);
                setChats(newChats);
            }
        });

        return () => {
            mounted = false;
        };
    }, [token, username, socket, setChats]);

    return (
        <div>
            <div className="top">
                <Link to="/">
                    <button id="logout" type="button" className="btn btn-danger">Log out</button>
                </Link>
                <div id="chatsBox">
                    <LeftPannel userDetails={userDetails} chats={chats}
                        activeChatDetails={activeChatDetails} setActiveChatDetails={setActiveChatDetails} />
                    <RightPannel token={token} activeChatDetails={activeChatDetails}
                        setActiveChatDetails={setActiveChatDetails} currentUsername={username} setChats={setChats}
                        chats={chats} loading={loading} setLoading={setLoading} socket={socket} />
                </div>
                <AddContact token={token} chats={chats} setChats={setChats} socket={socket} userDetails={userDetails} />
            </div>
            <div className="bottom"></div>
        </div>
    );
}

export default Chats;
