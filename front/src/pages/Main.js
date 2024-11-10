import {useEffect, useState} from "react";
import axios from "axios";

function Main() {

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

    function logout() {

        let result = window.confirm("로그아웃 하시겠습니까?");

        if (!result) {
            return;
        }

        axios.post("/members/logout")
            .then(() => {{
                alert("성공적으로 로그아웃 하였습니다.");
                window.location.href = "/members/login";
            }})
            .catch((error) => {
                console.error("로그아웃 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }

    return (
        <div className="bg-light">
            {/* Top Section with Logout Button */}
            <div className="container mt-4">
                <div className="d-flex justify-content-between align-items-center">
                    <h2 className="fw-bold">LHJ STUDYCAFE</h2>
                    <button className="btn btn-secondary btn-sm" onClick={logout}>로그아웃</button>
                </div>
            </div>

            {/* Location Selection and Options */}
            <div className="container mt-3 text-center">
                <button className="btn btn-outline-dark btn-sm">LHJ STUDYCAFE 대방점</button>
            </div>

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

            {/* Auto-login Switch and Ticket Purchase */}
            <div className="container mt-4 text-center">
                <div className="form-check form-switch d-flex justify-content-center align-items-center mb-3">
                    <input className="form-check-input" type="checkbox" id="autoLogin" />
                    <label className="form-check-label ms-2" htmlFor="autoLogin">로그인 시 좌석표 바로가기</label>
                </div>
                <button className="btn btn-dark w-100">이용권 구매</button>
            </div>

            {/* Bottom Navigation */}
            <nav className="navbar fixed-bottom navbar-light bg-light border-top">
                <div className="container d-flex justify-content-around">
                    <a href="#" className="text-center text-dark">
                        <i className="bi bi-house"></i>
                        <p className="small mb-0">지점정보</p>
                    </a>
                    <a href="#" className="text-center text-dark">
                        <i className="bi bi-book"></i>
                        <p className="small mb-0">스터디존</p>
                    </a>
                    <a href="#" className="text-center text-dark">
                        <i className="bi bi-journal"></i>
                        <p className="small mb-0">게시판</p>
                    </a>
                    <a href="#" className="text-center text-dark">
                        <i className="bi bi-person"></i>
                        <p className="small mb-0">투표</p>
                    </a>
                    <a href="#" className="text-center text-dark">
                        <i className="bi bi-list"></i>
                        <p className="small mb-0">메뉴</p>
                    </a>
                </div>
            </nav>

            {/* 약관 모달 */}
            <div class="modal fade" id="subscriptionListModal" tabindex="-1" aria-labelledby="subscriptionListModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-body">
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
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Main;