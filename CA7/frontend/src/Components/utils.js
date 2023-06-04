import {getCommodity} from "../APIs/CommoditiesRequest.js";
import {getBuyList} from "../APIs/UserRequest.js";

export const extractCommodities = async (data) => {
    const commodities = [];
    for (const [commodityId, quantity] of Object.entries(data)) {
        const CommodityResponse = await getCommodity(commodityId);
        const commodity = {...CommodityResponse.data, quantity: quantity};
        commodities.push(commodity);
    }
    return commodities;
}

export const fetchBuyList = async () => {
    const buyListResponse = await getBuyList(sessionStorage.getItem('username'));
    console.log(Object.keys(buyListResponse.data));
    return extractCommodities(buyListResponse.data);
}
