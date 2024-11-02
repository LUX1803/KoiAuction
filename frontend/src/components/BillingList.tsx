import { useState, useEffect } from 'react';
import { Transaction } from '../type/transaction.d';
import { getTransaction } from '@/service/transactionService';
import { formatInTimeZone } from 'date-fns-tz';
import { NavLink } from 'react-router-dom';
import { Checkbox } from './ui/checkbox'; // Make sure this is a controlled component
import { formatMoney } from '@/util/helper';
import { payByVNPay } from '@/service/paymentService';
import { useAuth } from '@/context/AuthContext';
import { getWalletBallance } from '@/service/walletService';

const BillingList = () => {

   const { user } = useAuth();

   const [balance, setBalance] = useState<number>(0);
   const [isUseWallet, setIsUseWallet] = useState<boolean>(false);

   const [transactions, setTransactions] = useState<Transaction[]>([]);
   const [selectedTransactions, setSelectedTransactions] = useState<Set<number>>(new Set());
   const timezone = 'Asia/Bangkok';


   useEffect(() => {
      //getMoney
      if (user) {
         getWalletBallance().then((data) => {
            setBalance(data);
         })
      }
   }, [user])


   useEffect(() => {
      const fetchTransactions = async () => {
         const data = await getTransaction();
         // console.log("test: ", data);

         const pendingTransactions = data.filter((transaction: Transaction) => transaction.status === 'PENDING' && transaction.paymentType != 'WALLET');
         setTransactions(pendingTransactions);
      };

      fetchTransactions();
   }, []);

   const toggleSelection = (transactionId: number) => {
      console.log('Toggle selection:', transactionId);
      setSelectedTransactions((prev) => {
         const newSelected = new Set(prev);
         if (newSelected.has(transactionId)) {
            newSelected.delete(transactionId);
         } else {
            newSelected.add(transactionId);
         }
         return newSelected;
      });
   };

   const handleSubmit = async (event: React.FormEvent) => {
      event.preventDefault();
      // console.log('Selected transactions to pay:', Array.from(selectedTransactions));
      if (isUseWallet) {
         
      } else {
         const paymentUrl = await payByVNPay(Array.from(selectedTransactions));
         if (paymentUrl)
            window.open(paymentUrl, '_blank');
         else
            console.error('Failed to get payment URL');
      }
   };

   return (
      <>
         <div>
            <h2>Paid Item</h2>
         </div>
         <form onSubmit={handleSubmit}>
            <div className="bg-white shadow-md rounded-lg overflow-hidden border border-gray-300">
               <div className="grid grid-cols-[1fr_2fr_2fr_3fr_2fr_2fr] bg-gray-100 text-gray-700 font-semibold p-3">
                  <div className="col-span-1">No.</div>
                  <div className="col-span-1">Description</div>
                  <div className="col-span-1">Amount</div>
                  <div className="col-span-1">Due Date</div>
                  <div className="col-span-1">Detail</div>
                  <div className="col-span-1">Action</div>
               </div>

               <div className="">
                  {transactions.map((transaction, index) => (
                     <div key={transaction.id} className="grid grid-cols-[1fr_2fr_2fr_3fr_2fr_2fr] text-gray-800 p-3">
                        <div className="col-span-1">
                           {index + 1}
                        </div>
                        <div className="col-span-1">{transaction.description}</div>
                        <div className="col-span-1">{formatMoney(transaction.amount)} VND</div>
                        <div className="col-span-1">{transaction.closed && formatInTimeZone(new Date(transaction.closed), timezone, "dd.MM.yyyy HH:mm a")}</div>
                        <div className="col-span-1">
                           <NavLink to={`/invoice/${transaction.invoiceId}`} className="text-blue-500 hover:underline">
                              View
                           </NavLink>
                        </div>
                        <div className="col-span-1">
                           <Checkbox
                              value={transaction.id}
                              checked={selectedTransactions.has(transaction.id)}
                              onClick={() => toggleSelection(transaction.id)}
                           />
                        </div>
                     </div>
                  ))}
               </div>
            </div>


            <div className="flex justify-end mt-4 mr-4">
               <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
                  Proceed to Payment
               </button>
            </div>
            <div className="flex justify-end items-center gap-2 mt-4 mr-4">
               <Checkbox onClick={() => setIsUseWallet(!isUseWallet)} />Use Wallet: {balance}
            </div>
         </form>
      </>
   );
};

export default BillingList;
