import {useEffect, useState} from "react";
import axios from "axios";

function OrderList() {

    let [orderList, setOrderList] = useState([]);

    useEffect(() => {
        axios.get("/orders")
            .then((result) => {
                setOrderList(result.data);
            })
            .catch((error) => {
                console.error("주문 내역 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, [])

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh", backgroundColor: "#f8f9fa", color: "#495057"}}>
            <div className="payment-container w-50" style={{backgroundColor: "#ffffff", padding: "30px", borderRadius: "10px", boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)"}}>
                {/* 결제 내역 박스 (스크롤 가능한 영역) */}
                <div className="payment-box scroll-container" style={{backgroundColor: "#f1f3f5", borderRadius: "8px", padding: "20px", marginTop: "20px", maxHeight: "400px", overflowY: "auto"}}>
                    {
                        orderList.length === 0 ?
                        <div>주문 내역이 존재하지 않습니다!</div> :
                        orderList.map(function(order) {
                            return (
                                <div className="payment-item mb-3" style={{backgroundColor: "#ffffff", borderRadius: "8px", padding: "15px", boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)"}}>
                                    <div className="row">
                                        <div className="col-4" style={{fontWeight: "bold"}}>결제날짜</div>
                                        <div className="col-8">{order.orderDatetime}</div>
                                    </div>
                                    <div className="row">
                                        <div className="col-4" style={{fontWeight: "bold"}}>상품명</div>
                                        <div className="col-8">{order.itemName}</div>
                                    </div>
                                    <div className="row">
                                        <div className="col-4" style={{fontWeight: "bold"}}>결제금액</div>
                                        <div className="col-8">{order.orderPrice}원</div>
                                    </div>
                                </div>
                            );
                        })
                    }
                </div>
            </div>
        </div>
    );
}

export default OrderList;