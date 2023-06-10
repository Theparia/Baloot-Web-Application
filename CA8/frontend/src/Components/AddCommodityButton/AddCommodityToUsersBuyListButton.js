import {addToBuyList, removeFromBuyList} from "../../APIs/UserRequest.js";
import {fetchBuyList} from "../utils.js";
import React, {useEffect, useState} from "react";

export function AddCommodityToUsersBuyListButton(props) {
    const handleAddToBuyList = (e) => {
        e.preventDefault();
        addToBuyList(localStorage.getItem('username'), {"id": props.commodity.id}).then(async (response) => {
            await fetchBuyList().then(response => {
                    props.setBuyList(response);
                }
            );
        }).catch((error) => alert(error.response.data))
    }

    const handleRemoveFromBuyList = (e) => {
        e.preventDefault();
        removeFromBuyList(localStorage.getItem('username'), {"id": props.commodity.id}).then(async (response) => {
            await fetchBuyList().then(response => {
                    props.setBuyList(response);
                }
            );
        }).catch((error) => alert(error.response.data))
    }

    const isCommodityInBuyList = props.buyList.some((item) => item.id === props.commodity.id);
    const buyListItem = props.buyList.filter(item => item.id === props.commodity.id).find(item => item);

    if (props.commodity.inStock <= 0) {
        return (
            <button className="add-to-cart-button-disabled" type="button" disabled={true}>add to cart</button>
        )
    }

    if (isCommodityInBuyList) {
        return (
            <div className="add-remove-button-home">
                <div className="add-remove-buttons" onClick={(e) => handleRemoveFromBuyList(e)}>-</div>
                <div> {buyListItem.quantity}</div>
                <div className="add-remove-buttons" onClick={(e) => handleAddToBuyList(e)}>+</div>
            </div>
        )
    } else {
        return (
            <button className="add-to-cart-button-home" type="button" onClick={(e) => handleAddToBuyList(e)}>add to
                cart</button>
        )
    }
}
