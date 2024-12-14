import React, { useContext, useEffect } from 'react'
import { AdminContext } from '../../Context/AdminContext'
import {assets} from '../../assets/assets'
import { AppContext } from '../../Context/AppContext'

const Dashboard = () => {
  const {aToken,cancelAppointment,getDashboardData,dashData} = useContext(AdminContext)
  const {slotDateFormat} = useContext(AppContext)
  useEffect(() => {
    if(aToken){
      getDashboardData()
    }
  }, [aToken])
  
  return dashData && (
    <div className='m-5'>
      <div className='flex flex-wrap gap-3'>
        <div className='flex items-center gap-2 bg-white p-4 min-w-52 rounded border-2 border-gray-100 cursor-pointer hover:scale-105 transition-all'>
          <img className='w-14' src={assets.doctor_icon}/>
          <div>
            <p className='text-xl font-semibold text-gray-600'>{dashData.doctorsData.length}</p>
            <p className='text-gray-400'>Doctors</p>
          </div>
        </div>

        <div className='flex items-center gap-2 bg-white p-4 min-w-52 rounded border-2 border-gray-100 cursor-pointer hover:scale-105 transition-all'>
          <img className='w-14' src={assets.appointments_icon}/>
          <div>
            <p className='text-xl font-semibold text-gray-600'>{dashData.appointmentData.length}</p>
            <p className='text-gray-400'>Doctors</p>
          </div>
        </div>

        <div className='flex items-center gap-2 bg-white p-4 min-w-52 rounded border-2 border-gray-100 cursor-pointer hover:scale-105 transition-all'>
          <img className='w-14' src={assets.patients_icon}/>
          <div>
            <p className='text-xl font-semibold text-gray-600'>{dashData.userData.length}</p>
            <p className='text-gray-400'>Doctors</p>
          </div>
        </div>
      </div>

      <div className='bg-white'>
        <div className='flex items-center gap-2.5 px-4 py-4 mt-10 rounded-t border'>
          <img src={assets.list_icon}/>
          <p className='font-medium'>Latest Bookings</p>
        </div>

        <div className='pt-4 border border-t-0'>
          {
            dashData.appointmentData.map((item,index)=>(
              <div className='flex items-center px-6 py-3 gap-3 hover:bg-gray-600' key={index}>
                <img className='rounded-full w-10' src={item.docData.docImg}/>
                <div className='flex-1 text-sm'>
                  <p className='text-gray-800 font-medium'>{item.docData.name}</p>
                  <p className='text-gray-600'>{slotDateFormat(item.slotDate)}</p>
                </div>
                {
                  item.cancelled ?
                      <p className='text-red-400 text-xs font-medium'>Cancelled</p> :
                      <img onClick={()=>cancelAppointment(item.appointmentId)} className='w-10 cursor-pointer' src={assets.cancel_icon}/>
                }
              </div>
            ))
          }
        </div>
      </div>
    </div>
  )
}

export default Dashboard