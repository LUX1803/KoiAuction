import { payByVNPay } from '@/service/paymentService';
import { getTransaction } from '@/service/transactionService';
import { Transaction } from '@/type/transaction';
import { formatMoney } from '@/util/helper';
import { formatInTimeZone } from 'date-fns-tz';
import React, { useEffect, useState } from 'react'
import { NavLink } from 'react-router-dom';
import { Checkbox } from './ui/checkbox';
import { useAuth } from '@/context/AuthContext';
import { getWalletBallance } from '@/service/walletService';
import { Button, Form, Input, InputNumber, Modal } from 'antd';

const Wallet = () => {

    const { user } = useAuth();
    const [ballance, setBallance] = useState(null);

    const [transactions, setTransactions] = useState<Transaction[]>([]);
    const [selectedTransactions, setSelectedTransactions] = useState<Set<number>>(new Set());
    const timezone = 'Asia/Bangkok';

    const [isModalOpen, setIsModalOpen] = useState(false);

    const showModal = () => {
        setIsModalOpen(true);
    };


    useEffect(() => {

        //getMoney
        if (user) {
            getWalletBallance().then((data) => {
                setBallance(data);
            })
        }

        const fetchTransactions = async () => {
            const data = await getTransaction();

            const filterTransactions = data.filter((transaction: Transaction) => transaction.memberId === user?.id && transaction.paymentType === 'WALLET');

            setTransactions(filterTransactions);
        };

        fetchTransactions();
    }, [user]);

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
        console.log('Selected transactions to pay:', Array.from(selectedTransactions));
        const paymentUrl = await payByVNPay(Array.from(selectedTransactions));
        if (paymentUrl)
            window.open(paymentUrl, '_blank');
        else
            console.error('Failed to get payment URL');
    };

    return (
        <>
            <Modal title="Create Transaction" open={isModalOpen} onCancel={() => { setIsModalOpen(false); }} onOk={() => {
                setIsModalOpen(false);
            }}>
                <Form layout="vertical" autoComplete="off">
                    <Form.Item name="age" label="Input Amount: ">
                        <InputNumber className='w-1/2' />
                    </Form.Item>
                </Form>
            </Modal>
            <div >
                <div>Your current balance: {formatMoney(ballance ? ballance : 0)} </div>
                <Button type="primary" onClick={showModal}>
                    Create Transaction
                </Button>

            </div>
            <form onSubmit={handleSubmit}>
                <div className="bg-white shadow-md rounded-lg overflow-hidden border border-gray-300">
                    <div className="grid grid-cols-[1fr_2fr_2fr_3fr_2fr_2fr] bg-gray-100 text-gray-700 font-semibold p-3">
                        <div className="col-span-1">No.</div>
                        <div className="col-span-1">Description</div>
                        <div className="col-span-1">Amount</div>
                        <div className="col-span-1">Due Date</div>
                        <div className="col-span-1">Detail</div>
                        <div className="col-span-1">Status</div>
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
                                <div className="col-span-1">{formatInTimeZone(new Date(transaction.created), timezone, "dd.MM.yyyy HH:mm a")}</div>
                                <div className="col-span-1">
                                    {transaction.status}
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
            </form>
        </>
    )
}

export default Wallet