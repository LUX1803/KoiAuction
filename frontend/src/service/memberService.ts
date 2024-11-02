import axiosInstance from "@/config/axios";

const API_URL = "http://localhost:8080/";


export const getRecepient = async (koiId: number) => {
   const response = await axiosInstance.get(`http://localhost:8080/recipient/koi/${koiId}`);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}