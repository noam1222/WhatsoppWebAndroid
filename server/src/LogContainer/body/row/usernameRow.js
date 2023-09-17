import { useRef } from "react";
import RawContainerRaw from "./rawContainerRow";

function UsernameRow({ label, type, id, placeholder, isValidInput, setState}) {
    const inputRef = useRef(null);
    const errHintRef = useRef(null);

    const handleChanges = (event) => {
        let result = isValidInput(event.target.value);
        if (result === "") {
            setState(event.target.value);
            inputRef.current.className = "form-control";
            errHintRef.current.innerText = "";
        } else {
            setState(null);
            if (!inputRef.current.className.includes(" is-invalid")) {
                inputRef.current.className += " is-invalid";
            }
            errHintRef.current.innerText = result;
        }
    }

    return (
        <RawContainerRaw label={label} type={type} placeholder={placeholder}
            id={id} inputRef={inputRef} errHintRef={errHintRef} handleChanges={handleChanges} />
    );
}

export default UsernameRow;