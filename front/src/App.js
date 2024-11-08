import './App.css';
import { Routes, Route, Link } from 'react-router-dom';
import Login from './pages/member/Login';

function App() {
  return (
    <div className="App">
        <Routes>
            <Route path="/members/login" element={<Login></Login>} />
        </Routes>
    </div>
  );
}

export default App;
