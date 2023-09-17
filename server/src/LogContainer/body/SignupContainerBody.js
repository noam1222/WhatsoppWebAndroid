import LogBtm from "../Bottom/LogBtm";
import ContainerRow from "./row/ContainerRow";
import ImageContainerRow from "./row/imageContainerRow";
import { isValidPassword, isValidDisplayName, isValidNewUsername } from "./logic";
import { useState } from "react";
import UsernameRow from "./row/usernameRow";
import WarningModal from "../../WarningModal";
import { useNavigate } from 'react-router-dom';
import $ from 'jquery';
import { createUser } from "../../network";

function SignUpContainerBody() {
    const navigate = useNavigate();
    const [username, setUsername] = useState('')
    const [pass, setPass] = useState('');
    const [verifiedPass, setVerifyPass] = useState('');
    const [displayName, setDisplayName] = useState('');
    const [selectedImage, setSelectedImage] = useState(null);
    const passFunc = isValidPassword;
    const displayNameFunc = isValidDisplayName;
    const usernameFunc = isValidNewUsername;

    const [errorMsg, setErrorMsg] = useState("Missing information");

    async function addUser(e) {
        e.preventDefault();
        if (username && pass && verifiedPass && displayName && selectedImage) {
            const resStatus = await createUser(username, pass, displayName, selectedImage);  
            if (resStatus === 200) {
                $('.modal-backdrop').remove();
                navigate("/");
                return;
            } else {
                if (resStatus === 409) {
                    setErrorMsg("Username already exist");
                } else {
                    setErrorMsg("Error in server");
                }
                $("#WarningModal").modal("show");
            }
        } else {
            setErrorMsg("Missing information");
            $("#WarningModal").modal("show");
        }
    }


    return (
        <>
            <UsernameRow label="Username:" type="text" id="username" placeholder="Enter username"
                isValidInput={usernameFunc} setState={setUsername} />
            <ContainerRow label="Password:" type="password" id="password" placeholder="Enter password"
                isValidInput={passFunc} setState={setPass}
                errorTxt="Must be at least 8 characters long and contains 1-2 numeric characters." />
            <ContainerRow label="Verify Password:" type="password" id="password-verify"
                placeholder="Enter password again" isValidInput={(val) => { return val === pass; }}
                setState={setVerifyPass} errorTxt="Passwords don't match!" />
            <ContainerRow label="Display Name:" type="text" id="display-name" placeholder="Enter Display Name"
                isValidInput={displayNameFunc} setState={setDisplayName} errorTxt="Must be at least 4 characters long" />
            <ImageContainerRow setState={setSelectedImage} />
            <div className="row mb-5">
                <div className="col md-3 d-flex align-items-center">
                    {selectedImage && <img src={selectedImage} alt="Uploaded image" className="profilePicture" />}
                    <br />
                    <br />
                </div>
                <LogBtm btnValue="Sign Up" onClick={addUser}
                    buttonLink="/" modalKind="WarningModal"
                    footnoteText="Already registered? Click here to login." footnoteLink="/" />
            </div>
            <WarningModal content={errorMsg} />
        </>
    );
}

export default SignUpContainerBody;