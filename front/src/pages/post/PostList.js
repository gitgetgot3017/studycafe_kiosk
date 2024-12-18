import {useEffect, useState} from "react";
import axios from "axios";

function PostList() {

    let [posts, setPosts] = useState([]);
    let [postCnt, setPostCnt] = useState(posts.length);
    let [content, setContent] = useState("");
    let [modalContent, setModalContent] = useState("");
    let [modifyPostId, setModifyPostId] = useState(0);

    useEffect(() => {
        axios.get("/posts")
            .then((result) => {
                setPosts(result.data);
            })
            .catch((error) => {
                console.error("게시글 가져오는 중 에러 발생:", error.response ? error.response.data : error.message);
                if (error.response) {
                    console.error("에러 상태 코드:", error.response.status);
                }
            });
    }, [postCnt]);

    return (
        <div className="bg-dark d-flex align-items-center justify-content-center" style={{height: "100vh", color: "#495057"}}>
            <div className="board-container w-50" style={{backgroundColor: "#ffffff", padding: "30px", borderRadius: "10px", boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)"}}>
                {/* 게시판 제목 */}
                <h2 className="text-center text-primary mb-4">스터디카페 게시판</h2>

                {/* 게시글 목록 (스크롤 가능한 영역) */}
                <div className="post-box scroll-container" style={{backgroundColor: "#f1f3f5", borderRadius: "8px", padding: "20px", maxHeight: "400px"}}>
                    {
                        posts.map(function(post, i) {
                            return (
                                <div className="post-item mb-3"
                                     style={{backgroundColor: "#ffffff", borderRadius: "8px", padding: "15px", boxShadow: "0 4px 6px rgba(0, 0, 0, 0.1)"}} key={i}>
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
                                        <div className="col-9">
                                            <input type="checkbox" checked={post.reflect} onChange={(e) => {
                                                if (e.target.checked) {
                                                    axios.post("/posts/" + post.postId + "/check")
                                                        .then(() => {
                                                            alert("확인 처리하였습니다!");
                                                        })
                                                        .catch((error) => {
                                                            console.error("게시글 check 중 에러 발생:", error.response ? error.response.data : error.message);
                                                            if (error.response) {
                                                                console.error("에러 상태 코드:", error.response.status);
                                                            }
                                                        });

                                                    let newPosts = [...posts];
                                                    newPosts = newPosts.filter((newPost) => {
                                                        if (newPost.id === post.id) {
                                                            return post.reflect = true;
                                                        }
                                                    })
                                                    setPosts(newPosts);
                                                } else {
                                                    console.log('unchecked');
                                                }
                                            }} />
                                        </div>
                                    </div>
                                    {
                                        post.mine ?
                                        <div className="row mt-2">
                                            <div className="row mt-2">
                                                <div className="col-12 d-flex justify-content-center">
                                                    <button className="btn btn-sm btn-primary me-2" data-bs-toggle="modal" data-bs-target="#modifyPostModal" onClick={() => {setModalContent(post.content); setModifyPostId(post.postId);}}>수정</button>
                                                    <button className="btn btn-sm btn-danger" onClick={() => {
                                                        let result = window.confirm("정말로 게시글을 삭제하시겠습니까?");
                                                        if (!result) {
                                                            return;
                                                        }

                                                        let newPosts = [...posts];
                                                        newPosts = newPosts.filter((newPost) => {return newPost.postId !== post.postId})
                                                        setPosts(newPosts);

                                                        axios.delete("/posts/" + post.postId)
                                                            .then(() => {{
                                                                alert("게시글이 삭제되었습니다!");
                                                            }})
                                                            .catch((error) => {
                                                                console.error("게시글 숨김 처리 중 에러 발생:", error.response ? error.response.data : error.message);
                                                                if (error.response) {
                                                                    console.error("에러 상태 코드:", error.response.status);
                                                                }
                                                            });
                                                    }}>삭제</button>
                                                </div>
                                            </div>
                                        </div> :
                                        null
                                    }
                                </div>
                            );
                        })
                    }
                </div>

                {/* 게시글 작성 영역 */}
                <div className="post-form mt-4">
                    <form>
                        <div className="mb-3">
                            <textarea onChange={(e) => {setContent(e.target.value);}} className="form-control" id="content" rows="3" placeholder="요구사항을 입력하세요" style={{resize: "none"}}></textarea>
                        </div>
                        <button type="button" className="btn btn-primary w-100" onClick={() => {
                            if (content === "") {
                                alert("게시글의 내용을 입력해주세요.");
                                return;
                            }

                            axios.post("/posts", {
                                content: content
                                }, {
                                    headers: { "Content-Type": "application/json" }
                                })
                                .then(() => {
                                    setPostCnt(postCnt + 1);
                                    window.location.href = "/posts";
                                })
                                .catch((error) => {
                                    console.error("게시글 등록 중 에러 발생:", error.response ? error.response.data : error.message);
                                    if (error.response) {
                                        console.error("에러 상태 코드:", error.response.status);
                                    }
                                });
                        }}>등록</button>
                    </form>
                </div>

                {/* 게시글 수정 모달 */}
                <div className="modal fade" id="modifyPostModal" tabIndex="-1" aria-labelledby="modifyPostModalLabel" aria-hidden="true">
                    <div className="modal-dialog">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h5 className="modal-title" id="termsModalLabel">게시글 수정</h5>
                                <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <textarea className="modal-body" style={{resize: "none", border: "none"}} value={modalContent} onChange={(e) => {setModalContent(e.target.value)}}></textarea>
                            <div className="modal-footer">
                                <button type="button" className="btn btn-secondary" data-bs-dismiss="modal" onClick={() => {
                                    let result = window.confirm("게시글을 수정하시겠습니까?");
                                    if (!result) {
                                        return;
                                    }

                                    axios.patch("/posts/" + modifyPostId, {
                                            content: modalContent
                                        })
                                        .then(() => {{
                                            alert("게시글이 수정되었습니다!");
                                        }})
                                        .catch((error) => {
                                            console.error("게시글 수정 중 에러 발생:", error.response ? error.response.data : error.message);
                                            if (error.response) {
                                                console.error("에러 상태 코드:", error.response.status);
                                            }
                                        });

                                    let newPosts = [...posts];
                                    for (let post of newPosts) {
                                        if (post.postId === modifyPostId) {
                                            post.content = modalContent;
                                        }
                                    }
                                    setPosts(newPosts);
                                }}>수정하기</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default PostList;