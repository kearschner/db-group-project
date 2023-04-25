import {Route, BrowserRouter as Router, Routes} from "react-router-dom";
import SectionLookup from "./SectionLookup/SectionLookup";
import SectionLookupResults from "./SectionLookupResults";


function App() {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<SectionLookup />} />
                <Route exact path="/sec-lookup-results" element={<SectionLookupResults/>} />
            </Routes>
        </Router>
    );
}

export default App;