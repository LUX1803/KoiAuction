// services/loginService.js
import axiosInstance from "@/config/axios";

const API_URL = "http://localhost:8080/signin";

export const signin = async (username: string, password: string) => {

  const response = await axiosInstance.post(API_URL, {
    username: username,
    password: password
  });

  if (response.status === 200) {
    return response.data.data.token;
  } else {
    console.error(`Unexpected response code: ${response.status}`);
    throw new Error('Login failed');
  }

};
