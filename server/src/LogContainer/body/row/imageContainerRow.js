import RawContainerRaw from "./rawContainerRow";
import { useRef } from "react";

function ImageContainerRow({ setState }) {
    const inputRef = useRef(null);
    const errHintRef = useRef(null);

    function handleChanges(event) {
        const file = event.target.files[0];
        if (file && file.type.startsWith("image/")) {
            const reader = new FileReader();
            reader.onload = (e) => {
                setState(e.target.result);
            }
            reader.readAsDataURL(file);
            inputRef.current.className = "form-control";
            errHintRef.current.innerText = "";
        } else {
            setState(null);
            if (!inputRef.current.className.includes(" is-invalid")) {
                inputRef.current.className += " is-invalid";
            }
            errHintRef.current.innerText = "Please upload image";
        }
    }

    return (
        <RawContainerRaw inputRef={inputRef} errHintRef={errHintRef}
            label="Picture:" type="file" accept="image/*" handleChanges={handleChanges} />
    );
}

export default ImageContainerRow;