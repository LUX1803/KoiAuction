import { useState, useEffect } from 'react';
import { Transaction } from '../type/transaction.d';
import { getTransaction } from '@/service/transactionService';
import { formatInTimeZone } from 'date-fns-tz';
import { NavLink } from 'react-router-dom';
import { Checkbox } from './ui/checkbox'; // Make sure this is a controlled component
import { formatMoney } from '@/util/helper';
import { payByVNPay, payByWallet } from '@/service/paymentService';
import { useAuth } from '@/context/AuthContext';
import { getWalletBallance } from '@/service/walletService';
import { useToast } from '@/hooks/use-toast';

const BillingList = () => {

   const { user, balance, getCurrentBallance } = useAuth();

   const [isUseWallet, setIsUseWallet] = useState<boolean>(false);

   const [transactions, setTransactions] = useState<Transaction[]>([]);
   const [selectedTransactions, setSelectedTransactions] = useState<Set<number>>(new Set());
   const timezone = 'Asia/Bangkok';


   const [totalTransMoney, setTotalTransMoney] = useState(0);

   const { toast } = useToast();

   const fetchTransactions = async () => {
      const data = await getTransaction();
      // console.log("test: ", data);

      const pendingTransactions = data.filter((transaction: Transaction) => transaction.status === 'PENDING' && transaction.paymentType != 'WALLET');
      setTransactions(pendingTransactions);
   };

   useEffect(() => {
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
         if(totalTransMoney > balance) {
            toast({
                variant: "destructive",
               title: "Your Wallet doesn't have enough money",
            });
         }else{
            const payment = await payByWallet(Array.from(selectedTransactions))
            toast({
               variant: "success",
               title: "Payment success",
            });
            getCurrentBallance();
            fetchTransactions();
            setTotalTransMoney(0);
         }
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
            <h2>Paid Item: {formatMoney(totalTransMoney)}</h2>
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
                  {transactions.map((transaction, index) => {
                     return (
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
                                 onClick={() => {
                                    const isChecked = selectedTransactions.has(transaction.id);
                                    const newTotal = isChecked
                                       ? totalTransMoney - transaction.amount
                                       : totalTransMoney + transaction.amount;
                                    setTotalTransMoney(newTotal);
                                    toggleSelection(transaction.id)}}
                              />
                           </div>
                        </div>
                     )
                  })}
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
