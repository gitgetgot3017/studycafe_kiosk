import './MainOut.css';
import {useEffect, useState} from "react";
import axios from "axios";

function MainOut() {

    let [itemName, setItemName] = useState("");
    let [endDateTime, setEndDateTime] = useState("");

    useEffect(() => {
        axios.get("/subscriptions/representative")
            .then((result) => {
                setItemName(result.data.itemName);
                setEndDateTime(result.data.endDateTime + " 까지");
            })
            .catch((error) => {
                console.error("메인 컴포넌트 - 유저 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                    setItemName(error.response.data.message);
                }
            });
    }, []);

    return (
        <div>
            {/* Main Info Section */}
            <div className="container mt-3">
                <div className="card shadow rounded-4 text-center p-4">
                    <div className="d-flex justify-content-between mb-3">
                        <span className="text-muted">이용 정보</span>
                        <a href="#" className="text-decoration-none">이용권 변경</a>
                    </div>
                    <p className="badge bg-primary mb-1">이용전</p>
                    <p className="text-muted mb-1">{itemName}</p>
                    <p className="mb-1">{endDateTime}</p>

                    <div className="buttons">
                        <a href="/seats" className="button" style={{textDecorationLine: "none"}}>좌석 선택</a>
                        <div className="button">연장하기</div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default MainOut;