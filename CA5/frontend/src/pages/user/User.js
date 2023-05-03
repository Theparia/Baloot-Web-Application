import React, {useEffect, useState} from 'react';
import Header from "../../components/Header/Header.js";
import {useParams} from "react-router-dom";
import Footer from "../../components/Footer/Footer.js";
import "./User.css"
import {addCredit, getBuyList, getPurchasedList, getUser} from "../../apis/UserRequest.js";
import {getCommodity} from "../../apis/CommoditiesRequest.js";
import {logout} from "../../apis/AuthRequest.js";
import { toast, ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';


const Logout = ({username}) => {

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
    const [user, setUser] = useState({});

    useEffect(() => {
        getUser(sessionStorage.getItem('username')).then(response => {
                setUser(response.data);
            }
        )
    }, []);


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

        function handleAddCredit(e) {
            e.preventDefault();
            addCredit(user.username, {"amount": amount})
                .then(async (creditResponse) => {
                    const userResponse = await getUser(sessionStorage.getItem('username'));
                    setUser(userResponse.data)
                    // console.log("NEW CREDIT : " + user.credit); //TODO: WHAT?
                    // setUser({ ...user, credit: parseFloat(user.credit) + parseFloat(amount) });
                    setAmount("");
                }).catch((error) => alert(error.response.data));
        }

        return (
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
        )
    }

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
    return (
        <div className="in-cart-btn">
            <a href="">-</a>
            <div> {commodity.quantity}</div>
            <a href="">+ </a>
        </div>
    )
}

const CommodityTableRow = ({commodity, tableType}) => {
    return (
        <div className="table-row">
            <div id="img-container" className="col-header-font">
                <img src={commodity.image}/>
            </div>
            <div className="col-font col-header-font">
                {commodity.name}
            </div>
            <div className="col-font col-header-font">
                {commodity.categories}
            </div>
            <div className="col-font col-header-font">
                {commodity.price}
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

const CommoditiesTable = ({tableType, commoditiesList}) => {
    return (
        <div className="commodities-table">
            <CommodityTableHeader tableType={tableType}/>
            {
                commoditiesList.map((item, index) => (
                    <CommodityTableRow key={index} commodity={item} tableType={tableType}/>
                ))
            }
        </div>
    )
}

const BuyList = () => {
    const [buyList, setBuyList] = useState([]);

    useEffect(() => {
        getBuyList(sessionStorage.getItem('username')).then(async (buyListResponse) => {
            const commodities = [];
            for (const [commodityId, quantity] of Object.entries(buyListResponse.data)) {
                const CommodityResponse = await getCommodity(commodityId);
                const commodity = {...CommodityResponse.data, quantity: quantity};
                commodities.push(commodity);
            }
            setBuyList(commodities);
        });
    }, []);


    const PayNowButton = () => { //TODO: ZERO SIZE
        return (
            <div className="pay-section">
                <button id="pay-btn" className="btn btn-font">
                    Pay Now!
                </button>
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

    return (
        <>
            <CartLogo/>
            <CommoditiesTable tableType={"Cart"} commoditiesList={buyList}/>
            <PayNowButton/>
        </>
    )
}

const PurchasedList = () => {
    const [purchasedList, setPurchasedList] = useState([]);

    useEffect(() => {
        getPurchasedList(sessionStorage.getItem('username')).then(async (purchasedListResponse) => {
            const commodities = [];
            for (const [commodityId, quantity] of Object.entries(purchasedListResponse.data)) {
                const CommodityResponse = await getCommodity(commodityId);
                const commodity = {...CommodityResponse.data, quantity: quantity};
                commodities.push(commodity);
            }
            setPurchasedList(commodities);
        });
    }, []);

    const HistoryLogo = () => {
        return (
            <div>
                <img src="/images/history.png"/>
                <span className="topic-font">History</span>
            </div>
        )
    }

    return (
        <>
            <HistoryLogo/>
            <CommoditiesTable tableType={"History"} commoditiesList={purchasedList}/>
        </>
    )
}

const UserBody = () => {
    return (
        <main>
            <UserInfo/>
            <BuyList/>
            <PurchasedList/>
        </main>
    )
}


const User = () => {
    const {username} = useParams();
    if (username !== sessionStorage.getItem('username')) {
        toast.error('An error occurred while fetching data.', { autoClose: 5000, closeOnClick: true });
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