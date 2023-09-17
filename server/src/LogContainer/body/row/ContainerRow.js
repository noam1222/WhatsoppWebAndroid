import { useRef } from "react";
import RawContainerRaw from "./rawContainerRow";

function ContainerRow({ label, type, id, placeholder, isValidInput, setState, errorTxt }) {
    const inputRef = useRef(null);
    const errHintRef = useRef(null);

    const handleChanges = (event) => {
        if (isValidInput(event.target.value)) {
            setState(event.target.value);
            inputRef.current.className = "form-control";
            errHintRef.current.innerText = "";
        } else {
            setState(null);
            if (!inputRef.current.className.includes(" is-invalid")) {
                inputRef.current.className += " is-invalid";
            }
            errHintRef.current.innerText = errorTxt;
        }
    }

    return (
        <RawContainerRaw label={label} type={type} placeholder={placeholder}
            id={id} inputRef={inputRef} errHintRef={errHintRef} handleChanges={handleChanges} />
    );
}

export default ContainerRow;