function ChangeInfoPassword() {
    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh"}}>
            <div className="password-change-container bg-white p-5 rounded shadow-lg">
                <h2 className="text-center text-primary mb-4">비밀번호 변경</h2>
                <form action="/change-password" method="post">
                    <div className="mb-3">
                        <input type="password" id="current-password" name="current-password" className="form-control" placeholder="현재 비밀번호 입력" required/>
                    </div>
                    <div className="mb-3">
                        <input type="password" id="new-password" name="new-password" className="form-control" placeholder="새 비밀번호 입력" required/>
                    </div>
                    <button type="button" className="btn btn-primary w-100">변경하기</button>
                </form>
            </div>
        </div>
    );
}

export default ChangeInfoPassword;