import axios from "axios";
import {useSelector} from "react-redux";
import {useState} from "react";

function FindPasswordMain() {

    let [password, setPassword] = useState('');
    let [pwdMismatchErrorMsg, setPwdMismatchErrorMsg] = useState('');

    let state = useSelector((state) => {return state});

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh"}}>
            <div className="password-change-container bg-white p-5 rounded shadow-lg">
                <h2 className="text-center text-primary mb-4">새 비밀번호 입력</h2>
                <form action="/change-password" method="post">
                    <div className="mb-3">
                        <input type="password" id="current-password" name="current-password" className="form-control" placeholder="변경할 비밀번호를 입력해주세요" required onChange={(e) => {setPassword(e.target.value)}} />
                    </div>
                    <div className="mb-3">
                        <input type="password" id="new-password" name="new-password" className="form-control" placeholder="변경할 비밀번호를 한번 더 입력해주세요" required onChange={(e) => {
                            if (e.target.value !== password) {
                                setPwdMismatchErrorMsg("비밀번호가 일치하지 않습니다.");
                            } else {
                                setPwdMismatchErrorMsg("");
                            }
                        }} />
                    </div>
                    <div style={{color: "red"}}>{ pwdMismatchErrorMsg }</div>
                    <button type="button" className="btn btn-primary w-100" onClick={() => {

                        axios.post("/members/find-password/password", {
                                uuid: state.uuid,
                                password: password
                            }, {
                                headers: { "Content-Type": "application/json" }
                            })
                            .then(() => {{
                                alert("비밀번호가 변경되었습니다!");
                            }})
                            .catch((error) => {
                                console.error("비밀번호 변경(2) 중 에러 발생:", error.response ? error.response.data : error.message);
                                if (error.response) {
                                    console.error("에러 상태 코드:", error.response.status);
                                }
                            });
                    }}>변경하기</button>
                </form>
            </div>
        </div>
    );
}

export default FindPasswordMain;