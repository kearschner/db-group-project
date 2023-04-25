import React from 'react';

const LookupSelect = (props) => {
    return (
        <div className="form-group">
            <label htmlFor={props.id}>{props.label + ':'}</label>
            <select
                id={props.id}
                name={props.id + "[]"}
                onChange={props.changeHandler}
                size={props.size}
                multiple
                style={{ overflowY: 'scroll', width: '200px', height: '100px' }}
                defaultValue={["Any"]}
            >
                <option key={0} value={"Any"}>Any</option>
                {
                    props.contents.map((item, index) =>(
                        <option key={index + 1} value={item}>
                            {item}
                        </option>
                    ))
                }
            </select>
        </div>
    );

};
LookupSelect.defaultProps = {
    changeHandler: undefined
}
export default LookupSelect;