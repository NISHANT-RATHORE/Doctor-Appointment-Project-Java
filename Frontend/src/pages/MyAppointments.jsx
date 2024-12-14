// eslint-disable-next-line no-unused-vars
import React, { useContext, useEffect, useState } from 'react'
import { AppContext } from "../context/AppContext.jsx";
import axios from 'axios';
import { toast } from 'react-toastify';
import { useNavigate } from 'react-router';

const MyAppointments = () => {
    const { backendUrl3, token, userId } = useContext(AppContext)
    const navigate = useNavigate()

    const [appointments, setAppointments] = useState([])
    const months = ['', 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
    const slotDateFormat = (slotDate) => {
        const dateArray = slotDate.split('_')
        return dateArray[0] + " " + months[Number(dateArray[1])] + " " + dateArray[2]
    }

    const getUserAppointments = async () => {
        try {
            const response = await axios.get(`${backendUrl3}/appointment/getAppointments`, {
                params: {
                    username: userId
                }
            });
            if (response.status === 200) {
                setAppointments(response.data.reverse());
            }
        } catch (error) {
            console.log(error);
            toast.error('Failed to fetch appointments');
        }
    };

    const cancelAppointment = async (appointmentId) => {
        try {
            const response = await axios.put(`${backendUrl3}/appointment/cancelAppointment`, null, {
                params: {
                    appointmentId: appointmentId
                }
            });
            if (response.status === 200) {
                toast.success('Appointment cancelled successfully');
                getUserAppointments();
            } else {
                toast.error('Failed to cancel appointment');
            }
        } catch (error) {
            console.log(error);
            toast.error('Error cancelling appointment');
        }
    };

    const initPay = (order) => {
        const options = {
            key: import.meta.env.VITE_RAZORPAY_KEY_ID,
            amount: order.amount,
            currency: order.currency,
            name: 'Appointment Payment',
            description: 'Appointment Payment',
            order_id: order.id,
            receipt: order.receipt,
            handler: async (response) => {
                try {
                    const verifyResponse = await axios.post(`${backendUrl3}/appointment/verifyPayment`, null, {
                        params: {
                            razorpay_order_id: response.razorpay_order_id
                        }
                    });
                    if (verifyResponse.status === 200) {
                        toast.success('Payment successful');
                        getUserAppointments();
                        navigate('/my-appointments');
                    } else {
                        toast.error('Payment failed');
                    }
                } catch (error) {
                    console.error(error);
                    toast.error(error.message);
                }
            }
        }
        const rzp = new window.Razorpay(options)
        rzp.open()
    }
    const appointmentRazorPay = async (appointmentID) => {
        try {
            const response = await axios.get(`${backendUrl3}/appointment/payment`, {
                params: {
                    appointmentId: appointmentID
                }
            });
            if (response.status === 200) {
                initPay(response.data)
            } else {
                toast.error('Payment failed');
            }
        } catch (error) {
            console.error(error);
            toast.error('Error in payment');
        }
    };

    useEffect(() => {
        if (token) {
            getUserAppointments()
        }
    }, [token]);

    return (
        <div className='pb-3 mt-12 font-medium text-zinc-700 border-b'>
            <p>My Appointments</p>
            <div>
                {
                    appointments.map((item, index) => (
                        <div className='grid grid-cols-[1fr_2fr] gap-4 sm:flex sm:gap-6 py-2 border-b' key={index}>
                            <div>
                                <img className='w-32 bg-indigo-50' src={item.docData.docImg} />
                            </div>
                            <div className='flex-1 text-sm text-zinc-600'>
                                <p className='text-neutral-800 font-semibold'>{item.docData.name}</p>
                                <p>{item.docData.speciality}</p>
                                <p className='text-zinc-700 font-medium mt-1'>Address:</p>
                                <p className='text-xs'>{item.docData.address1}</p>
                                <p className='text-xs'>{item.docData.address2}</p>
                                <p className='text-xs mt-1'><span className='text-sm text-neutral-700 font-medium'>Date & Time:</span> {slotDateFormat(item.slotDate)} | {item.slotTime}</p>
                            </div>
                            <div></div>
                            <div className='flex flex-col gap-2 justify-end'>
                                {!item.cancelled && item.payment && <button className='sm:min-w-48 py-2 border rounded text-stone-500 bg-indigo-50'>Appointment Booked</button>}
                                {!item.cancelled && !item.payment && <button onClick={() => appointmentRazorPay(item.appointmentId)} className='text-sm text-stone-500 text-center sm:min-w-48 py-2  border rounded hover:bg-primary hover:text-white transition-all duration-300'>Pay Online</button>}
                                {!item.cancelled && !item.payment &&  <button onClick={() => cancelAppointment(item.appointmentId)} className='text-sm text-stone-500 text-center sm:min-w-48 py-2  border rounded hover:bg-red-700 hover:text-white transition-all duration-300'>Cancel appointment</button>}
                                {item.cancelled && <button className='sm:min-w-48 py-2 border border-red-500 rounded text-red-500'>Appointment Cancelled</button>}
                            </div>
                        </div>
                    ))
                }
            </div>
        </div>
    )
}

export default MyAppointments
