import {useEffect, useState} from "react";
import axios from "axios";

function MainIn() {

    let [subscriptions, setSubscriptions] = useState([]);
    let [beforeSubscriptionId, setBeforeSubscriptionId] = useState();

    function showSubscriptions() {

        axios.get("/subscriptions")
            .then((result) => {{
                setSubscriptions(result.data);
            }})
            .catch((error) => {
                console.error("이용권 조회 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }

    useEffect(() => {
        let representativeSubscription = subscriptions.find(subscription => subscription.representative);
        if (representativeSubscription) {
            setBeforeSubscriptionId(representativeSubscription.subscriptionId); // 조건을 만족하는 subscription의 id 저장
        }
    }, [subscriptions]);

    function changeSubscription(afterSubscriptionId) {

        let result = window.confirm("이용권을 변경하시겠습니까?");

        if (!result) {
            return;
        }

        axios.patch("/subscriptions", {
            beforeSubscriptionId: beforeSubscriptionId,
            afterSubscriptionId: afterSubscriptionId
        })
            .then(() => {
                alert("이용권이 변경되었습니다!");
            })
            .catch((error) => {
                console.error("이용권 변경 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }

    return (
        <div>
            {/* Main Info Section */}
            <div className="container mt-3">
                <div className="card shadow rounded-4 text-center p-4">
                    <div className="d-flex justify-content-between mb-3">
                        <span className="text-muted">이용 정보</span>
                        <p className="text-decoration-none" onClick={showSubscriptions} data-bs-toggle="modal" data-bs-target="#subscriptionListModal" style={{cursor: "pointer"}}>이용권 변경</p>
                    </div>
                    <h1 className="display-3 mb-3">11</h1>
                    <p className="badge bg-primary mb-1">이용중</p>
                    <p className="text-muted mb-1">기간권 8주일</p>
                    <p className="mb-1">2024. 12. 9. 오후 5:07:35 까지</p>
                    <p className="mt-3">입실코드 <strong>2073 | 06</strong></p>
                    <button className="btn btn-outline-primary mt-3">퇴실하기</button>
                </div>
            </div>

            {/* Navigation Options */}
            <div className="container mt-4">
                <div className="d-flex justify-content-between">
                    <a href="#" className="text-decoration-none">자리이동</a>
                    <a href="#" className="text-decoration-none">이용연장</a>
                </div>
            </div>

            {/* 약관 모달 */}
            <div className="modal fade" id="subscriptionListModal" tabindex="-1" aria-labelledby="subscriptionListModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-body">
                            <p>
                                {
                                    subscriptions.map(function(subscription) {
                                        return (
                                            <div style={{background: "skyblue", marginBottom: "15px", cursor: "pointer"}} onClick={() => changeSubscription(subscription.subscriptionId)}>
                                                <div>{subscription.itemName}</div>
                                                <div>{subscription.leftTime}</div>
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