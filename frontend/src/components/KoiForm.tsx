"use client";

import { useForm } from "react-hook-form";
import { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import {
   Form,
   FormControl,
   FormField,
   FormItem,
   FormLabel,
   FormMessage,
} from "./ui/form";
import { Input } from "./ui/input";
import { Card, CardHeader, CardTitle } from "./ui/card";
import { getVariety } from "@/service/varietyService";
import { Variety } from "@/type/variety.d";
import { createKoi } from "@/service/koiService";
import { getStorage, ref, uploadBytes, getDownloadURL } from "firebase/storage";
import { app } from "../config/firebase";
import { useNavigate } from "react-router-dom";

const initialFormState = {
   yob: "",
   length: "",
   gender: "",
   image: "",
   message: "",
   variety: "",
};

export function KoiForm() {
   const [varieties, setVarieties] = useState<Variety[]>([]);
   const storage = getStorage(app); // Initialize Firebase Storage
   const navigate = useNavigate();

   useEffect(() => {
      getVariety()
         .then((data) => {
            setVarieties(data);
         })
         .catch(console.error);
   }, []);

   const form = useForm({
      defaultValues: initialFormState,
   });

   const onSubmit = async (data: typeof initialFormState & { image: File }) => {


      // Upload image and get download URL
      if (data.image) {
         const imageUrl = await uploadImage(data.image);
         if (imageUrl) {
            const koi = {
               yob: data.yob,
               length: data.length,
               varietyId: data.variety,
               gender: data.gender,
               image: imageUrl,
               message: data.message,
            };

            createKoi(koi).then((data) => {
               navigate("/koi");
            });
         }
      }
   };

   const uploadImage = async (file: File) => {
      const storage = getStorage();
      const storageRef = ref(storage, `koi-images/${file.name}`);

      try {
         await uploadBytes(storageRef, file);
         const url = await getDownloadURL(storageRef);
         console.log("Image uploaded and available at:", url);
         return url;
      } catch (error) {
         console.error("Error uploading image:", error);
         return null;
      }
   };

   return (
      <Card className="bg-white w-full max-w-md mx-auto p-4 rounded-2xl">
         <CardHeader>
            <CardTitle className="text-2xl">Add Koi to Auction</CardTitle>
         </CardHeader>
         <Form {...form}>
            <form
               onSubmit={form.handleSubmit(onSubmit)}
               className="space-y-4 p-3"
            >
               <FormField
                  control={form.control}
                  name="yob"
                  render={({ field }) => (
                     <FormItem>
                        <FormLabel>Year of Birth (YOB)</FormLabel>
                        <FormControl>
                           <Input type="number" className="rounded-xl" {...field} />
                        </FormControl>
                        <FormMessage className="text-red-600" />
                     </FormItem>
                  )}
               />

               <FormField
                  control={form.control}
                  name="length"
                  render={({ field }) => (
                     <FormItem>
                        <FormLabel>Length (cm)</FormLabel>
                        <FormControl>
                           <Input type="number" className="rounded-xl" {...field} />
                        </FormControl>
                        <FormMessage className="text-red-600" />
                     </FormItem>
                  )}
               />

               <FormField
                  control={form.control}
                  name="gender"
                  render={({ field }) => (
                     <FormItem>
                        <FormLabel>Gender</FormLabel>
                        <FormControl>
                           <select
                              {...field}
                              className="rounded-xl w-full border-gray-300"
                           >
                              <option value="">Select Gender</option>
                              <option value="MALE">MALE</option>
                              <option value="FEMALE">FEMALE</option>
                              <option value="UNKNOWN">UNKNOWN</option>
                           </select>
                        </FormControl>
                        <FormMessage className="text-red-600" />
                     </FormItem>
                  )}
               />

               <FormField
                  control={form.control}
                  name="variety"
                  render={({ field }) => (
                     <FormItem>
                        <FormLabel>Variety</FormLabel>
                        <FormControl>
                           <select
                              {...field}
                              className="rounded-xl w-full border-gray-300"
                              onChange={(e) => {
                                 field.onChange(e.target.value);
                              }}
                           >
                              <option value="">Select variety</option>
                              {varieties.map((variety) => (
                                 <option key={variety.id} value={variety.id}>
                                    {variety.name}
                                 </option>
                              ))}
                           </select>
                        </FormControl>
                        <FormMessage className="text-red-600" />
                     </FormItem>
                  )}
               />

               <FormField
                  control={form.control}
                  name="image"
                  render={({ field }) => (
                     <FormItem>
                        <FormLabel>Upload Image</FormLabel>
                        <FormControl>
                           <Input
                              type="file"

                              onChange={(e) => {
                                 if (e.target.files && e.target.files[0]) {
                                    field.onChange(e.target.files[0]);
                                 }
                              }}
                           />
                        </FormControl>
                        <FormMessage className="text-red-600" />
                     </FormItem>
                  )}
               />

               <FormField
                  control={form.control}
                  name="message"
                  render={({ field }) => (
                     <FormItem>
                        <FormLabel>Message</FormLabel>
                        <FormControl>
                           <textarea
                              rows={3}
                              className="rounded-xl w-full border-black-300 border-2"
                              {...field}
                           />
                        </FormControl>
                        <FormMessage className="text-red-600" />
                     </FormItem>
                  )}
               />

               <Button className="bg-primary-background rounded-xl w-full !mt-5" type="submit">
                  Submit Koi
               </Button>
            </form>
         </Form>
      </Card>
   );
}
