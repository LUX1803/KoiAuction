import { getFarmKoi } from "@/service/koiService";
import { Koi } from "@/type/koi.d";
import { useEffect, useState } from "react";
import KoiItem from "./KoiItem";
import { NavLink } from "react-router-dom";

const KoiList = () => {
   const [koiList, setKoiList] = useState<Koi[]>([]);

   useEffect(() => {
      getFarmKoi().then((data) => {

         setKoiList(data.sort((a: Koi, b: Koi) => new Date(b.sent).getTime() - new Date(a.sent).getTime()));
      });

   }, []);

   return (
      <>
         <div className="sticky left-5">
            <NavLink to='/koi/new'>
               <button className="bg-blue-500 hover:bg-blue-700 text-white
               font-bold py-2 px-4 rounded-2xl shadow-md border-2 border-gray-200">
                  Submit Koi
               </button>
            </NavLink>
         </div>

         <div className='grid grid-cols-3 gap-10 py-8 px-2 mt-8'>
            {koiList?.map((koi) => (
               <KoiItem {...koi} />
            ))}
         </div>

      </>
   )
}

export default KoiList