import './VoteRegister.css';
import {useState} from "react";
import axios from "axios";
import {useNavigate} from "react-router-dom";

function VoteRegister() {

    let [voteTitle, setVoteTitle] = useState('');
    let [multiple, setMultiple] = useState(false);
    let [voteOptions, setVoteOptions] = useState(['']);
    let [errorMsg, setErrorMsg] = useState('');

    let navigate = useNavigate();

    return (
        <div className="container">
            <h3>투표 등록</h3>
            <div className="form-group">
                <label htmlFor="voteTitle">투표 제목</label>
                <input type="text" id="voteTitle" placeholder="투표 제목을 입력하세요" onChange={(e) => {setVoteTitle(e.target.value);}} />
            </div>
            <div className="form-group" id="optionList">
                {
                    voteOptions.map(function(voteOption, i) {
                        return (
                            <div className="option-group" key={i}>
                                <input type="text" name="option" placeholder="선택지" onChange={(e) => {
                                    let newVoteOptions = [...voteOptions];
                                    newVoteOptions[i] = e.target.value;
                                    setVoteOptions(newVoteOptions);
                                }} />
                                <button type="button" onClick={() => {
                                    let newVoteOptions = [...voteOptions];
                                    newVoteOptions.push('');
                                    setVoteOptions(newVoteOptions);
                                }}>+</button>
                            </div>
                        );
                    })
                }
            </div>
            <div className="option-group">
                <input type="checkbox" name="multiple" id="multiple" onChange={(e) => {
                    if (e.target.checked) {
                        setMultiple(true);
                    } else {
                        setMultiple(false);
                    }
                }}/>
                <label htmlFor="multiple">다중 선택 가능</label>
            </div>
            <div style={{color: "red"}}>{errorMsg}</div>
            <div className="actions">
                <button className="create-button" onClick={() => {
                    let result = window.confirm("투표를 생성하시겠습니까?");
                    if (!result) {
                        return;
                    }

                    axios.post("/votes/register", {
                            voteTitle: voteTitle,
                            multiple: multiple,
                            voteOptions: voteOptions
                        }, {
                            headers: { "Content-Type": "application/json" }
                        })
                        .then((result) => {
                            alert("투표가 등록되었습니다.");
                            navigate("/votes");
                        })
                        .catch((error) => {
                            console.error("투표 등록 중 에러 발생:", error.response ? error.response.data : error.message);
                            if (error.response) {
                                console.error("에러 상태 코드:", error.response.status);
                            }
                            setErrorMsg(error.response.data.message);
                        });
                }}>투표 생성하기</button>
            </div>
        </div>
    );
}

export default VoteRegister;