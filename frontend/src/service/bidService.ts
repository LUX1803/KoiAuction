import axiosInstance from "@/config/axios";

const API_URL = "http://localhost:8080/lot/";


export const getBidListByLotId = async (lotId: string) => {
   const response = await axiosInstance.get(`${API_URL}${lotId}/bid`);

   if (response.status == 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);

   }
}

export const getBidByLotIdAndBidderId = async (lotId: string, bidderId: string) => {
   const response = await axiosInstance.get(`${API_URL}${lotId}/bidder/${bidderId}`);

   if (response.status == 200) {
      console.log(response.data.data);
      return response.data.data;
   } if (response.status == 400) {
      return null;
   } else {
      console.error(`response code: ${response.status}`);

   }
}

export const getHighestBidByLotId = async (lotId) => {
   const response = await axiosInstance.get(`${API_URL}${lotId}/highest-bid`);

   if (response.status == 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);

   }
}


export const placeFixedPriceBid = async (lotId) => {
   const fixedPriceResponse = await axiosInstance.post(`${API_URL}${lotId}/fixed-price-bid`);

   if (fixedPriceResponse.status == 200) {
      console.log(fixedPriceResponse.data.data);
      return fixedPriceResponse.data.data;
   } else {
      console.error(`response code: ${fixedPriceResponse.status}`);
      throw new Error('Failed to fetch auction');
   }
}

export const placeSealedBid = async (lotId, bidAmount) => {
   const response = await axiosInstance.post(`${API_URL}${lotId}/sealed-bid`, {
      amount: bidAmount
   });

   if (response.status == 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}

export const placeDutchBid = async (lotId) => {
   const response = await axiosInstance.post(`${API_URL}${lotId}/dutch-bid`);

   if (response.status == 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}

export const buyLotNow = async (lotId) => {
   const response = await axiosInstance.post(`${API_URL}${lotId}/buy-now`);

   if (response.status == 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}


