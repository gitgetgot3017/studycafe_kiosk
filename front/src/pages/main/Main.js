import axios from "axios";
import MainIn from './MainIn';
import MainOut from './MainOut';
import {useDispatch, useSelector} from "react-redux";
import {useEffect} from "react";
import {changeUserInOut, changeLoginStatus} from "../../store";
import {useNavigate} from "react-router-dom";

function Main() {

    let dispatch = useDispatch();
    let state = useSelector((state) => {return state});
    let navigate = useNavigate();

    useEffect(() => {
       axios.get("/main")
           .then((result) => {
               if (result.data.mainInOut === true) {
                   dispatch(changeUserInOut(true));
               } else {
                   dispatch(changeUserInOut(false));
               }
               dispatch(changeLoginStatus(result.data.login));
            })
           .catch((error) => {
               console.error("입퇴실 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
               if (error.response) {
                   console.error("에러 상태 코드:", error.response.status);
               }
           });
    }, []);

    return (
        <div className="bg-light">
            {/* Top Section with Logout Button */}
            <div className="container mt-4">
                <div className="d-flex justify-content-between align-items-center">
                    <h2 className="fw-bold">LHJ STUDYCAFE</h2>
                    {
                        state.login ?
                        <button className="btn btn-secondary btn-sm" onClick={() => {
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
                        }>로그아웃</button> :
                        <>
                            <a href="/members/join" className="btn btn-secondary btn-sm">회원가입</a>
                            <a href="members/login" className="btn btn-secondary btn-sm">로그인</a>
                        </>
                    }
                </div>
            </div>

            {/* Location Selection and Options */}
            <div className="container mt-3 text-center">
                <button className="btn btn-outline-dark btn-sm">LHJ STUDYCAFE 대방점</button>
            </div>

            { state.userInOut ? <MainIn></MainIn> : <MainOut></MainOut> }

            {/* Auto-login Switch and Ticket Purchase */}
            <div className="container mt-4 text-center">
                <div className="form-check form-switch d-flex justify-content-center align-items-center mb-3">
                    <input className="form-check-input" type="checkbox" id="autoLogin" />
                    <label className="form-check-label ms-2" htmlFor="autoLogin">로그인 시 좌석표 바로가기</label>
                </div>
                <a onClick={() => {navigate("/items")}} className="btn btn-dark w-100">이용권 구매</a>
            </div>

            {/* Bottom Navigation */}
            <nav className="navbar fixed-bottom navbar-light bg-light border-top">
                <div className="container d-flex justify-content-around">
                    <a href="/seats" className="text-center text-dark" style={{textDecoration: "none"}}>
                        <i className="bi bi-house"></i>
                        <p className="small mb-0">스터디존</p>
                    </a>
                    <a href="/posts" className="text-center text-dark" style={{textDecoration: "none"}}>
                        <i className="bi bi-book"></i>
                        <p className="small mb-0">게시판</p>
                    </a>
                    {
                        state.login ?
                        <a href="/votes" className="text-center text-dark" style={{textDecoration: "none"}}>
                            <i className="bi bi-list"></i>
                            <p className="small mb-0">투표</p>
                        </a> :
                        null
                    }
                    {
                        state.memberGrade === "MEMBER" ?
                        <>
                            <a href="/orders" className="text-center text-dark" style={{textDecoration: "none"}}>
                                <i className="bi bi-journal"></i>
                                <p className="small mb-0">주문내역 조회</p>
                            </a>
                            <a href="/coupons" className="text-center text-dark" style={{textDecoration: "none"}}>
                                <i className="bi bi-ticket"></i>
                                <p className="small mb-0">쿠폰함</p>
                            </a>
                        </> :
                        null
                    }
                    {
                        state.memberGrade === "MANAGER" ?
                        <>
                            <a onClick={() => navigate("/items/detail?itemType=DAILY")} className="text-center text-dark" style={{textDecoration: "none"}}>
                                <i className="bi bi-box"></i>
                                <p className="small mb-0">상품 관리</p>
                            </a>
                        </> :
                        null
                    }
                    {
                        state.login ?
                        <a href="/members/info" className="text-center text-dark" style={{textDecoration: "none"}}>
                            <i className="bi bi-person"></i>
                            <p className="small mb-0">회원정보 변경</p>
                        </a> :
                        null
                    }
                </div>
            </nav>
        </div>
    );
}

export default Main;