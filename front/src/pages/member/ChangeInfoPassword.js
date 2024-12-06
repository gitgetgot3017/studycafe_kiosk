import axios from "axios";
import {useState} from "react";
import {useNavigate} from "react-router-dom";

function ChangeInfoPassword() {

    let [curPassword, setCurPassword] = useState("");
    let [newPassword, setNewPassword] = useState("");
    let [errorMsg, setErrorMsg] = useState("");

    let navigate = useNavigate();

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh"}}>
            <div className="password-change-container bg-white p-5 rounded shadow-lg">
                <h2 className="text-center text-primary mb-4">비밀번호 변경</h2>
                <form action="/change-password" method="post">
                    <div className="mb-3">
                        <input type="password" onChange={(e) => {setCurPassword(e.target.value);}} id="current-password" name="current-password" className="form-control" placeholder="현재 비밀번호 입력" required/>
                    </div>
                    <div className="mb-3">
                        <input type="password" onChange={(e) => {setNewPassword(e.target.value);}} id="new-password" name="new-password" className="form-control" placeholder="새 비밀번호 입력" required/>
                    </div>
                    <div style={{color: "red"}}>{errorMsg}</div>
                    <button type="button" className="btn btn-primary w-100" onClick={() => {
                        axios.patch("/members?type=password", {
                            curPassword: curPassword,
                            newPassword: newPassword
                            })
                            .then(() => {{
                                alert("비밀번호가 변경되었습니다!");
                                navigate("/");
                            }})
                            .catch((error) => {
                                console.error("비밀번호 변경 중 에러 발생:", error.response ? error.response.data : error.message);
                                if (error.response) {
                                    console.error("에러 상태 코드:", error.response.status);
                                    setErrorMsg(error.response.data.message);
                                }
                            });
                    }}>변경하기</button>
                </form>
            </div>
        </div>
    );
}

export default ChangeInfoPassword;