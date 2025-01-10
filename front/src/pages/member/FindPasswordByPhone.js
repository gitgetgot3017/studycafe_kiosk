import axios from "axios";
import {useState} from "react";
import {useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";
import {setUuid} from "../../store";

function FindPasswordByPhone() {

    let [phone, setPhone] = useState('');
    let [verificationCode, setVerificationCode] = useState('');
    let [verified, setVerified] = useState(false);
    let [phoneErrorMsg, setPhoneErrorMsg] = useState('');
    let [verificationErrorMsg, setVerificationErrorMsg] = useState('');

    let navigate = useNavigate();
    let dispatch = useDispatch();

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh"}}>
            <div className="phone-change-container bg-white p-5 rounded shadow-lg">
                <h2 className="text-center text-primary mb-4">휴대폰 번호로 찾기</h2>
                <form>
                    <div className="mb-3">
                        <div className="input-group">
                            <input type="text" id="phone-number" name="phone-number" className="form-control" placeholder="휴대폰 번호 입력" required onChange={(e) => {setPhone(e.target.value)}} />
                            <button type="button" className="btn btn-outline-primary" onClick={() => {
                                axios.post("/sms", {
                                        toPhoneNumber: phone
                                    }, {
                                        headers: { "Content-Type": "application/json" }
                                    })
                                    .then(() => {{
                                        alert("인증번호를 전송하였습니다. 5분 내에 입력해주세요.");
                                    }})
                                    .catch((error) => {
                                        console.error("인증 번호 전송 중 에러 발생:", error.response ? error.response.data : error.message);
                                        if (error.response) {
                                            console.error("에러 상태 코드:", error.response.status);
                                        }
                                        setPhoneErrorMsg(error.response.data.message);
                                    });
                            }}>인증번호 전송</button>
                            <div style={{color: "red"}}>{ phoneErrorMsg }</div>
                        </div>
                    </div>
                    <div className="mb-3">
                        <div className="input-group">
                            <input type="text" id="verification-code" name="verification-code" className="form-control" placeholder="인증번호 입력" required onChange={(e) => {setVerificationCode(e.target.value)}} />
                            <button type="button" className="btn btn-outline-primary" onClick={() => {
                                axios.post("/sms/verify", {
                                        phone: phone,
                                        verificationCode: verificationCode
                                    }, {
                                        headers: { "Content-Type": "application/json" }
                                    })
                                    .then(() => {{
                                        alert("인증에 성공하였습니다.");
                                        setVerificationErrorMsg('');
                                        setVerified(true);
                                    }})
                                    .catch((error) => {
                                        console.error("번호 인증 중 에러 발생:", error.response ? error.response.data : error.message);
                                        if (error.response) {
                                            console.error("에러 상태 코드:", error.response.status);
                                        }
                                        setVerificationErrorMsg(error.response.data.message);
                                    });
                            }}>인증</button>
                            <div style={{color: "red"}}>{ verificationErrorMsg }</div>
                        </div>
                    </div>
                    <button type="button" className="btn btn-primary w-100" onClick={() => {
                        if (!verified) {
                            alert("휴대폰 번호 인증을 완료해야 합니다!");
                            return;
                        }

                        axios.post("/members/find-password/phone", {
                                phone: phone,
                                verificationCode: verificationCode
                            }, {
                                headers: { "Content-Type": "application/json" }
                            })
                            .then((result) => {{
                                dispatch(setUuid(result.data.uuid));
                                navigate("/members/find-password/password");
                            }})
                            .catch((error) => {
                                console.error("비밀번호 변경(1) 중 에러 발생:", error.response ? error.response.data : error.message);
                                if (error.response) {
                                    console.error("에러 상태 코드:", error.response.status);
                                }
                            });
                    }}>다음</button>
                </form>
            </div>
        </div>
    );
}

export default FindPasswordByPhone;