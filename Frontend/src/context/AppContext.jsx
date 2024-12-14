import {createContext, useEffect, useState} from "react";
import axios from "axios";
import {toast} from "react-toastify"

export const AppContext = createContext();

const AppContextProvider = (props) => {

    const currencySymbol = '$';
    const backendUrl1 = import.meta.env.VITE_BACKEND_URL1
    const backendUrl2 = import.meta.env.VITE_BACKEND_URL2
    const backendUrl3 = import.meta.env.VITE_BACKEND_URL3
    const [doctors,setDoctors] = useState([])
    const [token,setToken] = useState(localStorage.getItem('token') ? localStorage.getItem('token') : false)
    const [userData,setUserData] = useState(false)
    const [userId,setUserId] = useState(false)

    const getDoctorsData = async () => {
        try {
            const response = await axios.get(`${backendUrl2}/doctor/list`)
            if(response.status === 200){
                setDoctors(response.data)
            }
        } catch (error) {
            console.log(error)
            toast.error(error.message)
        }
    }

    const loadUserProfileData = async () => {
        try {
            const response = await axios.get(`${backendUrl1}/user/getProfile`,{
                headers: {
                    Authorization: `Bearer ${token}`
                }
            })
            if(response.status === 200){
                setUserData(response.data)
                setUserId(response.data.email)
            } else {
                toast.error('Failed to load profile data')
            }
        } catch (error) {
            console.log(error)
            toast.error(error.message)
        }
    }

    const value = {
        doctors,currencySymbol,getDoctorsData,
        token,setToken,
        backendUrl1,backendUrl2,backendUrl3,
        userData,setUserData,
        loadUserProfileData,
        userId,setUserId,
    }

    useEffect(()=>{
        getDoctorsData()
    },[])

    useEffect(()=>{
        if(token){
            loadUserProfileData()
        } else{
            setUserData(false)
        }
    },[token])

    

    return (
        <AppContext.Provider value={value}>
            {/* eslint-disable-next-line react/prop-types */}
            {props.children}
        </AppContext.Provider>
    )
}

export default AppContextProvider
