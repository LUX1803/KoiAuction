"use client"

import { z } from "zod"

export const formSchema = z.object({
   username: z.string().min(2).max(50),
   password: z.string().min(8).max(50),
   email: z.string().email(),
   firstName: z.string().min(2).max(20),
   lastName: z.string().min(2).max(20),
   address: z.string().min(5).max(50),
})
