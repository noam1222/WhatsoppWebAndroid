import ContactList from "./ContactList"
import TopLeftPannel from "./TopLeftPannel";



function LeftPannel({ userDetails, chats, activeChatDetails, setActiveChatDetails }) {
    return (
        <div id="leftPanel">
            <TopLeftPannel profileimage={userDetails.profilePic} displayName={userDetails.displayName} />
            <ContactList chats={chats}
                activeChatDetails={activeChatDetails} setActiveChatDetails={setActiveChatDetails} />
        </div>
    );
}

export default LeftPannel;