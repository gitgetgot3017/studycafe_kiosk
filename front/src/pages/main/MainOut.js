import './MainOut.css';
import {useEffect, useState} from "react";
import axios from "axios";

function MainOut() {

    let [itemName, setItemName] = useState("");
    let [endDateTime, setEndDateTime] = useState("");
    let [itemType, setItemType] = useState("");
    let [ownSubscription, setOwnSubscription] = useState(false);
    let [subscriptions, setSubscriptions] = useState([]);

    useEffect(() => {
        axios.get("/subscriptions/representative")
            .then((result) => {
                setOwnSubscription(true);
                setItemName(result.data.itemName);
                setEndDateTime(result.data.endDateTime);
                setItemType(result.data.itemType);
            })
            .catch((error) => {
                setOwnSubscription(false);
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
                        {
                            ownSubscription ?
                            <a className="text-decoration-none" data-bs-toggle="modal"
                            data-bs-target="#subscriptionListModal"
                            onClick={() => {
                                axios.get("/subscriptions")
                                    .then((result) => {
                                        {
                                            setSubscriptions(result.data);
                                        }
                                    })
                                    .catch((error) => {
                                        console.error("이용권 조회 중 에러 발생:", error.response ? error.response.data : error.message);
                                        if (error.response) {
                                            console.error("에러 상태 코드:", error.response.status);
                                        }
                                    });
                            }}>이용권 변경</a> :
                            null
                        }
                    </div>
                    <p className="badge bg-primary mb-1">이용전</p>
                    <p className="text-muted mb-1">{itemName}</p>
                    <p className="mb-1">{endDateTime}</p>

                    <div className="buttons">
                        {
                            ownSubscription ?
                            <>
                                <a href="/seats" className="button" style={{textDecorationLine: "none"}}>좌석 선택</a>
                                <a href={"/items/detail?itemType=" + itemType} className="button" style={{textDecorationLine: "none"}}>연장하기</a>
                            </> :
                            null
                        }
                    </div>
                </div>
            </div>

            {/* 이용권 목록 모달 */}
            <div className="modal fade" id="subscriptionListModal" tabIndex="-1"
                 aria-labelledby="subscriptionListModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-body">
                            <p>
                                {
                                    subscriptions.map(function(subscription, i) {
                                        return (
                                            <div style={{background: "pink", marginBottom: "15px", cursor: "pointer"}} key={i}
                                                 onClick={() => {
                                                     let result = window.confirm("이용권을 변경하시겠습니까?");
                                                     if (!result) {
                                                         return;
                                                     }

                                                     axios.patch("/subscriptions", {
                                                         afterSubscriptionId: subscription.subscriptionId
                                                     })
                                                         .then(() => {
                                                             alert("이용권이 변경이 완료되었습니다.");
                                                         })
                                                         .catch((error) => {
                                                             console.error("이용권 변경 중 에러 발생:", error.response ? error.response.data : error.message);
                                                             if (error.response) {
                                                                 console.error("에러 상태 코드:", error.response.status);
                                                             }
                                                         });
                                                 }}>
                                                <div>이용권: {subscription.itemName}</div>
                                                <div>남은 시간: {subscription.leftTime}</div>
                                            </div>
                                        );
                                    })
                                }
                            </p>
                        </div>
                        <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default MainOut;