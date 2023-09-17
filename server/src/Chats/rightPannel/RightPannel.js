import { useRef, useState, useEffect } from "react";
import MessageHistory from "./MessageHistory";
import TopRightPannel from "./TopRightPannel";
import MessageSending from "./MessageSending";
import { getChats, getChatMessages, sendMessageTo, deleteChatId } from "../../network";

function isAllWhitespace(str) {
    return /^\s*$/.test(str);
}

function RightPannel({ token, activeChatDetails, setActiveChatDetails, currentUsername, setChats, chats
    , loading, setLoading, socket }) {
    const messageInputRef = useRef(null);
    const [chatMessages, setChatMessages] = useState([]);

    useEffect(() => {
        let mounted = true;

        const fetchData = async () => {
            let chatMsgs = await getChatMessages(token, activeChatDetails.id);
            if (mounted && chatMsgs !== null) {
                chatMsgs = chatMsgs.reverse();
                setChatMessages(chatMsgs);
            }
        }

        if (activeChatDetails !== null) {
            setLoading(true);
            fetchData();
            setLoading(false);
        }

        socket.on("newMessage", async (messageDetails) => {
            if (messageDetails.sendToUsername === currentUsername) {
                if (activeChatDetails && messageDetails.chatId === activeChatDetails.id){
                    setLoading(true);
                }
                fetchData();
                const newChats = await getChats(token);
                setChats(newChats);
                setLoading(false);
            }
        });

        socket.on("deleteChat", async (deleteDetails) => {
            if (deleteDetails.usernameOfDeletedChat === currentUsername) {
                if (activeChatDetails && deleteDetails.deleteChatId === activeChatDetails.id) {
                    setLoading(true);
                    setActiveChatDetails(null);
                }
                const newChats = await getChats(token);
                setChats(newChats);
                setLoading(false);
            }
        });

        return () => {
            mounted = false;
        };
    }, [token, activeChatDetails, setActiveChatDetails, setLoading, socket, setChats, currentUsername]);

    if (activeChatDetails === null) {
        return (
            <div id="chatPanel">
                <div className="row-pannel top-inner-panel"></div>
                <div id="chatDialog">
                    {loading && <div id="spinner">
                        <div class="spinner-border text-success" role="status">
                            <span class="sr-only">Loading...</span>
                        </div>
                    </div>}
                </div>
                <MessageSending />
            </div>
        );
    }

    function addMessageToActiveChat(newMsg) {
        const chatMsgs = [newMsg, ...chatMessages];
        setChatMessages(chatMsgs);
    }

    function notifyLastMessage(newMsg, contactChatId) {
        const chatsForLastMessage = [...chats];
        const CurrentUserIndex = chatsForLastMessage.findIndex((chat) => chat.id === contactChatId);
        if (CurrentUserIndex !== null) {
            chatsForLastMessage[CurrentUserIndex].lastMessage = newMsg;
            setChats(chatsForLastMessage);
        }
    }

    async function sendMessage() {
        setLoading(true);

        //send the message
        const content = messageInputRef.current.value;
        if (isAllWhitespace(content))
            return;
        const newMsg = await sendMessageTo(token, content, activeChatDetails.id)
        if (newMsg !== null) {
            addMessageToActiveChat(newMsg);
            notifyLastMessage(newMsg, activeChatDetails.id);
            socket.emit("newMessage",{
                chatId: activeChatDetails.id,
                sendToUsername: activeChatDetails.user.username,
            } );
        }

        setLoading(false);
    }

    async function deleteActiveChat() {
        setLoading(true);
        if (await deleteChatId(token, activeChatDetails.id)) {
            setActiveChatDetails(null);
            const newChats = await getChats(token);
            setChats(newChats);
            setLoading(false);
            socket.emit("deleteChat",  {
                usernameOfDeletedChat: activeChatDetails.user.username,
                deleteChatId: activeChatDetails.id,
            });
        }
        setLoading(false);
    }

    return (
        <div id="chatPanel">
            <TopRightPannel activeUserDetails={activeChatDetails.user} deleteChat={deleteActiveChat} />
            <div id="chatDialog">
                {loading && <div id="spinner">
                    <div class="spinner-border text-success" role="status">
                        <span class="sr-only">Loading...</span>
                    </div>
                </div>}
                <div id="messages-list" class="overflow-auto">
                    <MessageHistory messages={chatMessages} currentUsername={currentUsername} />
                </div>
            </div>
            <MessageSending messageInputRef={messageInputRef} sendMessage={sendMessage} />
        </div>
    );


}

export default RightPannel;