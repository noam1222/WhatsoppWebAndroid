function formatTime(str) {
    const split1 = str.split("T");
    const split2 = split1[1].split(":");
    const hour = split2[0];
    const minutes = split2[1];
    return hour + ":" + minutes;
}

function getMessageType(messageSender, currentUsername) {
    if (messageSender === currentUsername)
        return "outcoming";
    return "incoming";
}

function MessageHistory({ messages, currentUsername }) {
    return (
        <>
            {messages && Array.isArray(messages) &&
                messages.map((message, index) => (
                    <div key={index} className={`message ${getMessageType(message.sender.username, currentUsername)}`}>
                        {message.content}
                        <span className="message-time">{formatTime(message.created)}</span>
                    </div>
                ))}
        </>
    );
}

export default MessageHistory;