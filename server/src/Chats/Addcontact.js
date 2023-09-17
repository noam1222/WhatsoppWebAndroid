import $ from 'jquery';
import { useRef, useState } from "react";
import { addContact } from '../network';

function AddContact({ token, chats, setChats, socket, userDetails }) {
    const contactUsernameRef = useRef(null);
    const [errMsg, setErrMsg] = useState("");
    const modalRef = useRef(null);

    const addChat = async () => {
        const contactName = contactUsernameRef.current ? contactUsernameRef.current.value : "";
        let res = await addContact(token, contactName);
        if (res.status !== 200) {
            contactUsernameRef.current.className = "form-control is-invalid";
            setErrMsg(res.text);
        } else {
            contactUsernameRef.current.className = "form-control";
            setErrMsg("");
            const newChat = res.json;
            newChat["lastMessage"] = null;
            setChats([...chats, newChat]);
            $("#addContactModal .btn-secondary").click() // close the modal
            socket.emit("newChat", contactName);
        }
        if (contactUsernameRef.current)
            contactUsernameRef.current.value = "";
    }

    return (
        <div ref={modalRef} className="modal fade" id="addContactModal" tabIndex="-1" aria-labelledby="addContactModalLabel" style={{ display: "none" }} aria-hidden="true">
            <div className="modal-dialog">
                <div className="modal-content">
                    <div className="modal-header">
                        <h1 className="modal-title fs-5" id="addContactModalLabel">Add new contact</h1>
                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div className="modal-body">
                        <input ref={contactUsernameRef} id="contact-username" className="form-control" type="text" placeholder="Contact's username" />
                        <span className="help-block">{errMsg}</span>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="button" className="btn btn-primary"
                            data-bs-dismiss=""
                            onClick={addChat}>Add</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default AddContact;





