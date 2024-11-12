import axios from "axios";
import MainIn from './MainIn';
import MainOut from './MainOut';

function Main(props) {

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

            { props.userInOut ? <MainIn></MainIn> : <MainOut></MainOut> }

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
        </div>
    );
}

export default Main;