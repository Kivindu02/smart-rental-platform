import React from 'react'
import { assets } from '../../assets/assets'

const Footer = () => {
  return (
    <div className='bg-[#F6F9FC] text-gray-500/80 pt-8 px-6 md:px-16 lg:px-24 xl:px-32'>

        <div className="flex flex-col md:flex-row justify-between w-full gap-50 border-b border-gray-500/30 pb-6">
            <div className="md:max-w-96">
                <img src={assets.logo} alt="logo" className='mb-1 h-18 md:h-19 invert opacity-80'/>
                <p className="mt-1 text-sm">
                    Find affordable rooms, book instantly, and live comfortably—your perfect shared space starts here!
                </p>
                <div className='flex item-center gap-3 mt-4'>
                  <img src={assets.facebook_icon} alt="facebook-icon" className='w-6' />
                  <img src={assets.linkedin_icon} alt="linkendin-icon" className='w-6' />
                  <img src={assets.twitter_icon} alt="twitter-icon" className='w-6' />
                </div>
            </div>
            <div className="flex-1 flex items-start md:justify-end gap-110">
                <div>
                    <p className="font-playfair mb-5 text-gray-800 text-lg">Company</p>
                    <ul className="text-sm space-y-2">
                        <li><a href="#">Home</a></li>
                        <li><a href="#">About us</a></li>
                        <li><a href="#">Contact us</a></li>
                        <li><a href="#">Privacy policy</a></li>
                    </ul>
                </div>
                <div>
                    <p className="font-playfair mb-5 text-gray-800 text-lg">Get in touch</p>
                    <div className="text-sm space-y-2">
                        <p>+1-212-456-7890</p>
                        <p>contact@example.com</p>
                    </div>
                </div>
            </div>
        </div>
        <p className="pt-4 text-center text-xs md:text-sm pb-5">
            Copyright 2024 © <a href="https://prebuiltui.com">CoRent.lk</a>. All Right Reserved.
        </p>
      
    </div>
  )
}

export default Footer
