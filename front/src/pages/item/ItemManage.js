import './ItemManage.css';
import {useEffect, useState} from "react";
import axios from "axios";

function ItemManage() {

    let categoryNameDict = {"DAILY": "일일권", "CHARGE": "충전권", "PERIOD": "기간권", "FIXED": "고정석"};
    let [itemsPerItemType, setItemsPerItemType] = useState([]);

    useEffect(() => {
        axios.get("/items/detail")
            .then((result) => {
                setItemsPerItemType(result.data);
            })
            .catch((error) => {
                console.error("상품 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, [])

    function getItemName(itemType) {

        let itemName;
        while (true) {
            if (itemType === "DAILY") {
                itemName = prompt("몇 시간을 추가할까요? (숫자만 입력해주세요)");

            } else if (itemType === "CHARGE") {
                itemName = prompt("몇 시간을 추가할까요? (숫자만 입력해주세요)");
                itemName += ",";
                itemName += prompt("며칠을 유효기간으로 할까요? (숫자만 입력해주세요) 유효기간이 없는 경우에는 0을 입력해주세요.");
            } else {
                itemName = prompt("몇 주일을 추가할까요? (숫자만 입력해주세요)");
            }

            if (itemName == "" || isNaN(itemName)) { // 아무 것도 입력하지 않은 경우 또는 숫자만 입력하지 않은 경우
                alert("숫자만 입력해주세요.");
            } else {
                break;
            }
        }

        return itemName;
    }

    function getPrice() {
        while (true) {
            let price = prompt("가격을 입력하세요 (,나 원 없이 숫자만 입력해주세요)");

            if (price == "" || isNaN(price)) { // 아무 것도 입력하지 않은 경우 또는 숫자만 입력하지 않은 경우
                alert("숫자만 입력해주세요.");
            } else {
                break;
            }
        }
    }

    function getFullItemName(itemType, itemName) {

        if (itemType === "DAILY") {
            return itemName + "시간";
        } else if (itemType === "CHARGE") {
            let [hour, day] = itemName.split(",");
            if (day === "0") {
                return hour + "시간";
            }
            return hour + "시간(" + day + "일)";
        } else {
            return itemName + "주일";
        }
    }

    function getUsageTimeAndPeriod(itemType, itemName) {

        if (itemType === "DAILY") {
            return [itemName, null];
        } else if (itemType === "CHARGE") {
            return itemName.split(",");
        } else {
            return [null, itemName * 7];
        }
    }

    return (
        <div className="container" id="categoryContainer">
            <h2>상품 관리 페이지</h2>
            {
                itemsPerItemType.map(function(itemType) {
                    return (
                        <div key={itemType.itemType}>
                            <div className="category-title">
                                {categoryNameDict[itemType.itemType]}
                            </div>
                            <div className="category" id="category-1">
                                <ul className="product-list" id="productList-1">
                                    {
                                        itemType.items.map(function(item) {
                                            return (
                                                <li key={item.id}>
                                                    상품 종류: {item.itemName} {item.price}원
                                                    <span>
                                                        <button>수정</button>
                                                        <button>삭제</button>
                                                    </span>
                                                </li>
                                            );
                                        })
                                    }
                                </ul>
                                <div className="actions">
                                    <button className="add-product-btn" onClick={() => {
                                        let itemName = getItemName(itemType.itemType);
                                        let price = getPrice();
                                        let [usageTime, usagePeriod] = getUsageTimeAndPeriod(itemType.itemType, itemName);
                                        itemName = getFullItemName(itemType.itemType, itemName);

                                        axios.post("/items", {
                                                itemType: itemType.itemType,
                                                itemName: itemName,
                                                usageTime: usageTime,
                                                usagePeriod: usagePeriod,
                                                price: price
                                            }, {
                                                headers: { "Content-Type": "application/json" }
                                            })
                                            .then(() => {
                                                alert("상품이 추가되었습니다!");
                                            })
                                            .catch((error) => {
                                                console.error("상품 추가 중 에러 발생:", error.response ? error.response.data : error.message);
                                                if (error.response) {
                                                    console.error("에러 상태 코드:", error.response.status);
                                                }
                                            });

                                        let copy = [...itemsPerItemType];
                                        let itemPerItemType = copy.find((itemPerItemType) => itemPerItemType.itemType === itemType.itemType);
                                        let idx = itemPerItemType.items.length;

                                        if (itemType.itemType === "DAILY") {
                                            for (let i=0; i<itemPerItemType.items.length; i++) {
                                                if (itemPerItemType.items[i].usageTime > usageTime) {
                                                    idx = i;
                                                    break;
                                                }
                                            }
                                        } else if (itemType.itemType === "CHARGE") {
                                            for (let i=0; i<itemPerItemType.items.length; i++) {
                                                if (itemPerItemType.items[i].usageTime >= usageTime) {
                                                    idx = i;
                                                    break;
                                                }
                                            }

                                            for (let i=idx; i<itemPerItemType.items.length; i++) {
                                                if (itemPerItemType.items[i].usagePeriod > usagePeriod || itemPerItemType.items[i].usageTime > usageTime) {
                                                    idx = i;
                                                    break;
                                                }
                                            }
                                        } else {
                                            for (let i=0; i<itemPerItemType.items.length; i++) {
                                                if (itemPerItemType.items[i].usagePeriod > usagePeriod) {
                                                    idx = i;
                                                    break;
                                                }
                                            }
                                        }
                                        itemPerItemType.items.splice(idx, 0, {itemName: itemName, price: price});
                                        setItemsPerItemType(copy);
                                    }}>상품 추가</button>
                                </div>
                            </div>
                        </div>
                    );
                })
            }
        </div>
    );
}

export default ItemManage;