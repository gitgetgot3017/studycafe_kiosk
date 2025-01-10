import './App.css';
import { Routes, Route, Link } from 'react-router-dom';
import Main from './pages/main/Main';
import KioskEntrance from './pages/main/KioskEntrance';
import KioskLeave from './pages/main/KioskLeave';
import Login from './pages/member/Login';
import Join from './pages/member/Join';
import FindPasswordByPhone from "./pages/member/FindPasswordByPhone";
import FindPasswordMain from "./pages/member/FindPasswordMain";
import ChangeInfoMain from './pages/member/ChangeInfoMain';
import ChangeInfoGeneral from './pages/member/ChangeInfoGeneral';
import ChangeInfoPhone from './pages/member/ChangeInfoPhone';
import ChangeInfoPassword from './pages/member/ChangeInfoPassword';
import Seat from './pages/seat/Seat';
import ItemList from './pages/item/ItemList';
import ItemDetail from './pages/item/ItemDetail';
import ItemManage from "./pages/item/ItemManage";
import OrderList from './pages/order/OrderList';
import Order from './pages/order/Order';
import PostList from './pages/post/PostList';
import Vote from './pages/vote/Vote';
import VoteMain from './pages/vote/VoteMain';
import VoteResult from './pages/vote/VoteResult';
import VoteRegister from './pages/vote/VoteRegister';
import CouponList from './pages/coupon/CouponList';

function App() {

    return (
    <div className="App">
        <Routes>
            <Route path="/" element={<Main></Main>} />
            <Route path="/main/entrance" element={<KioskEntrance></KioskEntrance>} />
            <Route path="/main/leave" element={<KioskLeave></KioskLeave>} />
            <Route path="/members/login" element={<Login></Login>} />
            <Route path="/members/join" element={<Join></Join>} />
            <Route path="/members/find-password/phone" element={<FindPasswordByPhone></FindPasswordByPhone>} />
            <Route path="/members/find-password/password" element={<FindPasswordMain></FindPasswordMain>} />
            <Route path="/members/info" element={<ChangeInfoMain></ChangeInfoMain>} />
            <Route path="/members/info/general" element={<ChangeInfoGeneral></ChangeInfoGeneral>} />
            <Route path="/members/info/phone" element={<ChangeInfoPhone></ChangeInfoPhone>} />
            <Route path="/members/info/password" element={<ChangeInfoPassword></ChangeInfoPassword>} />
            <Route path="/seats" element={<Seat></Seat>} />
            <Route path="/items" element={<ItemList></ItemList>} />
            <Route path="/items/detail" element={<ItemDetail></ItemDetail>} />
            <Route path="/items/manage" element={<ItemManage></ItemManage>} />
            <Route path="/orders" element={<OrderList></OrderList>} />
            <Route path="/orders/summary" element={<Order></Order>} />
            <Route path="/posts" element={<PostList></PostList>} />
            <Route path="/votes" element={<Vote></Vote>} />
            <Route path="/votes/main" element={<VoteMain></VoteMain>} />
            <Route path="/votes/result" element={<VoteResult></VoteResult>} />
            <Route path="/votes/register" element={<VoteRegister></VoteRegister>} />
            <Route path="/coupons" element={<CouponList></CouponList>} />
        </Routes>
    </div>
  );
}

export default App;
