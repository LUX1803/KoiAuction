import axiosInstance from "@/config/axios";
import qs from 'qs';

const API_URL = "http://localhost:8080/payment/";

export const payByVNPay = async (transactionIds: number[]) => {
   const response = await axiosInstance.get(`${API_URL}vnpay`,
      {
         params: { transactionIds },
         paramsSerializer: params => {
            return qs.stringify(params, { arrayFormat: 'repeat' });
         }
      });

   if (response.status == 200) {
      console.log(response.data.paymentUrl);
      return response.data.paymentUrl;
   } else {
      console.error(`response code: ${response.status}`);
   }
}

export const payByWallet = async (transactionIds: number[]) => {
   const response = await axiosInstance.post(`${API_URL}wallet`,
      transactionIds,
      {
         headers: {
            'Content-Type': 'application/json'
         }
      });

   if (response.status == 200) {
      return response.data.paymentUrl;
   } else {
      console.error(`response code: ${response.status}`);
   }
}