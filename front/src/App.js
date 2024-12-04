import './App.css';
import { Routes, Route, Link } from 'react-router-dom';
import Main from './pages/main/Main';
import Login from './pages/member/Login';
import Join from './pages/member/Join';
import Seat from './pages/seat/Seat';
import ItemList from './pages/item/ItemList';
import ItemDetail from './pages/item/ItemDetail';
import OrderList from './pages/order/OrderList';
import PostList from './pages/post/PostList';

function App() {

    return (
    <div className="App">
        <Routes>
            <Route path="/" element={<Main></Main>} />
            <Route path="/members/login" element={<Login></Login>} />
            <Route path="/members/join" element={<Join></Join>} />
            <Route path="/seats" element={<Seat></Seat>} />
            <Route path="/items" element={<ItemList></ItemList>} />
            <Route path="/items/detail" element={<ItemDetail></ItemDetail>} />
            <Route path="/orders" element={<OrderList></OrderList>} />
            <Route path="/posts" element={<PostList></PostList>} />
        </Routes>
    </div>
  );
}

export default App;
