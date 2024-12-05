import {useEffect} from "react";
import {useDispatch, useSelector} from "react-redux";
import {getMemberInfo} from "../../store";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function ChangeInfoMain() {

    let navigate = useNavigate();
    let dispatch = useDispatch();
    let state = useSelector((state) => {return state});

    useEffect(() => {
        axios.get("/members")
            .then((result) => {
                dispatch(getMemberInfo(result.data));
            })
            .catch((error) => {
                console.error("나의 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, []);

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh"}}>
            <div className="profile-container bg-white p-5 rounded shadow-lg text-center">
                <h2 className="text-primary mb-3">안녕하세요, <span className="fw-bold">{state.memberInfo.name}</span>님</h2>
                <img src="https://via.placeholder.com/150" alt="사용자 이미지" className="img-fluid rounded-circle mb-4" style={{width: "150px", height: "150px"}}/>
                <div className="d-grid gap-3">
                    <button className="btn btn-outline-primary" onClick={() => {navigate("/members/info/general")}}>내 정보 변경</button>
                    <button className="btn btn-outline-primary" onClick={() => {navigate("/members/info/password")}}>비밀번호 변경</button>
                    <button className="btn btn-outline-primary" onClick={() => {navigate("/members/info/phone")}}>휴대폰번호 변경</button>
                </div>
            </div>
        </div>
    );
}

export default ChangeInfoMain;