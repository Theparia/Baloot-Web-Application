import React from "react";
import {AddCommodityToUsersBuyListButton} from "../AddCommodityButton/AddCommodityToUsersBuyListButton.js";

function Card(props) {
    return (
        <div className="product-card" id="suggested-product-card">
            <a href={"/commodities/" + props.commodity.id}>
                <div className="product-title">
                    {props.commodity.name}
                </div>
            </a>
            <div className="product-inStock">
                <span> {props.commodity.inStock} </span> left in stock
            </div>
            <a href={"/commodities/" + props.commodity.id}>
                <img className="w-100" src={props.commodity.image} alt="ProductImage"/>
            </a>
            <div className="product-price">
                <div>
                    <span>{props.commodity.price}</span>$
                </div>
                <AddCommodityToUsersBuyListButton commodity={props.commodity} buyList={props.buyList} setBuyList={props.setBuyList}/>
            </div>
        </div>
    )
}

export default Card;
