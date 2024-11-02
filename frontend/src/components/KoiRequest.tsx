import React, { useEffect, useState } from "react";
import { Koi } from "../type/koi.d";
import { getKoiByStatus, updateKoi } from "@/service/koiService";
import LotForm from "./LotForm";
import { Dialog, DialogContent, DialogTrigger } from "./ui/dialog";
import { Link } from "react-router-dom";

import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogFooter, AlertDialogTitle, AlertDialogTrigger } from "./ui/alert-dialog";


export function KoiRequest() {
   const [koiList, setKoiList] = useState<Koi[]>([]);
   const [showFormKoiId, setShowFormKoiId] = useState<number | null>(null); // To track which koi's form is shown
   const [rejectReason, setRejectReason] = useState<string>("");

   useEffect(() => {
      loadKois();
   }, []);

   const loadKois = async () => {
      try {
         const pendingKois = await getKoiByStatus("PENDING");
         const queuedKois = await getKoiByStatus("QUEUED");

         const combinedData = [...pendingKois, ...queuedKois];

         // Sort the combined data by the 'sent' date
         const sortedData = combinedData.sort((a: Koi, b: Koi) => new Date(a.sent).getTime() - new Date(b.sent).getTime());

         setKoiList(sortedData);
      } catch (error) {
         console.error("Error loading kois:", error);
         // Handle error (e.g., show a message to the user)
      }
   };

   const onReject = async (id: number, rejectReason: string) => {
      const koi = koiList.find((koi) => koi.id == id);

      const updatedKoi = { ...koi, status: "REJECTED", note: rejectReason };

      // Send updated koi to the API
      await updateKoi(updatedKoi);

      // Reload the koi list after updating the status
      await loadKois();
   };

   const onCreateLot = (id: number) => {

      if (showFormKoiId === id) {
         setShowFormKoiId(null); // Close the form if clicked again
      } else {
         setShowFormKoiId(id); // Open the form for the selected koi
      }
   };

   return (
      <div className="p-4">
         <h2 className="text-2xl font-bold mb-4">Koi Auction Requests</h2>
         <div className="overflow-x-auto">
            <div className="min-w-max">
               {/* <!-- Header Row --> */}
               <div className="flex bg-gray-100 font-bold text-center">
                  <div className="w-40 p-2 border-r-2 border-gray-300">Farm Name</div>
                  <div className="w-40 p-2 border-r-2 border-gray-300">Message</div>
                  <div className="w-40 p-2 border-r-2 border-gray-300">Variety</div>
                  <div className="w-40 p-2 border-r-2 border-gray-300">Length (cm)</div>
                  <div className="w-40 p-2 border-r-2 border-gray-300">Gender</div>
                  <div className="w-40 p-2 border-r-2 border-gray-300">YOB</div>
                  <div className="w-40 p-2 border-r-2 border-gray-300">Image</div>
                  <div className="w-40 p-2">Action</div>
               </div>

               {/* <!-- Koi List Rows --> */}
               {koiList.map((koi) => (
                  <React.Fragment key={koi.id}>
                     <div className={`flex items-center text-center border-b`}>
                        <div className="w-40 p-2 border-r-2 border-gray-300">{koi.farmName}</div>
                        <div className="w-40 p-2 border-r-2 border-gray-300">{koi.message || "empty"}</div>
                        <div className="w-40 p-2 border-r-2 border-gray-300">{koi.varietyName}</div>
                        <div className="w-40 p-2 border-r-2 border-gray-300">{koi.length} cm</div>
                        <div className="w-40 p-2 border-r-2 border-gray-300">{koi.gender}</div>
                        <div className="w-40 p-2 border-r-2 border-gray-300">{koi.yob}</div>

                        <div className="w-40 p-2 border-r-2 border-gray-300"><Link target="_blank" to={koi.image}>...</Link></div>
                        <div className="w-50 p-2">
                           {(koi.status == 'PENDING' || koi.status == 'QUEUED') && (
                              <div className="flex space-x-2 justify-between">
                                 <Dialog>
                                    <DialogTrigger asChild><button
                                       onClick={() => onCreateLot(koi.id)}
                                       className="px-3 w-50 text-white bg-blue-500 rounded hover:bg-blue-600"
                                    >
                                       Create Lot
                                    </button></DialogTrigger>
                                    <DialogContent className="max-w-lg mx-auto bg-white p-6 rounded-md shadow-lg">
                                       <LotForm koiId={koi.id} koiStatus={koi.status} onLotCreated={loadKois} />
                                    </DialogContent>
                                 </Dialog>
                                 {
                                    koi.status == 'PENDING' && (
                                       <>
                                          <AlertDialog>
                                             <AlertDialogTrigger>
                                                <button
                                                   className="px-3  text-white bg-red-500 rounded hover:bg-red-600"
                                                >
                                                   Reject
                                                </button>
                                             </AlertDialogTrigger>
                                             <AlertDialogContent className="bg-white rounded-lg">
                                                <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
                                                <div>
                                                   <textarea
                                                      className="w-full p-2 mt-2 border border-gray-300 rounded"
                                                      placeholder="Enter rejection reason"
                                                      value={rejectReason}
                                                      onChange={(e) => setRejectReason(e.target.value)}
                                                   />
                                                </div>
                                                <AlertDialogFooter>
                                                   <AlertDialogCancel
                                                      className="border-gray-200 border-2 text-black"
                                                   >Cancel</AlertDialogCancel>
                                                   <AlertDialogAction
                                                      onClick={() => onReject(koi.id, rejectReason)} // Pass the reason when rejecting
                                                      className="border-red-200 border-2 text-red-600"
                                                   >Continue</AlertDialogAction>
                                                </AlertDialogFooter>
                                             </AlertDialogContent>

                                          </AlertDialog>
                                       </>
                                    )
                                 }


                              </div>
                           )}
                        </div>
                     </div>

                     {/* Form Row (Visible when a koi is selected) */}
                     {/* {showFormKoiId === koi.id && (
                        <div className="w-full bg-gray-50 p-4 border-t">
                           <LotForm koiId={koi.id} onLotCreated={loadKois} />
                        </div>
                     )} */}
                  </React.Fragment>
               ))}
            </div>
         </div>

      </div>
   );
}
