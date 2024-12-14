import './Vote.css';
import {useEffect, useState} from "react";
import axios from "axios";

function Poll() {

    let [voteTitleWithOptions, setVoteTitleWithOptions] = useState([]);

    useEffect(() => {
        axios.get("/votes")
            .then((result) => {
                console.log(result.data);
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
                                                            <input type="checkbox" name="snack" value="과자"/>{option}
                                                        </label>
                                                    </li>
                                                );
                                            }) :
                                            vote.contents.map(function(option) {
                                                return (
                                                    <li key={option}>
                                                        <label>
                                                            <input type="radio" name="weather" value="더워요"/>{option}
                                                        </label>
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
                <button type="button">투표하기</button>
            </div>
        </div>
    );
}

export default Poll;