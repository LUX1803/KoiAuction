import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "@/context/AuthContext";
import logo from '../assets/koi-fish-icon.svg';
import { Button } from "./ui";
import {
   DropdownMenu,
   DropdownMenuContent,
   DropdownMenuItem,
   DropdownMenuLabel,
   DropdownMenuSeparator,
   DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { useEffect, useState } from "react";
import { formatMoney } from "@/util/helper";

function Header() {
   const { user, logout, balance } = useAuth();
   // const [ballance, setBallance] = useState(null);
   const navigate = useNavigate();

   function handleLogout() {
      logout();
      localStorage.removeItem("token");
      navigate("/signin");
   }

   useEffect(() => {
      // if (user && user.role == 'BREEDER') {
      //    getWalletBallance().then((data) => {
      //       setBallance(data);
      //    })
      // }
      // if (user) {
      //    getWalletBallance().then((data) => {
      //       setBallance(data);
      //    })
      // }
   }, [user])
   return (

      <div className="flex items-center justify-between max-w-7xl mx-auto">
         {/* Left - Logo */}
         <NavLink to="/" className="flex items-center space-x-2">
            <img src={logo} alt="logo" className="w-10 h-10" />
            <span className="text-xl font-bold">KOIAUCTION</span>
         </NavLink>

         {/* Center - Navigation Links */}
         <div className="flex space-x-16">
            <NavLink
               to="/"
               className={({ isActive }) =>
                  `flex items-center justify-center w-28 space-x-2 ${isActive ? 'bg-[#6ba5cc] rounded-full px-5 py-2' : ''} `
               }
            >
               {({ isActive }) => (
                  <div className="flex items-center space-x-2">
                     <svg
                        className="w-6 h-6"
                        fill={isActive ? 'white' : 'black'}
                        viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
                        <g id="_12" data-name="12"><path id="_12-2" d="m218 471.16h-103.27a61 61 0 0 1 -60.81-60.59v-205.34a37.92 37.92 0 0 1 13.69-28.95l18.18-13.46a20.2 20.2 0 0 0 -23.34-32.09l-18.63 13.91a78.07 78.07 0 0 0 -30.29 60.59v205.56a101.42 101.42 0 0 0 101.2 101.21h103.27a20.2 20.2 0 1 0 0-40.39z" data-name="12-2" /><path id="_12-1" d="m498.47 204.78a78.76 78.76 0 0 0 -29.39-60.59l-155.08-123.65a93.13 93.13 0 0 0 -116.69 0l-49.14 35.68a20.58 20.58 0 0 0 24 33.44l49.83-37.25a53 53 0 0 1 67.32 0l155.07 123.65a37.88 37.88 0 0 1 14.36 29.17v205.56a60.59 60.59 0 0 1 -60.59 60.59h-46a8.53 8.53 0 0 1 -8.53-8.53v-92.68a53.63 53.63 0 0 0 -53.41-53.63h-67.32a53.62 53.62 0 0 0 -53.63 53.63v35.46a20.2 20.2 0 1 0 40.39 0v-35.46a13 13 0 0 1 13-13.24h67.34a13 13 0 0 1 13 13v92.9a49.15 49.15 0 0 0 49.15 48.93h44.85a101.21 101.21 0 0 0 101.25-101.19z" data-name="12-1" /></g>
                     </svg>
                     <span className={`${isActive ? 'text-white' : 'text-black'} text-lg`}>Home</span>
                  </div>
               )}
            </NavLink>

            <NavLink
               to="/auction"
               className={({ isActive }) =>
                  `flex items-center justify-center w-28 space-x-2 ${isActive ? 'bg-[#6ba5cc] rounded-full px-5 py-2' : ''}`
               }
            >
               {({ isActive }) => (
                  <div className="flex items-center space-x-2">
                     <svg
                        className="w-6 h-6"
                        fill={isActive ? 'white' : 'black'}
                        viewBox="0 0 50 50" xmlns="http://www.w3.org/2000/svg">
                        <g><path d="m27.23979 22.21756c.63927.65978 1.2579 1.31956 1.86596 1.9896l4.58764-4.59771c-.67023-.60825-1.32988-1.22676-1.98979-1.85558z" /><path d="m45.77509 29.73275c-9.14833-7.57117-9.07914-7.44416-10.92723-9.09241l-4.71122 4.72147c1.30027 1.4378 2.10738 2.48051 9.08216 10.92736 1.75245 2.10299 4.93798 2.26796 6.88624.31957 1.9382-1.93807 1.78367-5.12354-.32995-6.87599z" /><path d="m18.311 9h11.94v11.59h-11.94z" transform="matrix(.707 -.707 .707 .707 -3.35 21.503)" /><path d="m34.23098 14.11129c.40244.40256 1.05505.40256 1.45774 0l2.08291-2.08285c.40269-.40263.40269-1.0553 0-1.45792l-9.26866-9.26859c-.40244-.40256-1.0553-.40256-1.45799 0l-2.08266 2.08285c-.40269.40263-.40269 1.0553 0 1.45792z" /><path d="m20.05908 28.283c.40269.40263 1.0553.40263 1.45799 0l2.08291-2.08278c.40244-.40263.40244-1.05536 0-1.45793l-9.26866-9.26859c-.40269-.40256-1.0553-.40256-1.45799 0l-2.08291 2.08285c-.40244.40256-.40244 1.0553 0 1.45786z" /><path d="m28.79669 42.45384h-24.64863c-.81444 0-1.4741.65978-1.4741 1.47416v3.59784c0 .81438.65966 1.47416 1.4741 1.47416h24.64863c.81419 0 1.47409-.65978 1.47409-1.47416v-3.59784c0-.81438-.6599-1.47416-1.47409-1.47416z" /><path d="m9.65207 35.83633c-1.82318 0-3.3013 1.478-3.3013 3.30117v1.76881h20.22785v-1.76881c0-1.82318-1.47787-3.30117-3.30105-3.30117z" /><path d="m7.24273 29.77088c.26986.32953.75771.37871 1.08776.10772.33071-.27081.37903-.75706.10822-1.08827l-2.16345-2.64164c-.2698-.3292-.75756-.37853-1.08777-.10772-.33071.27081-.37903.75706-.10822 1.08827z" /><path d="m3.07087 32.32595 2.90792.93927c.41307.13155.84394-.09729.9735-.49833.13087-.40672-.09211-.84162-.49833-.9735l-2.90792-.93927c-.40672-.12785-.84263.09262-.9735.49833-.13088.40671.09211.84162.49833.9735z" /><path d="m10.23169 27.43025c.10922.39952.52098.65615.94984.54262.41175-.11275.65437-.53759.54212-.94934l-.8527-3.12185c-.11275-.41276-.53709-.65135-.94984-.54262-.41174.11275-.65436.53759-.54211.94934z" /></g>
                     </svg>
                     <span className={`${isActive ? 'text-white' : 'text-black'} text-lg`}>Auction</span>
                  </div>
               )}
            </NavLink>

            <NavLink
               to="/about"
               className={({ isActive }) =>
                  `flex items-center justify-center w-28 space-x-2 ${isActive ? 'bg-[#6ba5cc] rounded-full px-5 py-2' : ''}`
               }
            >
               {({ isActive }) => (
                  <div className="flex items-center space-x-2">
                     <svg
                        viewBox="0 0 42 42"
                        className="w-6 h-6"
                        fill={isActive ? 'white' : 'black'}
                        xmlns="http://www.w3.org/2000/svg">
                        <path d="m24 8a16 16 0 1 0 16 16 16.0212 16.0212 0 0 0 -16-16zm0 8a2 2 0 1 1 -2 2 2.0059 2.0059 0 0 1 2-2zm2 16h-2a2.0059 2.0059 0 0 1 -2-2v-4a2 2 0 0 1 0-4h2a2.0059 2.0059 0 0 1 2 2v4a2 2 0 0 1 0 4z" />
                     </svg>
                     <span className={`${isActive ? 'text-white' : 'text-black'} text-lg`}>About</span>
                  </div>
               )}
            </NavLink>
         </div>


         {/* Right - Signin/Signup Links */}
         <div className="flex space-x-6">
            {user ? (
               <div className="flex space-x-6 ">
                  <div className="bg-black text-white rounded-full w-38 px-6 py-2">
                     <DropdownMenu>
                        <DropdownMenuTrigger className="flex gap-2"> <svg
                           className="w-4 h-4 mt-1"
                           xmlns="http://www.w3.org/2000/svg" viewBox="-42 0 512 512.001" fill="white"><
                              path d="m210.351562 246.632812c33.882813 0 63.21875-12.152343 87.195313-36.128906 23.96875-23.972656 36.125-53.304687 36.125-87.191406 0-33.875-12.152344-63.210938-36.128906-87.191406-23.976563-23.96875-53.3125-36.121094-87.191407-36.121094-33.886718 0-63.21875 12.152344-87.191406 36.125s-36.128906 53.308594-36.128906 87.1875c0 33.886719 12.15625 63.222656 36.128906 87.195312 23.980469 23.96875 53.316406 36.125 87.191406 36.125zm-65.972656-189.292968c18.394532-18.394532 39.972656-27.335938 65.972656-27.335938 25.996094 0 47.578126 8.941406 65.976563 27.335938 18.394531 18.398437 27.339844 39.980468 27.339844 65.972656 0 26-8.945313 47.578125-27.339844 65.976562-18.398437 18.398438-39.980469 27.339844-65.976563 27.339844-25.992187 0-47.570312-8.945312-65.972656-27.339844-18.398437-18.394531-27.34375-39.976562-27.34375-65.976562 0-25.992188 8.945313-47.574219 27.34375-65.972656zm0 0" /><path d="m426.128906 393.703125c-.691406-9.976563-2.089844-20.859375-4.148437-32.351563-2.078125-11.578124-4.753907-22.523437-7.957031-32.527343-3.3125-10.339844-7.808594-20.550781-13.375-30.335938-5.769532-10.15625-12.550782-19-20.160157-26.277343-7.957031-7.613282-17.699219-13.734376-28.964843-18.199219-11.226563-4.441407-23.667969-6.691407-36.976563-6.691407-5.226563 0-10.28125 2.144532-20.042969 8.5-6.007812 3.917969-13.035156 8.449219-20.878906 13.460938-6.707031 4.273438-15.792969 8.277344-27.015625 11.902344-10.949219 3.542968-22.066406 5.339844-33.042969 5.339844-10.96875 0-22.085937-1.796876-33.042968-5.339844-11.210938-3.621094-20.300782-7.625-26.996094-11.898438-7.769532-4.964844-14.800782-9.496094-20.898438-13.46875-9.753906-6.355468-14.808594-8.5-20.035156-8.5-13.3125 0-25.75 2.253906-36.972656 6.699219-11.257813 4.457031-21.003906 10.578125-28.96875 18.199219-7.609375 7.28125-14.390625 16.121094-20.15625 26.273437-5.558594 9.785157-10.058594 19.992188-13.371094 30.339844-3.199219 10.003906-5.875 20.945313-7.953125 32.523437-2.0625 11.476563-3.457031 22.363282-4.148437 32.363282-.679688 9.777344-1.023438 19.953125-1.023438 30.234375 0 26.726562 8.496094 48.363281 25.25 64.320312 16.546875 15.746094 38.4375 23.730469 65.066406 23.730469h246.53125c26.621094 0 48.511719-7.984375 65.0625-23.730469 16.757813-15.945312 25.253906-37.589843 25.253906-64.324219-.003906-10.316406-.351562-20.492187-1.035156-30.242187zm-44.90625 72.828125c-10.933594 10.40625-25.449218 15.464844-44.378906 15.464844h-246.527344c-18.933594 0-33.449218-5.058594-44.378906-15.460938-10.722656-10.207031-15.933594-24.140625-15.933594-42.585937 0-9.59375.316406-19.066407.949219-28.160157.617187-8.921874 1.878906-18.722656 3.75-29.136718 1.847656-10.285156 4.199219-19.9375 6.996094-28.675782 2.683593-8.378906 6.34375-16.675781 10.882812-24.667968 4.332031-7.617188 9.316407-14.152344 14.816407-19.417969 5.144531-4.925781 11.628906-8.957031 19.269531-11.980469 7.066406-2.796875 15.007812-4.328125 23.628906-4.558594 1.050781.558594 2.921875 1.625 5.953125 3.601563 6.167969 4.019531 13.277344 8.605469 21.136719 13.625 8.859375 5.648437 20.273437 10.75 33.910156 15.152344 13.941406 4.507812 28.160156 6.796875 42.273437 6.796875 14.113282 0 28.335938-2.289063 42.269532-6.792969 13.648437-4.410156 25.058594-9.507813 33.929687-15.164063 8.042969-5.140624 14.953125-9.59375 21.121094-13.617187 3.03125-1.972656 4.902344-3.042969 5.953125-3.601563 8.625.230469 16.566406 1.761719 23.636719 4.558594 7.636719 3.023438 14.121093 7.058594 19.265625 11.980469 5.5 5.261719 10.484375 11.796875 14.816406 19.421875 4.542969 7.988281 8.207031 16.289062 10.886719 24.660156 2.800781 8.75 5.15625 18.398438 7 28.675782 1.867187 10.433593 3.132812 20.238281 3.75 29.144531v.007812c.636719 9.058594.957031 18.527344.960937 28.148438-.003906 18.449219-5.214844 32.378906-15.9375 42.582031zm0 0" />
                        </svg>
                           {user?.firstname + ' ' + user?.lastname}</DropdownMenuTrigger>
                        <DropdownMenuContent className="bg-white mt-5 rounded-b-2xl">
                           <DropdownMenuSeparator />

                           {
                              user.role == 'BREEDER' && (
                                 <>

                                    <DropdownMenuItem>
                                       <NavLink to="/koi" >
                                          Koi
                                       </NavLink>
                                    </DropdownMenuItem>
                                 </>
                              )
                           }
                           {
                              (user.role == 'BIDDER' || user.role == 'BREEDER') && (
                                 <>
                                    <DropdownMenuItem>
                                       <NavLink to="/billing" >
                                          Billing
                                       </NavLink>
                                    </DropdownMenuItem>
                                    <DropdownMenuItem>
                                       <NavLink to="/transaction" >
                                          Transaction
                                       </NavLink>
                                    </DropdownMenuItem>
                                    <DropdownMenuItem>
                                       <NavLink to="/wallet">
                                          Ballance: {formatMoney(balance ? balance : 0)}
                                       </NavLink>
                                    </DropdownMenuItem>
                                 </>
                              )
                           }

                           {
                              user.role == 'SHIPPER' && (
                                 <>
                                    <DropdownMenuItem>
                                       <NavLink to="/shipping" >
                                          Shipping
                                       </NavLink>
                                    </DropdownMenuItem>
                                 </>
                              )
                           }

                           {
                              user.role == 'STAFF' && (
                                 <>
                                    <DropdownMenuItem>
                                       <NavLink to="/koi-review" >
                                          Koi Review
                                       </NavLink>
                                    </DropdownMenuItem>
                                    <DropdownMenuItem>
                                       <NavLink to="/auction-setup" >
                                          Auction Setup
                                       </NavLink>
                                    </DropdownMenuItem>
                                 </>
                              )
                           }

                           <DropdownMenuLabel>
                              <Button onClick={handleLogout}>Logout</Button>
                           </DropdownMenuLabel>
                        </DropdownMenuContent>
                     </DropdownMenu>
                  </div>

               </div>
            ) : (
               <>
                  <NavLink
                     to="/signin"
                     className={({ isActive }) =>
                        `hover:text-gray-400 ${isActive ? 'text-white bg-[#6ba5cc]' : 'text-black'} text-lg font-semibold rounded-full px-5 py-2`
                     }
                  >
                     Sign in
                  </NavLink>
                  <NavLink
                     to="/signup"
                     className={({ isActive }) =>
                        ` ${isActive ? 'bg-[#6ba5cc] ' : 'text-white bg-black '} font-semibold rounded-full px-5 py-2`
                     }
                  >
                     Sign up
                  </NavLink>
               </>
            )}
         </div>
      </div>
   );
}

export default Header;
