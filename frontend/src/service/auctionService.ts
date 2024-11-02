import axiosInstance from "@/config/axios";
import { Auction, AuctionStatus } from "@/type/auction";

const API_URL = "http://localhost:8080/auction/";


export const getCurrentAuction = async () => {
   const response = await axiosInstance.get(`${API_URL}current`);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}

export const getAuctionByStatus = async (status: string) => {
   const response = await axiosInstance.get(`${API_URL}status/${status}`);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}

export const getAuctionById = async (id: number) => {
   const response = await axiosInstance.get(`${API_URL}${id}`);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}

export const updateAuction = async (auction: Auction) => {
   const response = await axiosInstance.post(`${API_URL}${auction.id}/update`, auction);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}


export const startAuction = async (auctionId: number) => {
   const response = await axiosInstance.patch(`${API_URL}${auctionId}/UPCOMING`);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to start auction');
   }
}

export const stopAuction = async (auctionId: number) => {
   const response = await axiosInstance.patch(`${API_URL}${auctionId}/CLOSED`);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to start auction');
   }
}