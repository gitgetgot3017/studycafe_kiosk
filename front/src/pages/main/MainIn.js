import {useEffect, useState} from "react";
import axios from "axios";
import {useDispatch} from "react-redux";
import {changeUserInOut} from "../../store";

function MainIn() {

    let [subscriptions, setSubscriptions] = useState([]);
    let [mySeatNum, setMySeatNum] = useState(0);
    let [myEntranceCode, setMyEntranceCode] = useState("");
    let [itemName, setItemName] = useState("");
    let [endDateTime, setEndDateTime] = useState("");
    let [itemType, setItemType] = useState("");

    let dispatch = useDispatch();

    useEffect(() => {

        axios.get("/seats/me")
            .then((result) => {
                setMySeatNum(result.data.id);
                setMyEntranceCode(result.data.entranceCode);
            })
            .catch((error) => {
                console.error("나의 좌석 번호 및 입실 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                    setItemName(error.response.data.message);
                }
            });

        axios.get("/subscriptions/representative")
            .then((result) => {{
                setItemName(result.data.itemName);
                setEndDateTime(result.data.endDateTime);
                setItemType(result.data.itemType);
            }})
            .catch((error) => {
                console.error("대표 이용권 조회 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
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
                        <p className="text-decoration-none" data-bs-toggle="modal" data-bs-target="#subscriptionListModal"
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
                                   })
                           }}>이용권 변경</p>
                    </div>
                    <h1 className="display-3 mb-3">{mySeatNum}</h1>
                    <p className="badge bg-primary mb-1">이용중</p>
                    <p className="text-muted mb-1">{itemName}</p>
                    <p className="mb-1">{endDateTime}</p>
                    <p className="mt-3">입실코드 <strong>{myEntranceCode}</strong></p>
                    <button className="btn btn-outline-primary mt-3" onClick={() => {
                        let result = window.confirm("퇴실 처리하시겠습니까?");
                        if (!result) {
                            return;
                        }

                        axios.patch("/seats/" + mySeatNum)
                            .then(() => {
                                alert("퇴실 처리가 완료되었습니다.");
                                dispatch(changeUserInOut(false));
                            })
                            .catch((error) => {
                                console.error("퇴실 처리 중 에러 발생:", error.response ? error.response.data : error.message);
                                if (error.response) {
                                    console.error("에러 상태 코드:", error.response.status);
                                }
                            });
                    }}>퇴실하기</button>
                </div>
            </div>

            {/* Navigation Options */}
            <div className="container mt-4">
                <div className="d-flex justify-content-between">
                    <a href="/seats" className="text-decoration-none">자리이동</a>
                    <a href={"/items/detail?itemType=" + itemType} className="text-decoration-none">이용연장</a>
                </div>
            </div>

            {/* 약관 모달 */}
            <div className="modal fade" id="subscriptionListModal" tabindex="-1" aria-labelledby="subscriptionListModalLabel" aria-hidden="true">
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

export default MainIn;