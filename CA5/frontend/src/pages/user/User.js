import React, {useEffect, useState} from 'react';
import Header from "../../components/Header/Header.js";
import {useParams} from "react-router-dom";
import Footer from "../../components/Footer/Footer.js";
import "./User.css"
import {addCredit, getBuyList, getUser} from "../../apis/UserRequest.js";


const UserInfo = () => {
    const [user, setUser] = useState({});

    useEffect(() => { // TODO: hook other than useEffect?
        getUser(sessionStorage.getItem('username')).then(response => {
                setUser(response.data)
            }
        )
    }, []);


    const Info = () => {
        return (
            <div className="info font">
                <div>
                    <img src="/images/user.png" />
                    {user.username}
                </div>
                <div>
                    <img src="/images/email.png" />
                    {user.email}
                </div>
                <div>
                    <img src="/images/date.png" />
                    {user.birthDate}
                </div>
                <div>
                    <img src="/images/address.png" />
                    {user.address}
                </div>
            </div>
        )
    }

    const Credit = () => {
        const [amount, setAmount] = useState("");
        function handleAddCredit(e){
            e.preventDefault();
            addCredit(user.username, {"amount": amount})
                .then(response => {
                    console.log(response.data);
                    console.log("NEW CREDIT : " + user.credit);
                    setUser({ ...user, credit: parseFloat(user.credit) + parseFloat(amount) });
                    setAmount("");
                }).catch((error) => alert(error.response.data));
        }

        return (
            <div className="credit">
                <div id="font">
                    <img src="/images/dollar.png" />
                    {user.credit}
                </div>
                <div>
                    <input className="btn-font" id="amount-input" placeholder="$Amount" value={amount}
                           onChange={(event)=>setAmount(event.target.value)}/>
                </div>
                <div>
                    <button className="btn btn-font" type="submit"
                            onClick={(e) => handleAddCredit(e)}>
                        Add More Credit
                    </button>
                </div>
            </div>
        )
    }

    return (
        <div className="user-section">
            <Info />
            <Credit />
        </div>
    )
}

const BuyList = () => {
    const [buyList, setBuyList] = useState({});

    useEffect(() => {
        getBuyList(sessionStorage.getItem('username')).then(response => {
            const commodities = [];

            console.log("&&&  " + Object.entries(response.data).map(([key, value]) => {
                return {...key, quantity: value};
            }));

            const keys = Object.keys(response.data);
            for(let i in keys) {
                // console.log(keys[i].name + " %%%%% " + response.data[keys[i]]);
                commodities.push({...keys[i], quantity: response.data[keys[i]]});
            }
            setBuyList(commodities);
        })
    }, [])

    return(
        <>
            <div>
                <img src="/images/cart.png" />
                <span className="topic-font">Cart</span>
            </div>
            <div className="cart-table">
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
                        In Cart
                    </div>
                </div>
                <div className="table-row">
                    <div className="col-header-font">
                        <img src="/images/product.png" />
                    </div>
                    <div className="col-font col-header-font">
                        Galaxy S21
                    </div>
                    <div className="col-font col-header-font">
                        Technology, Phone
                    </div>
                    <div className="col-font col-header-font">
                        $21000000
                    </div>
                    <div className="col-font col-header-font">
                        1234
                    </div>
                    <div className="col-rating-font col-header-font">
                        8.3
                    </div>
                    <div className="col-in-stock-font col-header-font">
                        17
                    </div>
                    <div className="col-header-font">
                        <div className="in-cart-btn">
                            <a href="">-</a>
                            <div> 1</div>
                            <a href="">+ </a>
                        </div>
                    </div>
                </div>
                <div className="pay-btn">
                    <button className="btn btn-font">
                        Pay Now!
                    </button>
                </div>
            </div>
        </>
    )
}

const UserBody = () => {
    return(
        <main>
            <UserInfo/>
            <BuyList/>
        </main>
    )
}

const Test = () => {
    return (
        <main>
            <div>

                <div>
                    <div>
                        <img src="/images/history.png" />
                            <span className="topic-font">History</span>
                    </div>
                    <div className="cart-table">
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
                                Quantity
                            </div>
                        </div>
                        <div className="table-row">
                            <div className="col-header-font">
                                <img src="/images/spaghetti.png" />
                            </div>
                            <div className="col-font col-header-font">
                                Mom’s Spaghetti
                            </div>
                            <div className="col-font col-header-font">
                                Food
                            </div>
                            <div className="col-font col-header-font">
                                $60000
                            </div>
                            <div className="col-font col-header-font">
                                313
                            </div>
                            <div className="col-rating-font col-header-font">
                                10
                            </div>
                            <div className="col-in-stock-font col-header-font">
                                0
                            </div>
                            <div className="col-font col-header-font">
                                3
                            </div>
                        </div>
                        <div className="table-row">
                            <div className="col-header-font">
                                <img src="/images/microphone.png" />
                            </div>
                            <div className="col-font col-header-font">
                                Dre’s Microphone
                            </div>
                            <div className="col-font col-header-font">
                                Technology
                            </div>
                            <div className="col-font col-header-font">
                                $4200000
                            </div>
                            <div className="col-font col-header-font">
                                4321
                            </div>
                            <div className="col-rating-font col-header-font">
                                8.5
                            </div>
                            <div className="col-in-stock-font col-header-font">
                                22
                            </div>
                            <div className="col-font col-header-font">
                                1
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    )
}

const User = () => {
    const {username} = useParams();
    if (username !== sessionStorage.getItem('username')) {
        alert("You don't have access")
    }
    return (
        <>
            <Header searchBar={false} username={username}/>
            <UserBody/>
            <Footer/>
        </>
    )
}

export default User;