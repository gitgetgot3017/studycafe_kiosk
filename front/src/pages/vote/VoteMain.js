import './VoteMain.css';
import {useNavigate} from "react-router-dom";

function VoteMain() {

    let navigate = useNavigate();

    return (
        <div className="container">
            <h3>관리자 투표 페이지</h3>
            <div className="link-group">
                <a onClick={() => {navigate("/vote/register")}}>투표 등록하기</a>
                <a onClick={() => {navigate("/vote/result")}}>투표 결과 조회하기</a>
            </div>
        </div>
    );
}

export default VoteMain;