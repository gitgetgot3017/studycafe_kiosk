import './Vote.css';
import {useEffect, useState} from "react";
import axios from "axios";

function Vote() {

    let [voteTitleWithOptions, setVoteTitleWithOptions] = useState([]);
    let [voteOptions, setVoteOptions] = useState([]);

    useEffect(() => {
        axios.get("/votes")
            .then((result) => {
                setVoteTitleWithOptions(result.data);
            })
            .catch((error) => {
                console.error("투표 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, []);

    return (
        <div>
            <div className="container">
                <h2>투표하기</h2>
                <div className="poll-list">
                    {
                        voteTitleWithOptions.map(function(vote, i) {
                            return (
                                <div className="poll" key={i}>
                                    <div className="poll-title">{vote.voteTitle}</div>
                                    <ul className="options">
                                        {
                                            vote.multiple ?
                                            vote.contents.map(function(option) {
                                                return (
                                                    <li key={option}>
                                                        <label>
                                                            <input type="checkbox" onChange={(e) => {
                                                                let newVoteOptions = [...voteOptions];
                                                                if (e.target.checked) {
                                                                    newVoteOptions.push(vote.voteOptionId[j]);
                                                                } else {
                                                                    newVoteOptions = newVoteOptions.filter((voteOptionId) => {return voteOptionId != vote.voteOptionId[j]});
                                                                }
                                                                setVoteOptions(newVoteOptions);
                                                            }}/>{option}
                                                        </label>
                                                    </li>
                                                );
                                            }) :
                                            vote.contents.map(function(option, j) {
                                                return (
                                                    <li key={option}>
                                                        <label>
                                                            <input type="radio" name="temperature" onClick={(e) => {
                                                                setVoteOptions([vote.voteOptionId[j]]);
                                                            }}/>{option}
                                                        </label>
                                                    </li>
                                                );
                                            })
                                        }
                                    </ul>
                                    <button type="button" onClick={() => {
                                        if (voteOptions.length === 0) {
                                            alert("항목을 선택한 후 투표하기 버튼을 눌러주세요.");
                                            return;
                                        }


                                        console.log(voteOptions);
                                        return;


                                        axios.post("/votes", {
                                            voteTitleId: vote.voteTitleId,
                                            voteOptionIds: voteOptions
                                            }, {
                                                headers: { "Content-Type": "application/json" }
                                            })
                                            .then(() => {{
                                                alert("투표 완료하였습니다!");
                                            }})
                                            .catch((error) => {
                                                console.error("투표 중 에러 발생:", error.response ? error.response.data : error.message);
                                                if (error.response) {
                                                    console.error("에러 상태 코드:", error.response.status);
                                                }
                                            });
                                    }}>투표하기</button>
                                </div>
                            );
                        })
                    }
                </div>
            </div>
        </div>
    );
}

export default Vote;