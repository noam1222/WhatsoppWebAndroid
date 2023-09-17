import { Link } from "react-router-dom";

function Footnote({text, link}) {
    let parts = text.split("Click here");
    return (
        <div className="col-md-9 text-center">
            {parts[0]} <Link to={link}>Click here</Link> {parts[1]}
        </div>
    );
}

export default Footnote;