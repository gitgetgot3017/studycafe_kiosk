import './App.css';
import { Routes, Route, Link } from 'react-router-dom';
import Main from './pages/main/Main';
import KioskEntrance from './pages/main/KioskEntrance';
import KioskLeave from './pages/main/KioskLeave';
import Login from './pages/member/Login';
import Join from './pages/member/Join';
import Seat from './pages/seat/Seat';
import ItemList from './pages/item/ItemList';
import ItemDetail from './pages/item/ItemDetail';
import ItemManage from "./pages/item/ItemManage";
import OrderList from './pages/order/OrderList';
import Order from './pages/order/Order';
import PostList from './pages/post/PostList';
import Vote from './pages/vote/VoteMain';
import Vote from './pages/vote/Vote';
import VoteResult from './pages/vote/VoteResult';
import CouponList from './pages/coupon/CouponList';
import ChangeInfoMain from './pages/member/ChangeInfoMain';
import ChangeInfoGeneral from './pages/member/ChangeInfoGeneral';
import ChangeInfoPhone from './pages/member/ChangeInfoPhone';
import ChangeInfoPassword from './pages/member/ChangeInfoPassword';

function App() {

    return (
    <div className="App">
        <Routes>
            <Route path="/" element={<Main></Main>} />
            <Route path="/main/entrance" element={<KioskEntrance></KioskEntrance>} />
            <Route path="/main/leave" element={<KioskLeave></KioskLeave>} />
            <Route path="/members/login" element={<Login></Login>} />
            <Route path="/members/join" element={<Join></Join>} />
            <Route path="/seats" element={<Seat></Seat>} />
            <Route path="/items" element={<ItemList></ItemList>} />
            <Route path="/items/detail" element={<ItemDetail></ItemDetail>} />
            <Route path="/items/manage" element={<ItemManage></ItemManage>} />
            <Route path="/orders" element={<OrderList></OrderList>} />
            <Route path="/orders/summary" element={<Order></Order>} />
            <Route path="/posts" element={<PostList></PostList>} />
            <Route path="/votes" element={<VoteMain></VoteMain>} />
            <Route path="/votes" element={<Vote></Vote>} />
            <Route path="/votes/result" element={<VoteResult></VoteResult>} />
            <Route path="/coupons" element={<CouponList></CouponList>} />
            <Route path="/members/info" element={<ChangeInfoMain></ChangeInfoMain>} />
            <Route path="/members/info/general" element={<ChangeInfoGeneral></ChangeInfoGeneral>} />
            <Route path="/members/info/phone" element={<ChangeInfoPhone></ChangeInfoPhone>} />
            <Route path="/members/info/password" element={<ChangeInfoPassword></ChangeInfoPassword>} />
        </Routes>
    </div>
  );
}

export default App;
