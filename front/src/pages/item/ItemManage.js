import './ItemManage.css';
import {useEffect, useState} from "react";
import axios from "axios";

function ItemManage() {

    let [categories, setCategories] = useState([]);
    let categoryNameDict = {"DAILY": "일일권", "CHARGE": "충전권", "PERIOD": "기간권", "FIXED": "고정석"};
    let [items, setItems] = useState([[]]); // 2차원 배열, 하나의 카테고리 당 하나의 배열을 가짐

    useEffect(() => {
        axios.get("/items/manage")
            .then((result) => {
                setCategories(result.data);
                console.log(result.data); // categories

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

    function getUsageTimeAndUsagePeriod(itemType, itemName) {

        let usageTime = null, usagePeriod = null;
        if (itemType === "DAILY") {
            usageTime = itemName.replace("시간", "");
            usageTime = Number(usageTime);
        } else if (itemType === "CHARGE") {
            let usageTimePeriod = itemName.match(/\d+/g);
            usageTime = parseInt(usageTimePeriod[0], 10);
            usagePeriod = parseInt(usageTimePeriod[1], 10);
        } else {
            usagePeriod = itemName.replace("주일", "");
            usagePeriod = Number(usagePeriod);
        }
        return [usageTime, usagePeriod];
    }

    return (
        <div className="container" id="categoryContainer">
            <h2>상품 관리 페이지</h2>
            {
                categories.map(function(category, i) {
                    return (
                        <div className="category" id="category-1" key={category.itemType}>
                            <div className="category-title">
                                카테고리명: {categoryNameDict[category.itemType] || "기타"}
                                <span>
                                    <button className="edit-category-btn" onClick={() => {
                                        let modifyCategoryNameYn = window.confirm("카테고리명을 수정하시겠습니까?");
                                        if (!modifyCategoryNameYn) {
                                            return;
                                        }


                                    }}>수정</button>
                                    <button className="delete-category-btn" onClick={() => {
                                        let deleteYn = window.confirm("해당 카테고리를 삭제하시겠습니까? 카테고리를 삭제할 경우, 카테고리 내의 상품도 전부 삭제됩니다.");
                                        if (!deleteYn) {
                                            return;
                                        }

                                        axios.delete("/items?itemType=" + category.itemType)
                                            .then(() => {{
                                                alert("해당 카테고리가 삭제되었습니다!");
                                            }})
                                            .catch((error) => {
                                                console.error("카테고리 삭제 중 에러 발생:", error.response ? error.response.data : error.message);
                                                if (error.response) {
                                                    console.error("에러 상태 코드:", error.response.status);
                                                }
                                            });

                                        let newCategories = [...categories];
                                        newCategories = newCategories.filter((newCategory) => {return newCategory.itemType !== category.itemType});
                                        setCategories(newCategories);
                                    }}>삭제</button>
                                </span>
                            </div>
                            <ul className="product-list" id="productList-1">
                                {
                                    items[i] !== undefined ?
                                    items[i].map(function(item, j) {
                                        return (
                                            <li key={item.itemName}>상품 종류: {item.itemName + " " + item.price + "원"}
                                                <span>
                                                    <button onClick={() => {
                                                        let modifyYn = window.confirm("해당 상품을 수정하시겠습니까?")
                                                        if (!modifyYn) {
                                                            return;
                                                        }

                                                        let itemName = prompt("상품명을 입력해주세요.");
                                                        let price = prompt("상품 가격을 입력해주세요(숫자만 입력해주세요)");

                                                        let [usageTime, usagePeriod] = getUsageTimeAndUsagePeriod(category.itemType, itemName); // itemName -> usageTime, usagePeriod 구하기
                                                        price = Number(price); // price -> Number 타입으로 변환

                                                        axios.patch("/items/" + item.id, {
                                                                itemType: category.itemType,
                                                                itemName: itemName,
                                                                usageTime: usageTime,
                                                                usagePeriod: usagePeriod,
                                                                price: price
                                                            }, {
                                                                headers: { "Content-Type": "application/json" }
                                                            })
                                                            .then(() => {
                                                                alert("해당 상품을 수정하였습니다!");
                                                            })
                                                            .catch((error) => {
                                                                console.error("상품 수정 중 에러 발생:", error.response ? error.response.data : error.message);
                                                                if (error.response) {
                                                                    console.error("에러 상태 코드:", error.response.status);
                                                                }
                                                            });

                                                        let newItems = [...items];
                                                        newItems[i][j] = {itemName: itemName, price: price};
                                                        setItems(newItems);
                                                    }}>수정</button>
                                                    <button onClick={() => {
                                                        let deleteYn = window.confirm("해당 상품을 삭제하시겠습니까?")
                                                        if (!deleteYn) {
                                                            return;
                                                        }

                                                        axios.delete("/items/" + item.id)
                                                            .then(() => {{
                                                                alert("해당 상품이 삭제되었습니다!");
                                                            }})
                                                            .catch((error) => {
                                                                console.error("상품 삭제 중 에러 발생:", error.response ? error.response.data : error.message);
                                                                if (error.response) {
                                                                    console.error("에러 상태 코드:", error.response.status);
                                                                }
                                                            });

                                                            let newItems = [...items];
                                                            newItems[i] = newItems[i].filter((newItem) => {return newItem.id !== item.id});
                                                            setItems(newItems);
                                                    }}>삭제</button>
                                                </span>
                                            </li>
                                        );
                                    }) :
                                    null
                                }
                            </ul>
                            <div className="actions">
                                <button className="add-product-btn" onClick={() => {
                                    let itemName = prompt("상품명을 입력해주세요.");
                                    let price = prompt("상품 가격을 입력해주세요(숫자만 입력해주세요)");

                                    let [usageTime, usagePeriod] = getUsageTimeAndUsagePeriod(category.itemType, itemName); // itemName -> usageTime, usagePeriod 구하기
                                    price = Number(price); // price -> Number 타입으로 변환

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
                <button className="add-category-btn" onClick={() => {
                    let addCategoryYn = window.confirm("카테고리를 추가하시겠습니까?")
                    if (!addCategoryYn) {
                        return;
                    }

                    let categoryName = prompt("카테고리명을 입력해주세요.");

                    let newCategories = [...categories];
                    newCategories.push({itemType: 'FIXED', items: []})
                    setCategories(newCategories);
                }}>카테고리 추가</button>
            </div>
        </div>
    );
}

export default ItemManage;