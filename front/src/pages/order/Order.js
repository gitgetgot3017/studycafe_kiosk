import './Order.css';
import {useSelector} from "react-redux";
import {useEffect, useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function Order() {

    let state = useSelector((state) => {return state});
    let navigate = useNavigate();
    let [coupons, setCoupons] = useState([]);
    let [totalPrice, setTotalPrice] = useState(state.orderItemPrice);
    let [couponId, setCouponId] = useState(null);

    useEffect(() => {
        axios.get("/coupons")
            .then((result) => {
                setCoupons(result.data);
            })
            .catch((error) => {
                console.error("주문 페이지에서 쿠폰 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, []);

    return (
        <div>
            <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh"}}>
                <div className="container">
                    <h1 className="text-center mb-4">이용권 구매</h1>
                    <div className="form-section">
                        <label htmlFor="productName">상품명</label>
                        <div id="productName" className="form-control" placeholder="Enter product name">{state.orderItemName}</div>
                    </div>
                    <div className="form-section">
                        <label htmlFor="productPrice">상품 가격</label>
                        <div id="productPrice" className="form-control" placeholder="Enter product price">{state.orderItemPrice}원</div>
                    </div>
                    <div className="form-section">
                        <label htmlFor="couponSelect">쿠폰 선택</label>
                        <select id="couponSelect" className="form-select" onChange={(e) => {
                            let selectedOption = e.target.value;

                            if (selectedOption === "쿠폰 없음") {
                                setTotalPrice(0);
                                return;
                            }

                            selectedOption = selectedOption.replace('RATE', '');
                            selectedOption === "HOUR1" ? // 일일권 1시간에 대해 가입 축하 쿠폰을 사용하는 경우
                            setTotalPrice(0) :
                            setTotalPrice(Number(state.orderItemPrice.replace(',', '')) * (100 - Number(selectedOption)) / 100);

                            let selectedCoupon = coupons.find((coupon) => coupon.couponType + coupon.rateOrHour === selectedOption);
                            if (selectedCoupon) {
                                setCouponId(selectedCoupon.couponId);
                            }
                        }}>
                            <option>쿠폰 없음</option>
                            {
                                coupons.map(function(coupon) {
                                    return (
                                        coupon.couponType + coupon.rateOrHour === "HOUR1" ?
                                        (state.orderItemName === "일일권 1시간" ? <option value={coupon.couponType + coupon.rateOrHour}>{coupon.name}</option> : null) :
                                        <option value={coupon.couponType + coupon.rateOrHour}>{coupon.name}</option>
                                    );
                                })
                            }
                        </select>
                    </div>
                    <div className="form-section">
                        <label>최종 주문 가격</label>
                        <div className="final-price">{totalPrice}원</div>
                    </div>
                    <button className="btn btn-primary w-100" onClick={() => {
                        axios.post("/orders", {
                                itemId: state.orderItemId,
                                couponId: couponId
                            }, {
                                headers: { "Content-Type": "application/json" }
                            })
                            .then(() => {{
                                alert("상품 주문을 완료하였습니다.");
                                navigate("/");
                            }})
                            .catch((error) => {
                                console.error("상품 주문 중 에러 발생:", error.response ? error.response.data : error.message);
                                if (error.response) {
                                    console.error("에러 상태 코드:", error.response.status);
                                }
                                alert();
                            });
                    }}>주문하기</button>
                </div>
            </div>
        </div>
    );
}

export default Order;