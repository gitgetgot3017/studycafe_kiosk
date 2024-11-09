import './App.css';
import { Routes, Route, Link } from 'react-router-dom';
import Login from './pages/member/Login';
import Join from './pages/member/Join';

function App() {
  return (
    <div className="App">
        <Routes>
            <Route path="/members/login" element={<Login></Login>} />
            <Route path="/members/join" element={<Join></Join>} />
        </Routes>
    </div>
  );
}

export default App;
