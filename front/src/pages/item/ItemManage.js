import './ItemManage.css';
import {useEffect, useState} from "react";
import axios from "axios";

function ItemManage() {

    let [categories, setCategories] = useState([]);
    let categoryName = {"DAILY": "일일권", "CHARGE": "충전권", "PERIOD": "기간권", "FIXED": "고정석"};
    let [items, setItems] = useState([[]]); // 2차원 배열, 하나의 카테고리 당 하나의 배열을 가짐

    useEffect(() => {
        axios.get("/items/manage")
            .then((result) => {
                setCategories(result.data);

                let newItems = new Array(result.data.length);
                for (let i=0; i<result.data.length; i++) {
                    newItems[i] = result.data[i].items;
                }
                setItems(newItems);
            })
            .catch((error) => {
                console.error("상품 정보 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, [])

    return (
        <div className="container" id="categoryContainer">
            <h2>상품 관리 페이지</h2>
            {
                categories.map(function(category, i) {
                    return (
                        <div className="category" id="category-1" key={category.itemType}>
                            <div className="category-title">
                                카테고리명: {categoryName[category.itemType]}
                                <span>
                                    <button className="edit-category-btn">수정</button>
                                    <button className="delete-category-btn">삭제</button>
                                </span>
                            </div>
                            <ul className="product-list" id="productList-1">
                                {
                                    items[i].map(function(item) {
                                        return (
                                            <li key={item.itemName}>상품 종류: {item.itemName + " " + item.price + "원"}
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
                                    let itemName = prompt("상품명을 입력해주세요.");
                                    let price = prompt("상품 가격을 입력해주세요(숫자만 입력해주세요)");

                                    // itemName -> usageTime, usagePeriod 구하기
                                    let usageTime = null, usagePeriod = null;
                                    if (category.itemType === "DAILY") {
                                        usageTime = itemName.replace("시간", "");
                                        usageTime = Number(usageTime);
                                    } else if (category.itemType === "CHARGE") {
                                        let usageTimePeriod = itemName.match(/\d+/g);
                                        usageTime = parseInt(usageTimePeriod[0], 10);
                                        usagePeriod = parseInt(usageTimePeriod[1], 10);
                                    } else {
                                        usagePeriod = itemName.replace("주일", "");
                                        usagePeriod = Number(usagePeriod);
                                    }

                                    // price -> Number 타입으로 변환
                                    price = Number(price);

                                    // 서버에 POST /items 요청 보내기
                                    axios.post("/items", {
                                            itemType: category.itemType,
                                            itemName: itemName,
                                            usageTime: usageTime,
                                            usagePeriod: usagePeriod,
                                            price: price
                                        }, {
                                            headers: { "Content-Type": "application/json" }
                                        })
                                        .then(() => {{
                                            alert("상품을 등록하였습니다!");
                                        }})
                                        .catch((error) => {
                                            console.error("상품 등록 중 에러 발생:", error.response ? error.response.data : error.message);
                                            if (error.response) {
                                                console.error("에러 상태 코드:", error.response.status);
                                            }
                                        });

                                    // 추가된 이용권에 대해, 화면 일부 갱신 (새로고침 x)
                                    let newItems = [...items];
                                    newItems[i].push({itemName: itemName, price: price})
                                    setItems(newItems);
                                }}>상품 추가</button>
                            </div>
                        </div>
                    );
                })
            }
            <div className="actions">
                <button className="add-category-btn">카테고리 추가</button>
            </div>
        </div>
    );
}

export default ItemManage;