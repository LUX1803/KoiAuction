import axiosInstance from "@/config/axios";

const API_URL = "http://localhost:8080/invoice/";


export const getInvoiceByLotId = async (lotId: string) => {
   const response = await axiosInstance.get(`${API_URL}lot/${lotId}`);
   if (response.status == 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
   }
}

export const getInvoiceById = async (invoiceId: string) => {
   const response = await axiosInstance.get(`${API_URL}${invoiceId}`);
   if (response.status == 200) {
      console.log(response.data.data);
      return response.data.data;
   } else {
      console.error(`response code: ${response.status}`);
   }
}