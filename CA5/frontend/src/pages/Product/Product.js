import React, { useEffect, useState } from 'react';
import Header from "../../components/Header/Header.js";
import Footer from "../../components/Footer/Footer.js";

import './Product.css'
import {useParams} from "react-router-dom";
import {getComments, getCommoditiesSize} from "../../apis/CommoditiesRequest.js";

const Comments = () => {
    const {commodityId} = useParams();
    const [commentsList, setCommentsList] = useState([]);

    useEffect(() => {
        getComments(commodityId).then((response) => {
            console.log("Getting Comments")
            let result = [];
            for (let i in response.data) {
                console.log(response.data[i].text);
                result.push(response.data[i]);
            }
            setCommentsList(result);
        }).catch(console.error);
    }, []);


    const CommentTableHeader = () => {
        return (
            <div className="comment-section-title comment-section-title-font">
                Comments &nbsp;
                <div className="comment-count">
                    (<span>{commentsList.length}</span>)
                </div>
            </div>
        )
    }

    const Comment = ({comment}) => {
        return (
            <div className="comment-row">
                <div className="comment-text">
                    {comment.text}
                </div>
                <div className="comment-details">
                    <span>{comment.date}</span>&emsp;&emsp;&#x2022;&emsp;&emsp;#<span>{comment.username}</span>
                </div>
                <div className="comment-voting">
                    Is this comment helpful?
                    &nbsp;
                    <span className="vote-count-text">1</span>
                    <a href="">
                        <img src="/images/thumbsUp.png" alt="thumbsUp img"/>
                    </a>
                    &nbsp;
                    <span className="vote-count-text">1</span>
                    <a href="">
                        <img src="/images/thumbsDown.png" alt="thumbsDown img"/>
                    </a>
                </div>
            </div>
        )
    }

    return (
        <div>
            <CommentTableHeader/>
            <div className="cart-table">
                {commentsList.length > 0 ?
                    commentsList.map((item, index) => (
                        <Comment key={index} comment={item}/>
                    )) : <></>
                }

                <div className="comment-row" id="submit-comment-section">
                    <div className="comment-section-title-font">
                        Submit your opinion
                    </div>
                    <div className="submit-comment-form">
                        <form action="/submit-comment" method="POST">
                            <textarea name="comment"></textarea>
                            <button type="submit">Post</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>

    )
}


const Product = () => {

    const {productId} = useParams();

    const Temp = () => {
        return (
            <div className="product-info-section">
                <div className="product-img">
                    <img src="/images/mobile.png"/>
                </div>
                <div className="product-info">
                    <div className="category-part">
                        <div>
                            <div id="product-name">
                                Huawei nova 9
                            </div>
                            <div id="stock" className="product-info-font">
                                5 left in stock
                            </div>
                            <div className="provider-name product-info-font">
                                by <a href="#">Huawei</a>
                            </div>
                            <div id="categories" className="product-info-font">
                                Category(s)
                                <ul>
                                    <li>
                                        Technologhy
                                    </li>
                                    <li>
                                        IT
                                    </li>
                                </ul>
                            </div>
                        </div>
                        <div id="rating">
                            <img src="/images/star.png"/>
                                <div id="rating-font" className="product-info-font">
                                    4.1
                                </div>
                                <div id="number-of-ratings" className="product-info-font">
                                    (12)
                                </div>
                        </div>
                    </div>
                    <div className="cart-part">
                        <div id="price" className="product-info-font">
                            300$
                        </div>
                        <button id="add-to-cart-btn" className="product-info-font">
                            add to cart
                        </button>
                    </div>
                    <div className="submit-part">
                        <div>
                            <div className="provider-name product-info-font">
                                rate now
                            </div>
                            <div className="stars">
                                <img src="/images/star.png" alt="start"/>
                                <img src="/images/star.png" alt="start"/>
                                <img src="/images/star.png" alt="start"/>
                                <img src="/images/star.png" alt="start"/>
                                <img src="/images/star.png" alt="start"/>
                                <img src="/images/star.png" alt="start"/>
                                <img src="/images/star.png" alt="start"/>
                                <img src="/images/star.png" alt="start"/>
                                <img src="/images/star.png" alt="start"/>
                                <img src="/images/star.png" alt="start"/>
                            </div>
                        </div>
                        <button id="submit-btn">
                            submit
                        </button>
                    </div>
                </div>
            </div>
        )
    }


    return(
        <>
            <Header searchBar={false} username={sessionStorage.getItem('username')}/>
            <main id="main-product">
                <Temp/>
                <Comments/>
            </main>
            <Footer/>
        </>

    )
}

export default Product;