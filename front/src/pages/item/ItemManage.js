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

    return (
        <div className="container" id="categoryContainer">
            <h2>상품 관리 페이지</h2>
            {
                itemsPerItemType.map(function(itemType) {
                    return (
                        <>
                            <div className="category-title">
                                카테고리명: {categoryNameDict[itemType.itemType]}
                            </div>
                            <div className="category" id="category-1">
                                <ul className="product-list" id="productList-1">
                                    {
                                        itemType.items.map(function(item) {
                                            return (
                                                <li>
                                                    상품 종류: {item.itemName} {item.price}원
                                                    <span>
                                                        <button onClick="editProduct(this, '1시간')">수정</button>
                                                        <button onClick="deleteProduct(this)">삭제</button>
                                                    </span>
                                                </li>
                                            );
                                        })
                                    }
                                </ul>
                                <div className="actions">
                                    <button className="add-product-btn" onClick="addProduct(1)">상품 추가</button>
                                </div>
                            </div>
                        </>
                    );
                })
            }
        </div>
    );
}

export default ItemManage;