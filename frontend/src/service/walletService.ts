import axiosInstance from "@/config/axios";

const API_URL = "http://localhost:8080/wallet";


export const getWalletBallance = async () => {
   const response = await axiosInstance.get(API_URL)

   if (response.status == 200) {
      // console.log(response.data);
      return response.data.data.balance;
   } else {
      console.error(`response code: ${response.status}`);
   }
}

export const addWalletBalance = async (balance: number) => {
   const response = await axiosInstance.post(
      API_URL+`/${balance}`
   )
   if (response.status == 200) {
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
   }
}

export const getUserPlacedBidsByLotId = async (lotId: number) => {
   const response = await axiosInstance.get(
      API_URL+`/${lotId}`
   )
   if (response.status == 200) {
      console.log("response.data.data", response.data.data);
      
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
   }
}