// services/loginService.js
import axiosInstance from "@/config/axios";

const API_URL = "http://localhost:8080/signup";

export const register = async (firstname: string, lastname: string, email: string, username: string, password: string) => {
   console.log(firstname, lastname, email, username, password);
   try {
      const response = await axiosInstance.post(API_URL, {
         firstname: firstname,
         lastname: lastname,
         email: email,
         username: username,
         password: password
      });

      if (response.status === 200) {
         return;
      } else {
         console.error(`Unexpected response code: ${response.status}`);
         throw new Error('Register failed');
      }

   } catch (error) {
      // Handle error appropriately
      console.error("Register error", error);
      throw error; // Rethrow the error to handle it in the component
   }
};
