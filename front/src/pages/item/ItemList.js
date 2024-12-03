import './ItemList.css';
import {useNavigate} from "react-router-dom";
import {useSelector} from "react-redux";

function ItemList() {

    let navigate = useNavigate();
    let state = useSelector((state) => {return state});

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh", backgroundColor: "#f8f9fa"}}>
            <div className="container" style={{backgroundColor: "#ffffff", padding: "30px", borderRadius: "10px", boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)", maxWidth: "600px", marginTop: "50px"}}>
                {/* 제목과 설명 */}
                <h2 className="text-primary mb-2">이용권 구매</h2>
                <p className="text-muted mb-4">
                    원하는 이용권을 선택해주세요.
                    {
                        !state.login ?
                        <>
                            <br/>
                            (비회원은 일일권만 이용 가능합니다)
                        </> :
                        null
                    }
                </p>

                {/* 상품 박스들을 세로로 배치하고, 스크롤 가능 영역 설정 */}
                <div className="scroll-container">
                    <div className="product-box item" onClick={() => {navigate("/items/detail?itemType=DAILY")}}>
                        <h5 className="text-center">일일권</h5>
                        <hr/>
                        <p className="text-center">오늘 하루만 사용하고 싶을 때 추천해요</p>
                    </div>
                    {
                        state.login ?
                        <>
                            <div className="product-box item" onClick={() => {navigate("/items/detail?itemType=CHARGE")}}>
                                <h5 className="text-center">충전권</h5>
                                <hr/>
                                <p className="text-center">시간을 충전해서 정기적으로 사용할 수 있어요</p>
                            </div>
                            <div className="product-box item" onClick={() => {navigate("/items/detail?itemType=PERIOD")}}>
                                <h5 className="text-center">기간권</h5>
                                <hr/>
                                <p className="text-center">일정 기간 동안 무제한으로 사용할 수 있어요</p>
                            </div>
                            <div className="product-box item" onClick={() => {navigate("/items/detail?itemType=FIXED")}}>
                                <h5 className="text-center">고정석</h5>
                                <hr/>
                                <p className="text-center">지정된 좌석을 일정 기간 동안 무제한으로 사용할 수 있어요</p>
                            </div>
                        </> :
                        null
                    }
                </div>
            </div>
        </div>
    );
}

export default ItemList;