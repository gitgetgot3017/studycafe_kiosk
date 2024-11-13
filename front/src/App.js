import './App.css';
import { Routes, Route, Link } from 'react-router-dom';
import Main from './pages/main/Main';
import Login from './pages/member/Login';
import Join from './pages/member/Join';
import Seat from './pages/seat/Seat';

function App() {

    return (
    <div className="App">
        <Routes>
            <Route path="/" element={<Main></Main>} />
            <Route path="/members/login" element={<Login></Login>} />
            <Route path="/members/join" element={<Join></Join>} />
            <Route path="/seats" element={<Seat></Seat>} />
        </Routes>
    </div>
  );
}

export default App;
