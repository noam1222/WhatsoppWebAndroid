import ContainerRow from "./row/ContainerRow";
import LogBtm from "../Bottom/LogBtm";
import { isValidPassword, isValidDisplayName } from "./logic";
import { useState } from "react";
import WarningModal from "../../WarningModal";
import { useNavigate } from "react-router-dom";
import $ from 'jquery'
import { createToken } from "../../network";

function LoginContainerBody({ setToken, setUsername }) {
    const navigator = useNavigate();
    const [currentUsername, setCurrentUsername] = useState('');
    const [password, setPassword] = useState('');
    const userFunc = isValidDisplayName;
    const passFunc = isValidPassword;

    async function handleLogAttempt(e) {
        e.preventDefault();
        if (!currentUsername || !password) {
            $("#WarningModal").modal("show");
            return;
        }
        const res = await createToken(currentUsername, password);
        if (res.status === 200) {
            $('.modal-backdrop').remove();
            const token = await res.text()
            setToken(token);
            setUsername(currentUsername);
            navigator("/chats");
        } else {
            $("#WarningModal").modal("show");
        }
    }

    return (
        <>
            <ContainerRow label="Username:" type="text" id="username" placeholder="Enter username"
                isValidInput={userFunc} setState={setCurrentUsername} errorTxt="Must be at least 4 characters long" />
            <ContainerRow label="Password:" type="password" id="password" placeholder="Enter password"
                isValidInput={passFunc} setState={setPassword}
                errorTxt="Must be at least 8 characters long and contains 1-2 numeric characters." />
            <LogBtm btnValue="Login" onClick={handleLogAttempt} buttonLink="/chats"
                footnoteText="Not registered yet? Click here to register."
                footnoteLink="/signup" modalKind="WarningModal" />
            <WarningModal content="Bad username or password" />
        </>
    );
}

export default LoginContainerBody;