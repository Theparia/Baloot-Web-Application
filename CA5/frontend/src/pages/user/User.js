import React, {useEffect, useState} from 'react';
import Header from "../../components/Header/Header.js";
import {useParams} from "react-router-dom";
import Footer from "../../components/Footer/Footer.js";
import "./User.css"
import {
    addCredit,
    addToBuyList, applyDiscountCode, deleteDiscountCode, finalizePayment,
    getBuyList,
    getPurchasedList,
    getUser,
    removeFromBuyList
} from "../../apis/UserRequest.js";
import {getCommodity} from "../../apis/CommoditiesRequest.js";
import {logout} from "../../apis/AuthRequest.js";
import {toast, ToastContainer} from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {Modal} from 'react-bootstrap';
import * as PropTypes from "prop-types";

function JSONList(props) {
    return null;
}

JSONList.propTypes = {data: PropTypes.any};
const UserBody = () => {
    const [buyList, setBuyList] = useState([]);
    const [user, setUser] = useState({});
    const [purchasedList, setPurchasedList] = useState([]);

    useEffect(() => {
        fetchUser().then();
        fetchBuyList().then();
        fetchPurchasedList().then();
    }, []);

    const fetchUser = async () => {
        const response = await getUser(sessionStorage.getItem('username'));
        setUser(response.data)
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

    const fetchPurchasedList = async () => {
        const purchasedListResponse = await getPurchasedList(sessionStorage.getItem('username'));
        const commodities = await extractCommodities(purchasedListResponse.data);
        setPurchasedList(commodities);
    }

    const Info = () => {
        return (
            <>
                <div>
                    <img src="/images/user.png"/>
                    {user.username}
                </div>
                <div>
                    <img src="/images/email.png"/>
                    {user.email}
                </div>
                <div>
                    <img src="/images/date.png"/>
                    {user.birthDate}
                </div>
                <div>
                    <img src="/images/address.png"/>
                    {user.address}
                </div>
            </>
        )
    }

    const Credit = () => {
        const [amount, setAmount] = useState("");
        const [showModal, setShowModal] = useState(false);

        function handleAddCredit(e) {
            e.preventDefault();
            setShowModal(true);
        }

        function handleConfirm() {
            addCredit(user.username, {"amount": amount})
                .then(async (creditResponse) => {
                    await fetchUser();
                    setAmount("");
                    setShowModal(false);
                }).catch((error) => alert(error.response.data));
        }

        return (
            <>
                <div className="credit">
                    <div id="font">
                        <img src="/images/dollar.png"/>
                        {user.credit}
                    </div>
                    <div>
                        <input className="btn-font" id="amount-input" placeholder="$Amount" value={amount}
                               onChange={(event) => setAmount(event.target.value)}/>
                    </div>
                    <div>
                        <button id="credit-btn" className="btn btn-font" type="submit"
                                onClick={(e) => handleAddCredit(e)}>
                            Add More Credit
                        </button>
                    </div>
                </div>

                {showModal && (
                    <>
                        <div className="modal-overlay"/>
                        <div className="modal">
                            <Modal.Header className="modal-header">
                                <Modal.Title className="modal-title">Add Credit</Modal.Title>
                            </Modal.Header>
                            <Modal.Body className="modal-body">
                                Are you sure you want to add {amount}$ to your account?
                            </Modal.Body>
                            <Modal.Footer className="modal-footer">
                                <button onClick={() => setShowModal(false)}>Close</button>
                                <button className="btn btn-font" onClick={handleConfirm}>Confirm!</button>
                            </Modal.Footer>
                        </div>
                    </>
                )}
            </>
        )
    }

    const PayNowButton = () => {
        const [showModal, setShowModal] = useState(false);
        const [discountPercentage, setDiscountPercentage] = useState(0);

        const handlePayment = (e) => {
            e.preventDefault();
            setShowModal(true);
        }

        const handleFinalizePayment = () => {
            finalizePayment(user.username).then(async (response) => {
                await fetchUser();
                await fetchBuyList();
                await fetchPurchasedList();
                setDiscountPercentage(0);
                setShowModal(false);
            }).catch((error) => alert(error.response.data))

        }

        const BuyListItems = () => {
            return (
                <ul className="buylist-item-names">
                    {buyList.map((item, index) => (
                        <li key={index}>
                            {item.name} &nbsp; x{item.quantity}
                        </li>
                    ))}
                </ul>
            )
        }

        const BuyListPrices = () => {
            return (
                <ul className="buylist-prices-list">
                    {buyList.map((item, index) => (
                        <li key={index}>
                            {item.price * item.quantity}$
                        </li>
                    ))}
                </ul>
            )
        }


        const Discount = () => {
            const [discountCode, setDiscountCode] = useState("");

            useEffect(() => {
                const applyDiscount = async () => {
                    try {
                        const response = await applyDiscountCode(user.username, {"code": discountCode});
                        setDiscountPercentage(response.data);
                    } catch (error) {
                        alert("Invalid Discount Code");
                        setDiscountPercentage(0);
                    }
                };

                if (discountCode && user.username) {
                    applyDiscount().then();
                }
            }, [discountCode, user.username]);

            const handleSubmitDiscount = async (e) => {
                e.preventDefault();
                setDiscountCode(e.target.elements.code.value);
            };

            return (
                <div>
                    <form onSubmit={handleSubmitDiscount} className="discount">
                        <div>
                            <input className="btn-font" id="code-input" placeholder="Code" name="code"/>
                        </div>
                        <div>
                            <button
                                id={`${discountPercentage > 0 ? "submit-discount-btn-on" : "submit-discount-btn-off"}`}
                                className="btn btn-font" type="submit"
                                disabled={discountPercentage > 0}>
                                {discountPercentage > 0 ? "Submitted" : "Submit"}
                            </button>
                        </div>
                    </form>
                </div>
            );
        }

        const getBuylistPrice = () => {
            let price = 0;
            {
                buyList.map((item, index) => (
                    price += item.price * item.quantity
                ))
            }
            return price;
        }

        const TotalPrice = () => {
            const price = getBuylistPrice();
            return (
                <div className="total-price">
                    <div> total</div>
                    <div className={`${discountPercentage > 0 ? "discounted-price" : "price"}`}> {price}$</div>
                </div>
            )
        }

        const TotalPriceWithDiscount = () => {
            const price = getBuylistPrice();
            return (
                <div className="total-price">
                    <div> with discount</div>
                    <div className="price"> {price * (100 - discountPercentage) / 100}$</div>
                </div>
            )
        }

        const deleteCurrentDiscountCode = () => {
            deleteDiscountCode(user.username).then().catch((error) => console.log(error.response.data));
        }


        return (
            <>
                <div className="pay-section">
                    <button id="pay-btn" className="btn btn-font" onClick={(e) => handlePayment(e)}>
                        Pay Now!
                    </button>
                </div>
                {showModal && (
                    <>
                        <div className="modal-overlay"/>
                        <div className="modal">
                            <Modal.Header className="modal-header">
                                <Modal.Title className="modal-title">Your Cart</Modal.Title>
                            </Modal.Header>
                            <Modal.Body className="modal-body">
                                <div className="buylist-items">
                                    <BuyListItems/>
                                    <BuyListPrices/>
                                </div>
                                <Discount/>
                                <TotalPrice/>
                                {discountPercentage > 0 && <TotalPriceWithDiscount/>}
                            </Modal.Body>
                            <Modal.Footer className="modal-footer">
                                <button onClick={() => {
                                    setShowModal(false);
                                    deleteCurrentDiscountCode();
                                    setDiscountPercentage(0);
                                }}>Close
                                </button>
                                <button id="buy-btn" className="btn btn-font" onClick={handleFinalizePayment}>Buy!
                                </button>
                            </Modal.Footer>
                        </div>
                    </>
                )}
            </>


        )
    }

    const Logout = () => {

        const handleLogout = (e) => {
            e.preventDefault();
            logout().then((response) => {
                console.log(response.data);
                sessionStorage.removeItem('username');
                window.location.replace("/login");
                console.log("AFTER LOGOUT: " + sessionStorage.getItem('username'))
            })
        }

        return (
            <button id="logout-btn" className="btn btn-font" type="submit"
                    onClick={(e) => handleLogout(e)}>
                Logout
            </button>
        )

    }

    const UserInfo = () => {
        return (
            <div className="user-section">
                <div className="info font">
                    <Info/>
                    <Logout/>
                </div>
                <Credit/>
            </div>
        )
    }

    const InCart = ({commodity}) => {
        const handleAddToBuyList = (e) => {
            e.preventDefault();
            addToBuyList(sessionStorage.getItem('username'), {"id": commodity.id}).then(async (response) => {
                console.log("ADD TO BUY LIST");
                await fetchBuyList();
                buyList.forEach((item) => {
                    console.log(item.id + " ===> " + item.quantity)
                })
            }).catch((error) => alert(error.response.data))
        }

        const handleRemoveFromBuyList = (e) => {
            e.preventDefault();
            removeFromBuyList(sessionStorage.getItem('username'), {"id": commodity.id}).then(async (response) => {
                console.log("REMOVE FROM BUY LIST");
                await fetchBuyList();
            }).catch((error) => console.log("ERROR: " + error.data))
        }

        return (
            <div className="in-cart-btn">
                <div className="add-remove-buttons" onClick={(e) => handleRemoveFromBuyList(e)}>-</div>
                <div> {commodity.quantity}</div>
                <div className="add-remove-buttons" onClick={(e) => handleAddToBuyList(e)}>+</div>
            </div>
        )
    }

    const CommodityTableRow = ({commodity, tableType}) => {
        return (
            <div className="table-row">
                <a id="img-container" className="col-header-font" href={"/commodities/" + commodity.id}>
                    <img src={commodity.image}/>
                </a>
                <a className="col-font col-header-font" href={"/commodities/" + commodity.id}>
                    {commodity.name}
                </a>
                <div className="col-font col-header-font" style={{flexBasis: 2}}>
                    {commodity.categories.join(", ")}
                </div>
                <div className="col-font col-header-font">
                    ${commodity.price}
                </div>
                <div className="col-font col-header-font">
                    {commodity.providerId}
                </div>
                <div className="col-rating-font col-header-font">
                    {commodity.rating}
                </div>
                <div className="col-in-stock-font col-header-font">
                    {commodity.inStock}
                </div>
                <div className="col-font col-header-font">
                    {tableType === "Cart" && <InCart commodity={commodity}/>}
                    {tableType === "History" && commodity.quantity}
                </div>
            </div>
        )
    }

    const CommodityTableHeader = ({tableType}) => {
        return (
            <div className="table-row">
                <div className="col-header-font">
                    Image
                </div>
                <div className="col-header-font">
                    Name
                </div>
                <div className="col-header-font">
                    Categories
                </div>
                <div className="col-header-font">
                    Price
                </div>
                <div className="col-header-font">
                    Provider ID
                </div>
                <div className="col-header-font">
                    Rating
                </div>
                <div className="col-header-font">
                    In Stock
                </div>
                <div className="col-header-font">
                    {tableType === "Cart" && "In Cart"}
                    {tableType === "History" && "Quantity"}
                </div>
            </div>
        )
    }

    const EmptyTable = ({tableType}) => {
        return (
            <div id="empty-table" className="font">
                your {tableType.toLowerCase()} is empty
            </div>
        )
    }

    const CommoditiesTable = ({tableType, commoditiesList}) => {
        return (
            <div className="commodities-table">
                <CommodityTableHeader tableType={tableType}/>
                {commoditiesList.length > 0 ?
                    commoditiesList.map((item, index) => (
                        <CommodityTableRow key={index} commodity={item} tableType={tableType}/>
                    )) : <EmptyTable tableType={tableType}/>
                }
            </div>
        )
    }


    const CartLogo = () => {
        return (
            <div>
                <img src="/images/cart.png"/>
                <span className="topic-font">Cart</span>
            </div>
        )
    }

    const BuyList = () => {
        return (
            <>
                <CartLogo/>
                <CommoditiesTable tableType={"Cart"} commoditiesList={buyList}/>
                <PayNowButton/>
            </>
        )
    }

    const HistoryLogo = () => {
        return (
            <div>
                <img src="/images/history.png"/>
                <span className="topic-font">History</span>
            </div>
        )
    }

    const PurchasedList = () => {
        return (
            <>
                <HistoryLogo/>
                <CommoditiesTable tableType={"History"} commoditiesList={purchasedList}/>
            </>
        )
    }

    return (
        <main id="main-user">
            <UserInfo/>
            <BuyList/>
            <PurchasedList/>
        </main>
    )
}


const User = () => {
    const {username} = useParams();
    if (username !== sessionStorage.getItem('username')) {
        // toast.error('An error occurred while fetching data.', {autoClose: 5000, closeOnClick: true});
        // setTimeout(() => {
        //     window.location.replace("/login");
        // }, 2000);
        // alert("You don't have access")
        window.location.replace("/login") //TODO
    }
    return (
        <>
            <ToastContainer/>
            <Header searchBar={false} username={username}/>
            <UserBody/>
            <Footer/>
        </>
    )
}

export default User;