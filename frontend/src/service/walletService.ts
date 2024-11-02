import axiosInstance from "@/config/axios";

const API_URL = "http://localhost:8080/wallet";


export const getWalletBallance = async () => {
   const response = await axiosInstance.get(API_URL)

   if (response.status == 200) {
      console.log(response.data);
      return response.data.data.balance;
   } else {
      console.error(`response code: ${response.status}`);
   }
}