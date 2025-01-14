import './ItemDetail.css';
import {useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router-dom";
import {useDispatch} from "react-redux";
import {setOrderItemId, setOrderItemName, setOrderItemPrice} from "../../store";

function ItemDetail() {

    let itemTypeObject = {"DAILY": 0, "CHARGE": 1, "PERIOD": 2, "FIXED": 3};
    let [chooseTab, setChooseTab] = useState(["", "", "", ""]);
    let [tab, setTab] = useState("daily");
    let [id, setId] = useState(0);
    let [type, setType] = useState("");
    let [price, setPrice] = useState("0");

    let navigate = useNavigate();
    let dispatch = useDispatch();
    let location = useLocation();

    useEffect(() => {
        let itemType = new URLSearchParams(location.search).get("itemType");
        let copy = [...chooseTab];
        copy[itemTypeObject[itemType]] = "active";
        setChooseTab(copy);
        setTab(itemType);
    }, []);

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh", backgroundColor: "#f8f9fa", color: "#495057"}}>
            <div className="container" style={{backgroundColor: "#ffffff", padding: "30px", borderRadius: "10px", boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1"}}>

                {/* 탭 메뉴 */}
                <ul className="nav nav-pills mb-3" id="productTabs" role="tablist">
                    <li className="nav-item" role="presentation" onClick={() => {setTab("DAILY")}}>
                        <a className={`nav-link ${chooseTab[0]}`} id="daily-tab" data-bs-toggle="pill" href="#daily" role="tab"
                           aria-controls="daily" aria-selected="true">일일권</a>
                    </li>
                    <li className="nav-item" role="presentation" onClick={() => {setTab("CHARGE")}}>
                        <a className={`nav-link ${chooseTab[1]}`} id="charge-tab" data-bs-toggle="pill" href="#charge" role="tab"
                           aria-controls="charge" aria-selected="false">충전권</a>
                    </li>
                    <li className="nav-item" role="presentation" onClick={() => {setTab("PERIOD")}}>
                        <a className={`nav-link ${chooseTab[2]}`} id="period-tab" data-bs-toggle="pill" href="#period" role="tab"
                           aria-controls="period" aria-selected="false">기간권</a>
                    </li>
                    <li className="nav-item" role="presentation" onClick={() => {setTab("FIXED")}}>
                        <a className={`nav-link ${chooseTab[3]}`} id="fixed-tab" data-bs-toggle="pill" href="#fixed" role="tab"
                           aria-controls="fixed" aria-selected="false">고정석</a>
                    </li>
                </ul>

                {/* 탭 내용 */}
                <div className="tab-content" id="productTabsContent">
                    <Explanation tab={tab}></Explanation>
                </div>

                {/* 환불 규정 및 이용권 안내 */}
                <p className="text-muted mb-4">
                    <a href="#" className="text-muted" data-bs-toggle="modal" data-bs-target="#termsModal">환불 규정 및 이용권 안내></a>
                </p>

                {/* 이용권 선택 및 좌석표 보기 */}
                <div className="d-flex justify-content-between mb-4">
                    <span className="font-weight-bold">이용권 선택</span>
                    <a href="/seats" className="text-primary seats">좌석표 보기</a>
                </div>

                {/* 이용권 박스들 (세로로 배치) */}
                <Price tab={tab} setId={setId} setPrice={setPrice} setType={setType}></Price>

                {/* 결제하기 버튼 */}
                <button className="btn btn-primary w-100" onClick={() => {
                    if (price == 0) {
                        alert("이용권을 선택해주세요.");
                        return;
                    }
                    dispatch(setOrderItemId(id));
                    dispatch(setOrderItemName(type));
                    dispatch(setOrderItemPrice(price));
                    navigate("/orders/summary");
                }}>{price}원 결제하기</button>
            </div>

            {/* 환불 규정 및 이용권 안내 모달 */}
            <div className="modal fade" id="termsModal" tabIndex="-1" aria-labelledby="termsModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                        <div className="modal-header">
                            <h5 className="modal-title" id="termsModalLabel">환불 규정 및 이용권 안내</h5>
                            <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div className="modal-body">
                            <p>여기에는 환불 규정 및 이용권에 대한 안내 내용이 들어갑니다.</p>
                            <p>이 내용은 예시로, 실제 내용은 필요한 정보를 제공해주세요.</p>
                        </div>
                        <div className="modal-footer">
                            <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

function Explanation(props) {
    if (props.tab === "DAILY") {
        return (
            <p className="text-muted mb-4">일일권은 퇴실이 불가능하며 당일 이용 후에 이용권이 완전히 종료돼요. 당일에 연속으로 이용하는 경우에 저렴하게 사용할 수 있는 이용권이에요.</p>
        );
    }
    else if (props.tab === "CHARGE") {
        return (
            <p className="text-muted mb-4">충전해서 계속 사용하는 정기 요금제 50시간권을 결제한 경우 오늘 2시간 사용하면 48시간이 남고, 향후 계속 사용 가능해요.</p>
        );
    }
    else if (props.tab === "PERIOD") {
        return (
            <p className="text-muted mb-4">기간동안 자유석으로 무제한 이용 가능한 이용권입니다. 최초 사용한 시점부터 시간이 차감돼요. 구매 후 원하는 시점에도 사용 가능해요(예를 들어 구매 후 1주일 뒤에 사용). 좌석 점유만 하고 실사용 하지 않을 경우 퇴실처리가 되고, 좌석 선택 후 10분 내에 키오스크에서 입장 처리해주셔야 퇴실 없이 사용 가능합니다.</p>
        );
    }
    else if (props.tab === "FIXED") {
        return (
            <p className="text-muted mb-4">해당 기간동안 좌석을 고정적으로 점유하여 사용 가능한 이용권입니다. 최초 사용한 시점부터 시간이 차감돼요. 구매 후 원하는 시점에도 사용 가능해요(예를 들어 구매 후 1주일 뒤에 사용). 좌석 점유만 하고 실사용 하지 않을 경우 퇴실처리 되지 않고 사용 가능합니다.</p>
        );
    }
}

function Price(props) {
    if (props.tab === "DAILY") {
        return (
            <div className="d-flex flex-column overflow-auto mb-4 scroll-container box">
                <div className="product-box mb-3 card" onClick={() => {props.setId(1); props.setPrice("3,000"); props.setType("일일권 1시간");}}>
                    <p className="text-center">1시간 3,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(2); props.setPrice("4,000"); props.setType("일일권 2시간");}}>
                    <p className="text-center">2시간 4,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(3); props.setPrice("6,000"); props.setType("일일권 4시간");}}>
                    <p className="text-center">4시간 6,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(4); props.setPrice("8,000"); props.setType("일일권 6시간");}}>
                    <p className="text-center">6시간 8,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(5); props.setPrice("9,000"); props.setType("일일권 8시간");}}>
                    <p className="text-center">8시간 9,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(6); props.setPrice("1,000"); props.setType("일일권 10시간");}}>
                    <p className="text-center">10시간 11,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(7); props.setPrice("12,000"); props.setType("일일권 12시간");}}>
                    <p className="text-center">12시간 12,000원</p>
                </div>
            </div>
        );
    }
    else if (props.tab === "CHARGE") {
        return (
            <div className="d-flex flex-column overflow-auto mb-4 scroll-container box">
                <div className="product-box mb-3 card" onClick={() => {props.setId(8); props.setPrice("39,000"); props.setType("충전권 30시간(60일)");}}>
                    <p className="text-center">30시간(60일) 39,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(9); props.setPrice("59,000"); props.setType("충전권 50시간(90일)");}}>
                    <p className="text-center">50시간(90일) 59,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(10); props.setPrice("79,000"); props.setType("충전권 100시간(45일)");}}>
                    <p className="text-center">100시간(45일) 79,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(11); props.setPrice("119,000"); props.setType("충전권 100시간(180일)");}}>
                    <p className="text-center">100시간(180일) 119,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(12); props.setPrice("129,000"); props.setType("충전권 200시간(45일)");}}>
                    <p className="text-center">200시간(45일) 112,900원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(13); props.setPrice("200,000"); props.setType("충전권 200시간(180일)");}}>
                    <p className="text-center">200시간(180일) 200,000원</p>
                </div>
            </div>
        );
    }
    else if (props.tab === "PERIOD") {
        return (
            <div className="d-flex flex-column overflow-auto mb-4 scroll-container box">
                <div className="product-box mb-3 card" onClick={() => {props.setId(14); props.setPrice("39,000"); props.setType("기간권 1주일");}}>
                    <p className="text-center">1주일 39,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(15); props.setPrice("69,000"); props.setType("기간권 2주일");}}>
                    <p className="text-center">2주일 69,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(16); props.setPrice("119,000"); props.setType("기간권 4주일");}}>
                    <p className="text-center">4주일 119,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(17); props.setPrice("209,000"); props.setType("기간권 8주일");}}>
                    <p className="text-center">8주일 209,000원</p>
                </div>
            </div>
        );
    }
    else if (props.tab === "FIXED") {
        return (
            <div className="d-flex flex-column overflow-auto mb-4 scroll-container box">
                <div className="product-box mb-3 card" onClick={() => {props.setId(18); props.setPrice("99,000"); props.setType("고정석 2주일");}}>
                    <p className="text-center">2주일 99,000원</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(19); props.setPrice("169,000"); props.setType("고정석 4주일");}}>
                    <p className="text-center">4주일 169,000</p>
                </div>
                <div className="product-box mb-3 card" onClick={() => {props.setId(20); props.setPrice("319,000"); props.setType("고정석 8주일");}}>
                    <p className="text-center">8주일 319,000</p>
                </div>
            </div>
        );
    }
}

export default ItemDetail;