import './Order.css';
import {useSelector} from "react-redux";

function Order() {

    let state = useSelector((state) => {return state});

    return (
        <div>
            <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh"}}>
                <div className="container">
                    <h1 className="text-center mb-4">이용권 구매</h1>
                    <div className="form-section">
                        <label htmlFor="productName">상품명</label>
                        <div id="productName" className="form-control" placeholder="Enter product name">{state.orderItem}</div>
                    </div>
                    <div className="form-section">
                        <label htmlFor="productPrice">상품 가격</label>
                        <div id="productPrice" className="form-control" placeholder="Enter product price">{state.orderItemPrice}원</div>
                    </div>
                    <div className="form-section">
                        <label htmlFor="couponSelect">쿠폰 선택</label>
                        <select id="couponSelect" className="form-select">
                            <option value="0">쿠폰 없음</option>
                            <option value="10">10% 할인</option>
                            <option value="20">20% 할인</option>
                            <option value="30">30% 할인</option>
                        </select>
                    </div>
                    <div className="form-section">
                        <label>최종 주문 가격</label>
                        <div className="final-price">0원</div>
                    </div>
                    <button className="btn btn-primary w-100">주문하기</button>
                </div>
            </div>
        </div>
    );
}

export default Order;