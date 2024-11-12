import './App.css';
import { Routes, Route, Link } from 'react-router-dom';
import Main from './pages/main/Main';
import Login from './pages/member/Login';
import Join from './pages/member/Join';
import Seat from './pages/seat/Seat';
import {useState} from "react";

function App() {

    let [userInOut, setUserInOut] = useState(false);

    return (
    <div className="App">
        <Routes>
            <Route path="/" element={<Main userInOut={userInOut}></Main>} />
            <Route path="/members/login" element={<Login></Login>} />
            <Route path="/members/join" element={<Join></Join>} />
            <Route path="/seats" element={<Seat setUserInOut={setUserInOut}></Seat>} />
        </Routes>
    </div>
  );
}

export default App;
