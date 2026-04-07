import React from 'react'
import { Link } from 'react-router-dom'
import { assets } from '../../assets/assets'

const PlaceCard = ({place}) => {
  return (
    <Link to={'/rooms/' + place._id} onClick={()=> scrollTo(0,0)} key={place.id} className='relative max-w-70 w-full rounded-xl overflow-hidden bg-white text-gray-500/90 shadow-[0px_4px_4px_rgba(0,0,0,0.05)]'>
      <img src={place.images[0]} alt="" />

      <div className='p-4 pt-5'>
        <div className='flex items-center justify-start'>
          <p className='font-playfair text-xl font-medium text-gray-800 break-words max-w-[70%] mr-5'>{place.name}</p>
          <div className='flex items-center gap-1'>
            <img src={assets.starIconFilled} alt="star-icon" /> 4.5
          </div>
        </div>
        <div className='flex items-center gap-1 text-sm'>
          <img src={assets.location_icon} alt="location-icon" />
          <span>{place.location}</span>
        </div>
        <div className='flex items-center justify-start mt-4'>
          <p className='mr-5'><span className='text-xl text-gray-800'>Rs{place.price}</span>/month</p>
          <button className='px-4 py-2 text-sm font-medium border border-gray-300 rounded hover:bg-gray-50 transition-all cursor-pointer whitespace-nowrap'>Book Now</button>
        </div>
      </div> 
    </Link>

  )
}

export default PlaceCard
