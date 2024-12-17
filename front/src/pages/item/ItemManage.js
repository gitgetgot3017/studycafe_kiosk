import './ItemManage.css';
import {useEffect, useState} from "react";
import axios from "axios";

function ItemManage() {

    let [categories, setCategories] = useState([]);
    let categoryName = {"DAILY": "일일권", "CHARGE": "충전권", "PERIOD": "기간권", "FIXED": "고정석"};

    useEffect(() => {
        axios.get("/items/manage")
            .then((result) => {
                console.log(result.data);
                setCategories(result.data);
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
                categories.map(function(category) {
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
                                    category.items.map(function(item) {
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
                                <button className="add-product-btn">상품 추가</button>
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