import Button from "./Button";
import Footnote from "./Footnote"

function LogBtm({ btnValue, onClick, buttonLink, footnoteText, footnoteLink, modalKind }) {
    return (
        <div className="row mb-3">
            <Button value={btnValue} onClick={onClick} link={buttonLink} modalKind={modalKind} />
            <Footnote text={footnoteText} link={footnoteLink} />
        </div>
    );
}

export default LogBtm;