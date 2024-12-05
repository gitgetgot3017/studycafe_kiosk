function ChangeInfoGeneral() {

    let years = Array.from({length: 100}, (_, i) => 1950 + i);
    let months = Array.from({length: 12}, (_, i) => 1 + i);
    let days = Array.from({length: 31}, (_, i) => 1 + i);

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh"}}>
            <div className="info-update-container bg-white p-5 rounded shadow-lg">
                <h2 className="text-center text-primary mb-4">내 정보 변경</h2>
                <form>
                    <div className="mb-3">
                        <label htmlFor="name" className="form-label">이름</label>
                        <input type="text" id="name" name="name" className="form-control" placeholder="이름 입력" required/>
                    </div>
                    <div className="mb-3">
                        <label className="form-label">성별</label>
                        <div>
                            <div className="form-check form-check-inline">
                                <input className="form-check-input" type="radio" name="gender" id="male" value="male" required/>
                                <label className="form-check-label" htmlFor="male">남성</label>
                            </div>
                            <div className="form-check form-check-inline">
                                <input className="form-check-input" type="radio" name="gender" id="female" value="female"/>
                                <label className="form-check-label" htmlFor="female">여성</label>
                            </div>
                        </div>
                    </div>
                    <div className="mb-3">
                        <label className="form-label">생일</label>
                        <div className="d-flex gap-2">
                            <select className="form-select" name="birth-year" required>
                                <option value="" disabled selected>년</option>
                                {
                                    years.map(function(year) {
                                        return (
                                            <option value={year} key={year}>{year}년</option>
                                        );
                                    })
                                }
                            </select>
                            <select className="form-select" name="birth-month" required>
                                <option value="" disabled selected>월</option>
                                {
                                    months.map(function(month) {
                                        return (
                                            <option value={month} key={month}>{month}월</option>
                                        );
                                    })
                                }
                            </select>
                            <select className="form-select" name="birth-day" required>
                                <option value="" disabled selected>일</option>
                                {
                                    days.map(function(day) {
                                        return (
                                            <option value={day} key={day}>{day}일</option>
                                        );
                                    })
                                }
                            </select>
                        </div>
                    </div>
                    <button type="button" className="btn btn-primary w-100">변경하기</button>
                </form>
            </div>
        </div>
    );
}

export default ChangeInfoGeneral;