import axiosInstance from "@/config/axios";

const API_URL = "http://localhost:8080/variety";

export const getVariety = async () => {
   const response = await axiosInstance.get(API_URL);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch auction');
   }
}