import axios from 'axios';
import {useState, useEffect} from "react";

function Login() {

    let [phone, setPhone] = useState('');
    let [password, setPassword] = useState('');
    let [loginSuccess, setLoginSuccess] = useState(true);

    function handleSubmit(e) {
        e.preventDefault(); // 폼의 기본 제출 방지

        axios.post("/members/login", {
            phone: phone,
            password: password
        }, {
            headers: { "Content-Type": "application/json" }
        })
            .then(() => {
                window.location.href = "/"; // 로그인 성공 시 메인 페이지로 이동
            })
            .catch((error) => {
                console.error("로그인 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                    setLoginSuccess(false);
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
                    { !loginSuccess ? <div style={{color: "red"}}>해당 회원은 존재하지 않습니다.</div> : null }
                    <div className="mb-3 form-check">
                        <input type="checkbox" id="remember" name="remember" className="form-check-input" />
                        <label htmlFor="remember" className="form-check-label">로그인 상태 유지</label>
                    </div>
                    <div className="d-flex justify-content-between mb-4">
                        <a href="/find-password" className="link-primary">비밀번호 찾기</a>
                    </div>
                    <button type="submit" className="btn btn-primary w-100 mb-2">로그인</button>
                    <button type="button" className="btn btn-secondary w-100" onClick="window.location.href='/signup'">회원가입</button>
                </form>
            </div>
        </div>
    );
}

export default Login;