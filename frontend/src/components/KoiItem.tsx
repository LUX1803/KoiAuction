import { NavLink } from "react-router-dom"

const KoiItem = (koi) => {
   return (
      <>
         <div className="block relative">
            <NavLink to={`/koi/${koi.id}`}>

               <span className="flex items-center font-semibold text-lg gap-2 ml-2 absolute text-black top-4 left-4">
                  <svg xmlns="http://www.w3.org/2000/svg" aria-hidden="true" role="img" width="1em" height="1em" viewBox="0 0 24 24" ><path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 11.172V5a2 2 0 0 1 2-2h6.172a2 2 0 0 1 1.414.586l8 8a2 2 0 0 1 0 2.828l-6.172 6.172a2 2 0 0 1-2.828 0l-8-8A2 2 0 0 1 3 11.172M7 7h.001"></path>
                  </svg>
                  {koi.id}
               </span>

               <div className="flex justify-center bg-[#E8F0FE] rounded-t-2xl">
                  <img className="transform scale-90 object-contain aspect-[2/3]  rounded-lg  relative rounded-t-2xl h-5/6" loading="lazy" src={koi.image} alt='Koi' />
               </div>

               <div className="bg-[#76bace5d] rounded-b-2xl px-4 py-4 flex justify-between">
                  <span className="text-2xl font-semibold">    {koi.varietyName}
                  </span>
                  <span className="block text-lg font-semibold">
                     {koi.status}
                  </span>
               </div>
            </NavLink>
         </div>
      </>
   )
}

export default KoiItem