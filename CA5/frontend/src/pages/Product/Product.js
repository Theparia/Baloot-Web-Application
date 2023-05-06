import React, {useEffect, useState} from 'react';
import Header from "../../components/Header/Header.js";
import Footer from "../../components/Footer/Footer.js";

import './Product.css'
import '../home/Home.css'

import {useParams} from "react-router-dom";
import {getCommodities, rateCommodity, getCommodity, getSuggestedCommodities} from "../../apis/CommoditiesRequest.js";
import {likeComment, dislikeComment, getComments, getCommentVotes, addComment} from "../../apis/CommentsRequest.js"
import {getProvider} from "../../apis/ProviderRequest.js"
import {getBuyList, addToBuyList, removeFromBuyList} from "../../apis/UserRequest.js"
import {Modal} from "react-bootstrap";
import Card from "../../components/Card/Card.js";
import {fetchBuyList} from "../../components/utils.js";

const Comments = () => {
    const {commodityId} = useParams();
    const [commentsList, setCommentsList] = useState([]);
    const [vote, setVote] = useState(true);

    useEffect(() => {
        getComments(commodityId).then((response) => {
            let result = [];
            for (let i in response.data) {
                result.push(response.data[i]);
            }
            setCommentsList(result);
        }).catch(console.error);
    }, []);

    useEffect(() => {
        getComments(commodityId).then((response) => {
            let result = [];
            for (let i in response.data) {
                result.push(response.data[i]);
            }
            setCommentsList(result);
        }).catch(console.error);
    }, [vote]);

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
        const handleLikeComment = (e) => {
            e.preventDefault();
            const req = {"username": sessionStorage.getItem('username')}
            likeComment(comment.id, req).then(() => {
                setVote(!vote)
            }).catch(error => alert(error.response.data));
        }

        const handleDislikeComment = (e) => {
            e.preventDefault();
            const req = {"username": sessionStorage.getItem('username')}
            dislikeComment(comment.id, req).then(() => {
                setVote(!vote)
            }).catch((error) => alert(error.response.data));
        }
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
                    <span className="vote-count-text">{comment.likeCount}</span>
                    <a href="" onClick={async (e) => await handleLikeComment(e)}>
                        <img src="/images/thumbsUp.png" alt="thumbsUp img"/>
                    </a>
                    &nbsp;
                    <span className="vote-count-text">{comment.dislikeCount}</span>
                    <a href="" onClick={async (e) => await handleDislikeComment(e)}>
                        <img src="/images/thumbsDown.png" alt="thumbsDown img"/>
                    </a>
                </div>
            </div>
        )
    }

    const SubmitComment = () => {
        const [commentText, setCommentText] = useState("");

        const handleSubmitComment = (e) => {
            e.preventDefault();
            const req = {"username": sessionStorage.getItem('username'), "commodityId": commodityId, "text": commentText}
            addComment(req).then(() => {
                setVote(!vote);
                setCommentText("");
            }).catch(error => alert(error.response.data));
        }

        return (
            <div className="comment-row" id="submit-comment-section">
                <div className="comment-section-title-font">
                    Submit your opinion
                </div>
                <div className="submit-comment-form">
                    <form>
                        <textarea name="comment" value={commentText} onChange={(event) => {setCommentText(event.target.value)}}></textarea>
                        <button type="submit" onClick={async (e) => await handleSubmitComment(e, commentText)}>Post</button>
                    </form>
                </div>
            </div>
        )
    }

    return (
        <div id="comment-section">
            <CommentTableHeader/>
            <div className="cart-table">
                {commentsList.length > 0 ?
                    commentsList.map((item, index) => (
                        <Comment key={index} comment={item}/>
                    )) : <></>
                }
            </div>
            <SubmitComment/>
        </div>

    )
}

const SuggestedProducts = () => {
    const {commodityId} = useParams();
    const [suggestedCommoditiesList, setSuggestedCommoditiesList] = useState([]);

    useEffect(()=>{
        getSuggestedCommodities(commodityId).then((response) => {
            let result = [];
            for (let i in response.data) {
                result.push(response.data[i]);
            }
            setSuggestedCommoditiesList(result);
        }).catch(console.error);
        },[]);

    const SuggestedCommoditiesTable = () => {
        return (
            <div id="product-container-commodity">
                {suggestedCommoditiesList.length > 0 ?
                    suggestedCommoditiesList.map((item, index) => (
                        <Card key={index} commodity={item}/>
                    )) : <></>
                }
            </div>
        )
    }

    return (
        <div>
            <div className="suggested-product-topic">
                You also might like...
            </div>
            <SuggestedCommoditiesTable/>
        </div>
    )

}

const ProductInfo = () => {
    const {commodityId} = useParams();
    const [commodity, setCommodity] = useState({});
    const [providerName, setProviderName] = useState("");
    const [buyList, setBuyList] = useState([]);


    useEffect(() => {
        fetchCommodity().then();
        fetchBuyList().then();
    }, [commodityId]);

    const fetchCommodity = async () => {
        await getCommodity(commodityId).then(async (responseCommodity) => {
            setCommodity(responseCommodity.data)
            await getProvider(responseCommodity.data.providerId).then((responseProvider) => {
                setProviderName(responseProvider.data.name);
            }).catch((error) => console.log("Provider Not Found"));
        }).catch((error) => console.log("Commodity Not Found"));
    }

    const extractCommodities = async (data) => {
        const commodities = [];
        for (const [commodityId, quantity] of Object.entries(data)) {
            const CommodityResponse = await getCommodity(commodityId);
            const commodity = {...CommodityResponse.data, quantity: quantity};
            commodities.push(commodity);
        }
        return commodities;
    }
    const fetchBuyList = async () => {
        const buyListResponse = await getBuyList(sessionStorage.getItem('username'));
        const commodities = await extractCommodities(buyListResponse.data);
        setBuyList(commodities);
    }

    const AddCommodityToUsersBuyListButton = () => {
        if (Array.isArray(buyList)) {
            const isCommodityInBuyList = buyList.some((item) => item.id === commodity.id);
            const buyListItem = buyList.filter(item => item.id === commodity.id).find(item => item);
            if (isCommodityInBuyList && commodity.inStock > 0) {
                return (
                    <div id="add-to-cart-btn" className="product-info-font">
                        <div className="add-remove-buttons" onClick={(e) => handleRemoveFromBuyList(e)}>-</div>
                        <div> {buyListItem.quantity}</div>
                        <div className="add-remove-buttons" onClick={(e) => handleAddToBuyList(e)}>+</div>
                    </div>
                )
            } else if (commodity.inStock > 0) {
                return (
                    <button id="add-to-cart-btn" className="product-info-font" type="button"
                            onClick={(e) => handleAddToBuyList(e)}>add to cart</button>
                )
            }
        }
    }

    const handleAddToBuyList = (e) => {
        e.preventDefault();
        addToBuyList(sessionStorage.getItem('username'), {"id": commodity.id}).then(async (response) => {
            console.log("ADD TO BUY LIST");
            await fetchBuyList();
        }).catch((error) => alert(error.response.data))
    }

    const handleRemoveFromBuyList = (e) => {
        e.preventDefault();
        removeFromBuyList(sessionStorage.getItem('username'), {"id": commodity.id}).then(async (response) => {
            console.log("REMOVE FROM BUY LIST");
            await fetchBuyList();
        }).catch((error) => alert(error.response.data))
    }

    const StarRating = () => {
        const [rating, setRating] = useState(0);
        const [hoverRating, setHoverRating] = useState(0);

        const handleSubmitRate = (e) => {
            e.preventDefault();
            rateCommodity(commodityId, {"username": sessionStorage.getItem('username'), "score": rating})
                .then((response) => {
                    fetchCommodity().then();
                })
                .catch((error) => alert(error.response.data))
        }

        const handleStarClick = (index) => {
            setRating(index + 1);
        };

        const handleStarHover = (index) => {
            setHoverRating(index + 1);
        };

        const handleStarLeave = () => {
            setHoverRating(0);
        };


        return (
            <div className="submit-part">
                <div>
                    <div className="provider-name product-info-font">
                        rate now
                    </div>
                    <div className="stars">
                        <div>
                            {[...Array(10)].map((_, index) => {
                                const starImg = (index < rating || index < hoverRating) ? "star-on.png" : "star-off.png";
                                return (
                                    <img
                                        src={"/images/" + starImg} alt="start"
                                        key={index}
                                        onClick={() => handleStarClick(index)}
                                        onMouseEnter={() => handleStarHover(index)}
                                        onMouseLeave={handleStarLeave}
                                    />
                                )
                            })}
                        </div>
                    </div>
                </div>
                <button id="submit-btn" onClick={(e) => handleSubmitRate(e)}>
                    submit
                </button>
            </div>
        );
    };

    const Categories = () => {
        const [showCategoriesModal, setShowCategoriesModal] = useState(false);

        const handleShowMore = (e) => {
            e.preventDefault();
            setShowCategoriesModal(true);
        }
        return (
            <div id="categories" className="product-info-font">
                Category(s)
                {commodity.categories && commodity.categories.length <= 4 ?

                    <ul>
                        {commodity.categories.map((category, index) => (
                            <li key={index}>{category}</li>
                        ))}
                    </ul>

                    : commodity.categories && (
                    <>
                        <ul>
                            <li>{commodity.categories[0]}</li>
                            <li>{commodity.categories[1]}</li>
                            <li>{commodity.categories[2]}</li>
                            <li>{commodity.categories[3]}</li>
                        </ul>
                        <div id="show-more" onClick={(e) => handleShowMore(e)}>Show more...</div>
                        {showCategoriesModal && (
                            <>
                                <div className="modal-overlay"/>
                                <div className="modal">
                                    <Modal.Header className="modal-header">
                                        <Modal.Title className="modal-title">Categories:</Modal.Title>
                                    </Modal.Header>
                                    <Modal.Body className="modal-body">
                                        <ul>
                                            {commodity.categories.map((category, index) => (
                                                <li key={index}>{category}</li>
                                            ))}
                                        </ul>
                                    </Modal.Body>
                                    <Modal.Footer className="modal-footer">
                                        <button onClick={() => setShowCategoriesModal(false)}>Close</button>
                                    </Modal.Footer>
                                </div>
                            </>
                        )}
                    </>
                )}
            </div>
        )
    }

    const Information = () => {
        return (
            <div className="category-part">
                <div>
                    <div className="product-name">
                        {commodity.name}
                    </div>
                    <div id="stock" className="product-info-font">
                        {commodity.inStock} left in stock
                    </div>
                    <div className="provider-name product-info-font">
                        by <a href={"/providers/" + commodity.providerId}>{providerName}</a>
                    </div>
                    <Categories/>
                </div>
                <div id="rating">
                    <img src="/images/star-on.png"/>
                    <div id="rating-font" className="product-info-font">
                        {commodity.rating}
                    </div>
                    {commodity?.usersRating && <div id="number-of-ratings" className="product-info-font">
                        ({Object.keys(commodity.usersRating).length})</div>}
                </div>
            </div>
        )
    }

    const CartPart = () => {
        return(
            <div className="cart-part">
                <div id="price" className="product-info-font">
                    {commodity.price}$
                </div>
                <AddCommodityToUsersBuyListButton/>
            </div>
        )
    }

    return (
        <div className="product-info-section">
            <div className="product-img">
                <img src={commodity.image}/>
            </div>
            <div className="product-info">
                <Information/>
                <CartPart/>
                <StarRating/>
            </div>
        </div>
    )
}


const Product = () => {
    if (sessionStorage.getItem('username') === null) {
        window.location.replace("/login")
        return;
    }
    return (
        <>
            <Header username={sessionStorage.getItem('username')}/>
            <main id="main-product">
                <ProductInfo/>
                <Comments/>
                <SuggestedProducts/>
            </main>
            <Footer/>
        </>

    )
}

export default Product;