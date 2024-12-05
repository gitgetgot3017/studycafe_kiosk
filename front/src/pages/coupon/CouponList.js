import {useEffect, useState} from "react";
import axios from "axios";

function CouponList() {

    let [couponList, setCouponList] = useState([]);

    useEffect(() => {
        axios.get("/coupons")
            .then((result) => {
                setCouponList(result.data);
            })
            .catch((error) => {
                console.error("쿠폰 목록 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, [])

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh", backgroundColor: "#f8f9fa", color: "#495057"}}>
            <div className="coupon-container w-50" style={{backgroundColor: "#ffffff", padding: "30px", borderRadius: "10px", boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)"}}>
                {/* 쿠폰함 제목 */}
                <h2 className="text-center text-primary mb-4">나의 쿠폰함</h2>

                {/* 쿠폰 박스 (스크롤 가능한 영역) */}
                <div className="coupon-box scroll-container" style={{backgroundColor: "#f1f3f5", borderRadius: "8px", padding: "20px", marginTop: "20px", maxHeight: "400px", overflowY: "auto"}}>
                    {
                        couponList.length === 0 ?
                        <div>사용 가능한 쿠폰이 존재하지 않습니다!</div> :
                        couponList.map(function(coupon) {
                            return (
                                <div className="coupon-item mb-3" style={{backgroundColor: "#ffffff", borderRadius: "8px", padding: "15px", boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)"}}>
                                    <div className="row">
                                        <div className="col-4" style={{fontWeight: "bold"}}>쿠폰명</div>
                                        <div className="col-8">{coupon.name}</div>
                                    </div>
                                    <div className="row">
                                        <div className="col-4" style={{fontWeight: "bold"}}>발급일</div>
                                        <div className="col-8">{coupon.issueDatetime}</div>
                                    </div>
                                    <div className="row">
                                        <div className="col-4" style={{fontWeight: "bold"}}>만료일</div>
                                        <div className="col-8">{coupon.endDatetime}</div>
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

export default CouponList;