import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { z } from "zod"

import { Button } from "@/components/ui/button"
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { Card, CardHeader, CardTitle } from "./ui/card"
import { useNavigate } from "react-router-dom"
import { useState } from "react"
import { useAuth } from "@/context/AuthContext"
import { signin } from "@/service/loginService"
import { jwtDecode } from "jwt-decode";
import { CustomJwtPayload } from "@/type/jwt"
import { Role } from "@/type/member"



const formSchema = z.object({
  username: z.string(),
  password: z.string(),
})


export default function LoginForm() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [error, setError] = useState<string | null>(null);

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      username: "",
      password: "",
    },
  })

  async function onSubmit(values: z.infer<typeof formSchema>) {
    try {
      const token = await signin(values.username, values.password);
      const user = jwtDecode(token) as CustomJwtPayload;
      login({
        id: user.id,
        role: user.scope as Role,
        firstname: user.firstname,
        lastname: user.lastname
      });
      localStorage.setItem("token", token);
      if (user.scope == "STAFF") {
        navigate("/auction-setup")
      } else if (user.scope == "SHIPPER") {
        navigate("/shipping")
      }
      else {
        navigate("/about")
      }

    } catch (error) {
      console.log(error);
      setError("Incorrect username or password");
    }


  }

  return (
    <Card className="bg-white w-full max-w-sm mx-auto p-4 rounded-2xl">
      <CardHeader>
        <CardTitle className="text-2xl">Sign In</CardTitle>
      </CardHeader>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-1 p-3">
          {error && <div className="text-red-500">{error}</div>}
          <FormField
            control={form.control}
            name="username"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Username</FormLabel>
                <FormControl>
                  <Input className="rounded-xl" {...field} required />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Password</FormLabel>
                <FormControl>
                  <Input className="rounded-xl" type="password" {...field} required />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <Button className="bg-primary-background rounded-xl w-full !mt-5" type="submit">Sign in</Button>
        </form>
      </Form>
    </Card>
  )
};
