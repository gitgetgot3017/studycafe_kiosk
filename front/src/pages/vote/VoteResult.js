import './VoteResult.css';
import {useEffect, useState} from "react";
import axios from "axios";

function VoteResult() {

    let [votes, setVotes] = useState([]);

    useEffect(() => {
        axios.get("/votes/result")
            .then((result) => {
                setVotes(result.data);
            })
            .catch((error) => {
                console.error("투표 결과 조회 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, []);

    return (
        <div>
            <div className="container" id="voteResultsContainer">
                <h3>투표 결과 조회</h3>
                <div className="scroll-container">
                    {
                        votes.map(function(vote) {
                            return (
                                <div className="vote-topic" key={vote.voteTitle} style={{marginBottom: "20px"}}>
                                    <div className="vote-title">투표 주제: {vote.voteTitle}</div>
                                    <ul className="vote-options">
                                        {
                                            vote.voteOptionResultDtoList.map(function(voteOption) {
                                                return (
                                                    <li key={voteOption.content}>
                                                        선택지: {voteOption.content}
                                                        <span>투표 수: {voteOption.count}</span>
                                                    </li>
                                                );
                                            })
                                        }
                                    </ul>
                                </div>
                            );
                        })
                    }
                </div>
            </div>
        </div>
    );
}

export default VoteResult;