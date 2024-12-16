import './Vote.css';
import {useEffect, useState} from "react";
import axios from "axios";

function Vote() {

    let [voteTitleWithOptions, setVoteTitleWithOptions] = useState([]);
    let [voteOptions, setVoteOptions] = useState([[]]); // 2차원 배열, 하나의 투표 제목 당 하나의 1차원 배열을 가짐

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

    useEffect(() => {
        setVoteOptions(Array.from({ length: voteTitleWithOptions.length }, () => []));
    }, [voteTitleWithOptions]);

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
                                            vote.contents.map(function(option, j) {
                                                return (
                                                    <li key={option}>
                                                        <label>
                                                            <input type="checkbox" onChange={(e) => {
                                                                let newVoteOption = [...voteOptions[i]];
                                                                if (e.target.checked) {
                                                                    newVoteOption.push(vote.voteOptionIds[j]);
                                                                } else {
                                                                    newVoteOption = newVoteOption.filter((voteOptionId) => {return voteOptionId != vote.voteOptionIds[j]});
                                                                }

                                                                let newVoteOptions = [...voteOptions];
                                                                newVoteOptions[i] = newVoteOption;
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
                                                            <input type="radio" name="temperature" onClick={() => {
                                                                let newVoteOptions = [...voteOptions];
                                                                newVoteOptions[i] = [vote.voteOptionIds[j]]
                                                                setVoteOptions(newVoteOptions);
                                                            }}/>{option}
                                                        </label>
                                                    </li>
                                                );
                                            })
                                        }
                                    </ul>
                                    <button type="button" onClick={() => {
                                        if (voteOptions[i].length === 0) {
                                            alert("항목을 선택한 후 투표하기 버튼을 눌러주세요.");
                                            return;
                                        }

                                        axios.post("/votes", {
                                            voteTitleId: vote.voteTitleId,
                                            voteOptionIds: voteOptions[i]
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