import './KioskEntrance.css';
import {useState} from "react";
import axios from "axios";

function KioskEntrance() {

    let [choose, setChoose] = useState(true); // true: 휴대폰 번호 필드 선택, false: 비밀번호 필드 선택
    let [phone, setPhone] = useState("");
    let [password, setPassword] = useState("");

    function expressNum(i) {
        if (choose && phone.length < 4) {
            setPhone(phone + i);
        } else {
            if (password.length < 2) {
                setPassword(password + i);
            }
        }
    }

    return (
        <div>
            <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh"}}>
                <div className="container">
                    <h3 className="mb-4 text-center">스터디카페 입실</h3>
                    <div className="input-box">
                        <label htmlFor="phoneLastDigits" className="form-label">휴대폰 번호 (뒷자리 4자리)</label>
                        <input type="text" onClick={() => {setChoose(true)}} value={phone} className="form-control text-center" />
                    </div>
                    <div className="input-box">
                        <label htmlFor="passwordFirstDigits" className="form-label">비밀번호 (앞자리 2자리)</label>
                        <input type="text" onClick={() => {setChoose(false)}} value={password} className="form-control text-center" />
                    </div>
                    <div className="keypad">
                        <button className="btn btn-outline-secondary" onClick={() => {expressNum(1)}}>1</button>
                        <button className="btn btn-outline-secondary" onClick={() => {expressNum(2)}}>2</button>
                        <button className="btn btn-outline-secondary" onClick={() => {expressNum(3)}}>3</button>
                        <button className="btn btn-outline-secondary" onClick={() => {expressNum(4)}}>4</button>
                        <button className="btn btn-outline-secondary" onClick={() => {expressNum(5)}}>5</button>
                        <button className="btn btn-outline-secondary" onClick={() => {expressNum(6)}}>6</button>
                        <button className="btn btn-outline-secondary" onClick={() => {expressNum(7)}}>7</button>
                        <button className="btn btn-outline-secondary" onClick={() => {expressNum(8)}}>8</button>
                        <button className="btn btn-outline-secondary" onClick={() => {expressNum(9)}}>9</button>
                        <button className="btn btn-outline-danger btn-clear" onClick={() => {setPhone(""); setPassword("");}}>clear all</button>
                        <button className="btn btn-outline-secondary" onClick={() => {expressNum(0)}}>0</button>
                        <button className="btn btn-outline-primary btn-delete" onClick={() => {choose ? setPhone(phone.substring(0, phone.length-1)) : setPassword(password.substring(0, password.length-1))}}>←</button>
                    </div>
                    <button className="btn btn-primary w-100 mt-4" onClick={() => {
                        axios.post("/main/entrance", {
                                phone: phone,
                                password: password
                            }, {
                                headers: { "Content-Type": "application/json" }
                            })
                            .then(() => {{
                                window.location.href = "/main/entrance";
                            }})
                            .catch((error) => {
                                alert(error.response.data.message);
                                console.error("입실 중 에러 발생:", error.response ? error.response.data : error.message);
                                if (error.response) {
                                    console.error("에러 상태 코드:", error.response.status);
                                }
                            });
                    }}>입실하기</button>
                </div>
            </div>
        </div>
    );
}

export default KioskEntrance;