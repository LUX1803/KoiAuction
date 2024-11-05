import { getWalletBallance } from "@/service/walletService";
import { CustomJwtPayload } from "@/type/jwt";
import { Role } from "@/type/member";
import { jwtDecode } from "jwt-decode";
import { createContext, useState, ReactNode, useContext, useEffect } from "react";

interface User {
  id: number;
  firstname: string;
  lastname: string;
  role: Role;
}

// Define the context type
interface AuthContextType {
  user: User | null;
  login: (userData: User) => void;
  logout: () => void;
  balance: number;
  getCurrentBallance: () => void
}

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

// Create the AuthContext with default values
export const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Create a provider component
export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<User | null>(null);
  const [balance, setBalance] = useState<number>(0);

  useEffect(() => {
    const token = localStorage.getItem('token');

    if (token) {
      const data = jwtDecode(token) as CustomJwtPayload;

      if (data && data.exp && data.exp * 1000 > Date.now()) {
        login({
          id: data.id,
          role: data.scope as Role,
          firstname: data.firstname,
          lastname: data.lastname
        });
      } else {
        logout();
        localStorage.removeItem('token');
      }
    }
  }, []);

  useEffect(() => {
    //getMoney
    if (user) {
      getCurrentBallance()
    }
  }, [user])

  // Function to handle login
  const login = (userData: User) => {
    setUser(userData);
  };

  // Function to handle logout
  const logout = () => {
    setUser(null);
  };

  const getCurrentBallance = () => {
    user && getWalletBallance().then((data) => {
      console.log("DA TRU TIEN: ", data);
      
      setBalance(data);
    })
  }

  return (
    <AuthContext.Provider value={{ user, login, logout, balance, getCurrentBallance }}>
      {children}
    </AuthContext.Provider>
  );
};



