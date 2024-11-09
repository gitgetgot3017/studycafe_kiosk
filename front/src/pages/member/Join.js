import axios from "axios";
import {useState} from "react";

function Join() {

    let [phone, setPhone] = useState('');
    let [password, setPassword] = useState('');
    let [optionalClause, setOptionalClause] = useState(false);
    let [errorMsg, setErrorMsg] = useState('');

    function handleSubmit(e) {
        e.preventDefault(); // 폼의 기본 제출 방지

        axios.post("/members/join", {
            phone: phone,
            password: password,
            optionalClause: optionalClause
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
                    setErrorMsg(error.response.data.message);
                }
            });
    };

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh"}}>
            <div className="signup-container bg-white p-5 rounded shadow-lg">
                <h2 className="text-center text-primary mb-4">회원가입</h2>
                <form onSubmit={handleSubmit}>
                    { /* 휴대폰 번호 입력 및 인증번호 전송 버튼 */ }
                    <div className="mb-3">
                        <div className="input-group">
                            <input type="tel" id="phone" name="phone" className="form-control" placeholder="휴대폰번호 (-없이입력)" required onChange={(e) => {setPhone(e.target.value)}} />
                            <button type="button" className="btn btn-outline-primary">인증번호 전송</button>
                        </div>
                    </div>
                    { /* 인증번호 입력 및 인증 버튼 */ }
                    <div className="mb-3">
                        <div className="input-group">
                            <input type="text" id="auth-code" name="auth-code" className="form-control" placeholder="인증번호 입력" required />
                            <button type="button" className="btn btn-outline-primary">인증</button>
                        </div>
                    </div>
                    { /* 비밀번호 입력 필드 */ }
                    <div className="mb-3">
                        <div className="input-group">
                            <input type="tel" id="phone" name="phone" className="form-control" placeholder="비밀번호 (숫자 6자리)" required onChange={(e) => {setPassword(e.target.value)}} />
                        </div>
                    </div>
                    { /* 비밀번호 재입력 필드 */ }
                    <div className="mb-3">
                        <div className="input-group">
                            <input type="tel" id="phone" name="phone" className="form-control" placeholder="비밀번호 확인 (숫자 6자리)" required />
                        </div>
                    </div>
                    <p style={{color: "red"}}>{ errorMsg }</p>
                    { /* 약관 동의 및 약관 보기 모달 */ }
                    <div className="mb-3">
                        <div className="form-check">
                            <input type="checkbox" id="agree-all" className="form-check-input" />
                            <label htmlFor="agree-all" className="form-check-label fw-bold">전체 동의</label>
                        </div>
                        <div className="form-check">
                            <input type="checkbox" id="agree-terms" name="agree-terms" className="form-check-input" required />
                            <label htmlFor="agree-terms" className="form-check-label">이용 약관에 동의합니다.</label>
                            <button type="button" className="btn btn-link p-0 ms-2" data-bs-toggle="modal" data-bs-target="#termsModal">약관 보기</button>
                        </div>
                        <div className="form-check">
                            <input type="checkbox" id="agree-privacy" name="agree-privacy" className="form-check-input" required />
                            <label htmlFor="agree-privacy" className="form-check-label">개인정보 처리방침에 동의합니다.</label>
                            <button type="button" className="btn btn-link p-0 ms-2" data-bs-toggle="modal"
                                    data-bs-target="#privacyModal">약관 보기
                            </button>
                        </div>
                        <div className="form-check">
                            <input type="checkbox" id="agree-marketing" name="agree-marketing" className="form-check-input" onClick={(e) => {setOptionalClause(e.target.checked)}} />
                            <label htmlFor="agree-marketing" className="form-check-label">마케팅 정보 수신에 동의합니다. (선택)</label>
                            <button type="button" className="btn btn-link p-0 ms-2" data-bs-toggle="modal" data-bs-target="#privacyModal">약관 보기</button>
                        </div>
                    </div>
                    { /* 회원가입 버튼 */ }
                    <button type="submit" className="btn btn-primary w-100">회원가입</button>
                </form>
            </div>

            { /* 약관 모달 */ }
            <div className="modal fade" id="termsModal" tabIndex="-1" aria-labelledby="termsModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="termsModalLabel">이용 약관</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <p>여기에 이용 약관의 상세 내용을 입력하세요...</p>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                        </div>
                    </div>
                </div>
            </div>

            <div className="modal fade" id="privacyModal" tabIndex="-1" aria-labelledby="privacyModalLabel"
                 aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="privacyModalLabel">개인정보 처리방침</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <p>여기에 개인정보 처리방침의 상세 내용을 입력하세요...</p>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Join;