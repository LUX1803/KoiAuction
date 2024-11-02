import axiosInstance from "@/config/axios";
import { Lot } from "@/type/lot";

const API_URL = "http://localhost:8080/auction/";


export const getLotListByAuctionId = async (auctionId: number) => {
   const response = await axiosInstance.get(`${API_URL}${auctionId}/list`);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}

export const getLotById = async (auctionId: string, lotId: string) => {
   const response = await axiosInstance.get(`${API_URL}${auctionId}/${lotId}`);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}


export const createLot = async (lot: Lot) => {
   console.log({ ...lot });
   const response = await axiosInstance.post(`http://localhost:8080/lot`, { ...lot });

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}


export const updateLot = async (lot: Lot) => {

   const response = await axiosInstance.post(`http://localhost:8080/lot/${lot.lotId}/updateLot`, lot);
   console.log({ ...lot });
   if (response.status === 200) {
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}

export const getLotsByKoiId = async (koiId: number) => {
   const response = await axiosInstance.get(`http://localhost:8080/lot/koi/${koiId}`);

   if (response.status === 200) {
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}