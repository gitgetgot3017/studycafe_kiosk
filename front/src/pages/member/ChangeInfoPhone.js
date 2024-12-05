function ChangeInfoPhone() {
    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh"}}>
            <div className="phone-change-container bg-white p-5 rounded shadow-lg">
                <h2 className="text-center text-primary mb-4">변경할 휴대폰번호 입력</h2>
                <form>
                    <div className="mb-3">
                        <div className="input-group">
                            <input type="text" id="phone-number" name="phone-number" className="form-control" placeholder="휴대폰 번호 입력" required/>
                            <button type="button" className="btn btn-outline-primary">인증번호 전송</button>
                        </div>
                    </div>
                    <div className="mb-3">
                        <div className="input-group">
                            <input type="text" id="verification-code" name="verification-code" className="form-control" placeholder="인증번호 입력" required/>
                            <button type="button" className="btn btn-outline-primary">인증</button>
                        </div>
                    </div>
                    <button type="button" className="btn btn-primary w-100">변경하기</button>
                </form>
            </div>
        </div>
    );
}

export default ChangeInfoPhone;