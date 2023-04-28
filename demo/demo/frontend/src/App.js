import {Route, BrowserRouter as Router, Routes} from "react-router-dom";
import SectionLookup from "./SectionLookup/SectionLookup";
import SectionLookupResults from "./SectionLookupResults";
import ScheduleLookup from "./ScheduleLookup/ScheduleLookup";


function App() {
    return (
        <Router>
            <Routes>
                <Route exact path="/" element={<SectionLookup />} />
                <Route exact path="/sec-lookup-results" element={<SectionLookupResults/>} />
                <Route exact path="/schedule-lookup" element={<ScheduleLookup/>} />
            </Routes>
        </Router>
    );
}

export default App;