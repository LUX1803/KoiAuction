import axiosInstance from "@/config/axios";
import { Koi } from "@/type/koi";

const API_URL = "http://localhost:8080/koi";

export const getKoiByStatus = async (status: string) => {
   const response = await axiosInstance.get(`${API_URL}/status/${status.toUpperCase()}`);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch koi');
   }
}

export const updateKoi = async (koi: Koi) => {
   console.log(koi);
   const response = await axiosInstance.patch(API_URL, { ...koi });

   if (response.status == 200) {
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to update koi status');
   }
}

export const createKoi = async (koi: Koi) => {
   const response = await axiosInstance.post(`${API_URL}`, koi);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to create koi');
   }
}

export const getFarmKoi = async () => {
   const response = await axiosInstance.get(`${API_URL}`);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch farm koi');
   }
}

export const getKoiById = async (id: number) => {
   const response = await axiosInstance.get(`${API_URL}/${id}`);

   if (response.status === 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
      throw new Error('Failed to fetch koi');
   }
}