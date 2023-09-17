import LoginContainerBody from "./body/LoginContainerBody";
import SignUpContainerBody from "./body/SignupContainerBody";
import ContainerHead from "./ContainerHead";
import "./LogContainer.css";
function LogContainer({ type, setToken, setUsername }) {

    let headline
    let body
    if (type === "login") {
        headline = "Login"
        body = <LoginContainerBody setToken={setToken} setUsername={setUsername} />
    } else {
        headline = "Register"
        body = <SignUpContainerBody />
    }
    return (
        <>
            <div className="top"></div>
            <div className="container w-50 h-20">
                <form className="bg-light w-50 rounded border ps-5 pe-5">
                    <ContainerHead headline={headline} />
                    {body}
                </form>
            </div>
            <div className="bottom"></div>
        </>
    );
}

export default LogContainer;