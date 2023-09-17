import { Link } from "react-router-dom";
function Button({ value, onClick, link , modalKind}) {
    return (
        <div className="col-3">
            <Link to={link} onClick={onClick}>
                <button className="btn btn-primary w-100" type="button"
                     data-toggle="modal" data-target={"#" + modalKind}>
                    {value}
                </button>
            </Link>
        </div>
    );
}

export default Button;