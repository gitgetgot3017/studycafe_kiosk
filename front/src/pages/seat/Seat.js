import './Seat.css';
import {useState, useEffect} from "react";
import {useNavigate} from "react-router-dom";
import axios from "axios";
import {useSelector} from "react-redux";

function Seat() {

    let navigate = useNavigate();
    let [seats, setSeats] = useState([null, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""]);
    let state = useSelector((state) => {return state});

    useEffect(() => {
        axios.get("/seats/occupied")
            .then((result) => {
                let copy = [...seats];
                for (let i=0; i<result.data.length; i++) {
                    if (result.data[i].mySeat) {
                        copy[result.data[i].seatId] = "my-seat";
                    } else {
                        copy[result.data[i].seatId] = "occupied";
                    }
                }
                setSeats(copy);
            })
            .catch((error) => {
                console.error("좌석 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, []);

    function chooseSeat(seatId) {
        if (seats[seatId] === "occupied") {
            alert("이미 사용 중인 좌석입니다.");
            return;
        }

        if (seats[seatId] === "my-seat") {
            alert("이미 나의 좌석입니다.");
            return;
        }

        let flag = false;
        if (seats.find((seat) => {
            if (seat === "my-seat") {
                flag = true;
            }
        }));

        if (flag) { // 좌석을 변경하는 경우
            let result = window.confirm("해당 좌석으로 변경하시겠습니까?");
            if (!result) {
                return;
            }

            axios.patch("/seats", {afterSeatId: seatId})
                .then(() => {{
                    navigate("/");
                }})
                .catch((error) => {
                    console.error("좌석 변경 중 에러 발생:", error.response ? error.response.data : error.message);
                    if (error.response) {
                        console.error("에러 상태 코드:", error.response.status);
                    }
                });
        } else { // 좌석을 선택하는 경우

            let result = window.confirm("해당 좌석을 선택하시겠습니까?");
            if (!result) {
                return;
            }

            if (state.memberGrade === "GUEST") { // 로그인을 하지 않은 경우에 대한 처리
                alert("로그인을 하셔야 좌석 선택이 가능합니다.");
                navigate("/members/login?redirectUrl=/seats");
                return;
            }

            axios.post("/seats/" + seatId)
                .then(() => {{
                    navigate("/");
                }})
                .catch((error) => {
                    console.error("좌석 선택 중 에러 발생:", error.response ? error.response.data : error.message);
                    if (error.response) {
                        console.error("에러 상태 코드:", error.response.status);
                    }
                    alert(error.response.data.message); // 보유 중인 이용권이 없는 경우에 대한 처리
                    navigate("/items");
                });
        }
    }

    return (
        <div className="bg-dark text-white">
            <div className="container seat-map-container">
                <div className="legend">
                    <div className="legend-item">
                        <div className="legend-color" style={{backgroundColor: "#66ccff"}}></div>
                        <span>내 좌석</span>
                    </div>
                    <div className="legend-item">
                        <div className="legend-color" style={{backgroundColor: "#6c757d"}}></div>
                        <span>사용가능</span>
                    </div>
                    <div className="legend-item">
                        <div className="legend-color" style={{backgroundColor: "orange"}}></div>
                        <span>사용중</span>
                    </div>
                    {/*<div className="legend-item">*/}
                    {/*    <div className="legend-color" style={{backgroundColor: "#70d36a"}}></div>*/}
                    {/*    <span>고정석</span>*/}
                    {/*</div>*/}
                    {/*<div className="legend-item">*/}
                    {/*    <div className="legend-color" style={{backgroundColor: "#ff4d4d"}}></div>*/}
                    {/*    <span>퇴실임박</span>*/}
                    {/*</div>*/}
                    {/*<div className="legend-item">*/}
                    {/*    <div className="legend-color" style={{backgroundColor: "#d3ad70"}}></div>*/}
                    {/*    <span>일일권</span>*/}
                    {/*</div>*/}
                    {/*<div className="legend-item">*/}
                    {/*    <div className="legend-color" style={{backgroundColor: "#66b3ff"}}></div>*/}
                    {/*    <span>기간권</span>*/}
                    {/*</div>*/}
                </div>

                <div className="d-flex">
                    {/* Rooms 2 and 3 (Left Side) */}
                    <div className="flex-grow-1">
                        <div className="d-flex justify-content-between container">
                            {/* Room 2 */}
                            <div className="room">
                                <div className="horizontal-row">
                                    {
                                        [1,2,3,4,5,6,7,8,9,10].map(function(seatId) {
                                            return <div className={`seat ${seats[seatId]}`} onClick={() => chooseSeat(seatId)}>{seatId}</div>;
                                        })
                                    }
                                </div>
                            </div>

                            {/* Room 3 */}
                            <div className="room">
                                <div className="horizontal-row">
                                    {
                                        [11,12,13,14,15,16,17,18,19,20].map(function (seatId) {
                                            return <div className={`seat ${seats[seatId]}`} onClick={() => chooseSeat(seatId)}>{seatId}</div>;
                                        })
                                    }
                                </div>
                            </div>
                        </div>

                        {/* Large Room (Bottom Section) */}
                        <div className="room">
                            <div className="horizontal-row flex-wrap">
                                {
                                    [21,22,23,24,25,26,27,28,29,30,31,32,33,34,35].map(function (seatId) {
                                        return <div className={`seat ${seats[seatId]}`} onClick={() => chooseSeat(seatId)}>{seatId}</div>;
                                    })
                                }
                            </div>
                        </div>
                    </div>

                    {/* Room 1 (Right Column) */}
                    <div className="room d-flex flex-column align-items-center vertical-row" style={{height: "100%"}}>
                        {
                            [41,42,43,44,45].map(function (seatId) {
                                return <div className={`seat ${seats[seatId]}`} onClick={() => chooseSeat(seatId)}>{seatId}</div>;
                            })
                        }
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Seat;