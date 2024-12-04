import {useEffect, useState} from "react";
import axios from "axios";
import {changeLoginStatus, changeUserInOut} from "../../store";

function PostList() {

    let [posts, setPosts] = useState([]);

    useEffect(() => {
        axios.get("/posts")
            .then((result) => {
                console.log(result.data);
                setPosts(result.data);
            })
            .catch((error) => {
                console.error("게시글 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, []);

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh", color: "#495057"}}>
            <div className="board-container w-50" style={{backgroundColor: "#ffffff", padding: "30px", borderRadius: "10px", boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)"}}>
                {/* 게시판 제목 */}
                <h2 className="text-center text-primary mb-4">스터디카페 게시판</h2>

                {/* 게시글 목록 (스크롤 가능한 영역) */}
                <div className="post-box scroll-container" style={{backgroundColor: "#f1f3f5", borderRadius: "8px", padding: "20px", maxHeight: "400px"}}>
                    {
                        posts.map(function(post) {
                            return (
                                <div className="post-item mb-3"
                                     style={{backgroundColor: "#ffffff", borderRadius: "8px", padding: "15px", boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)"}}>
                                    <div className="row">
                                        <div className="col-3" style={{fontWeight: "bold"}}>내용</div>
                                        <div className="col-9">{post.content}</div>
                                    </div>
                                    <div className="row">
                                        <div className="col-3" style={{fontWeight: "bold"}}>작성일</div>
                                        <div className="col-9">{post.postDateTime}</div>
                                    </div>
                                    <div className="row">
                                        <div className="col-3" style={{fontWeight: "bold"}}>확인</div>
                                        <div className="col-9">{post.reflect ? 'O' : null}</div>
                                    </div>
                                </div>
                            );
                        })
                    }
                </div>

                {/* 게시글 작성 영역 */}
                <div className="post-form mt-4">
                    <form>
                        <div className="mb-3">
                            <textarea className="form-control" id="content" rows="3" placeholder="요구사항을 입력하세요"></textarea>
                        </div>
                        <button type="submit" className="btn btn-primary w-100">등록</button>
                    </form>
                </div>
            </div>
        </div>
    );
}

export default PostList;