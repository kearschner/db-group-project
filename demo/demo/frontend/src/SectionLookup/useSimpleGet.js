import {useEffect, useState} from "react";

function useSimpleGet(mapping = "") {
    const [data, setData] = useState([]);
    useEffect(() => {
        fetch(mapping)
            .then(response => response.json())
            .then(data => setData(data));
    }, [mapping]);
    return data;
}

export default useSimpleGet;
