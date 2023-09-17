function RawContainerRaw({ label, type, id, placeholder, inputRef, errHintRef, handleChanges }) {
    return (
        <div className="row mb-3">
            <div className="col-md-3">{label}</div>
            <div className="col-md-9">
                <div className="form-group">
                    <input onChange={handleChanges} ref={inputRef} type={type} className="form-control"
                        id={id} placeholder={placeholder}></input>
                    <span ref={errHintRef} className="help-block"></span>
                </div>
            </div>
        </div>
    );
}

export default RawContainerRaw;