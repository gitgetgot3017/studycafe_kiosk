import axios from 'axios';
import {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import {changeMemberGrade} from "../../store";
import {useNavigate} from "react-router-dom";

function Login() {

    let navigate = useNavigate();
    let dispatch = useDispatch();
    let state = useSelector((state) => {return state});

    let [phone, setPhone] = useState('');
    let [password, setPassword] = useState('');
    let [loginSuccess, setLoginSuccess] = useState("");

    function handleSubmit(e) {
        e.preventDefault(); // 폼의 기본 제출 방지

        let queryString = window.location.search;
        let params = new URLSearchParams(queryString);
        let redirectUrl = params.get("redirectUrl");
        if (redirectUrl === null) {
            redirectUrl = "/";
        }

        axios.post("/members/login?redirectUrl=" + redirectUrl, {
                phone: phone,
                password: password
            }, {
                headers: { "Content-Type": "application/json" }
            })
            .then((result) => {
                dispatch(changeMemberGrade(result.data.grade));
                navigate(result.data.redirectUrl);
            })
            .catch((error) => {
                console.error("로그인 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                    setLoginSuccess(error.response.data.message);
                }
            });
    };

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh"}}>
            <div className="login-container bg-white p-5 rounded shadow-lg">
                <h2 className="text-center text-primary mb-4">로그인</h2>
                <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                        <input type="tel" id="phone" name="phone" className="form-control" placeholder="휴대폰번호 (-없이입력)" required onChange={(e) => {setPhone(e.target.value)}} />
                    </div>
                    <div className="mb-3">
                        <input type="password" id="password" name="password" className="form-control" placeholder="비밀번호 (숫자 6자리)" required onChange={(e) => {setPassword(e.target.value)}} />
                    </div>
                    <div style={{color: "red"}}>{loginSuccess}</div>
                    <div className="mb-3 form-check">
                        <input type="checkbox" id="remember" name="remember" className="form-check-input" />
                        <label htmlFor="remember" className="form-check-label">로그인 상태 유지</label>
                    </div>
                    <div className="d-flex justify-content-between mb-4">
                        <a onClick={() => {navigate("/members/find-password/phone");}} style={{cursor: "pointer"}}>비밀번호 찾기</a>
                    </div>
                    <button type="submit" className="btn btn-primary w-100 mb-2">로그인</button>
                    <button type="button" className="btn btn-secondary w-100" onClick={() => {navigate("/members/join");}}>회원가입</button>
                </form>
            </div>
        </div>
    );
}

export default Login;