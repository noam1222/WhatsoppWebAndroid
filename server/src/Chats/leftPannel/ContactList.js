function formatTime(timesStr) {
    let split1 = timesStr.split('T');
    const date = split1[0];
    let split2 = split1[1].split('.');
    const time = split2[0];
    return date + " " + time;
}

function ContactList({ chats, activeChatDetails, setActiveChatDetails }) {
    return (
        <div id="contacts-list" className="overflow-auto"> 
            <div>
                {chats.length !== 0 &&
                    chats.map((contact) => {
                        return <>
                            <div key={contact.id}
                                className={`row-pannel contact-info-panel ${
                                    activeChatDetails && contact.id === activeChatDetails.id 
                                    ? "open-chat-contact" : ""}`}
                                onClick={() => {
                                    setActiveChatDetails(contact);
                                }
                                }
                            >
                                <img
                                    src={contact.user.profilePic}
                                    className="rounded-circle profile-picture"
                                    alt={contact.user.displayName}
                                />
                                <span className="contact-info">
                                    <span className="username username-in-list">{contact.user.displayName}</span>
                                    <br />
                                    <span className="last-message">{contact.lastMessage ? contact.lastMessage.content : ""}</span>
                                </span>
                                <p className="last-message-date-and-time">{contact.lastMessage ? formatTime(contact.lastMessage.created) : ""}</p>
                            </div>
                        </>
                    })}
            </div>
        </div>
    );
}


export default ContactList;