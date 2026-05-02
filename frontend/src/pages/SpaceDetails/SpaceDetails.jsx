import { useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { place_Dummy_list, userDummyData, assets } from '../../assets/assets'
import StarRating from '../../components/StarRating/StarRating'
import Reviews from '../../components/Reviews/Reviews'

const SpaceDetails = () => {
  const {id} = useParams()
  const [mainImage, setMainImage] = useState(null)
  const [space, setSpace] = useState(null)

  useEffect(() => {
    const space = place_Dummy_list.find((s) => s._id === id)
    if (space) {
     setSpace(space)
     setMainImage(space.images[0])
    }
    }, [id]);

  return space &&(
    <div className="py-28 md:py-35 px-4 md:px-16 lg:px-24 xl:px-32">
      {/* Space Details */}
      <div className='flex flex-col md:flex-row items-start md:items-center gap-2'>
        <h1 className='text-3xl md-text-4xl font-playfair'>{space.name} <span className='font-inter text-sm'>({space.type})</span></h1>      
      </div>

      {/* Space Rating */}
      <div className='flex item-center gap-1 mt-2'>
        <StarRating />
      </div>

      <div className='flex items-center gap-1 text-sm mt-2'>
          <img src={assets.location_icon} alt="location-icon" />
          <span>{space.location}</span>
      </div>

      {/* Room Images */}
      <div className='flex flex-col lg:flex-row mt-6 gap-6'>
        <div className='lg:w-1/2 w-full'>
          <img src={mainImage} alt="Room Image"  className='w-full rounded-xl shadow-lg object-cover'/>
        </div>
        <div className='grid grid-cols-2 gap-4 lg:w-1/2 w-full'>
          {space?.images.length > 1 && space.images.map((image, index)=>(
            <img onClick={()=> setMainImage(image)} 
            key={index} src={image} alt="Room Image" className={`w-full rounded-xl shadow-md object-cover cursor-pointer ${mainImage === image && 'outline outline-3 outline-orange-500'}`}/>
          ))}
        </div>
      </div>

      {/* Room Highlights*/}
      <div className='bg-[#F6F9FC] text-gray-500/80 pt-8 px-6 md:px-16 lg:px-24 xl:px-32 mt-5'>
        <div className='mb-4'><p className='text-xl font-semibold'>Details</p></div>
        <div className="flex flex-col md:flex-row justify-between w-full gap-10 border-gray-500/30 pb-6">

          <div className="flex flex-col text-lg">
        
            <p className='mb-2'>Location: {space.location}</p>
            
            <p>Type: {space.type}</p>

          </div>
        
          <div className="flex flex-col text-lg">

            
            <p className='mb-2'>Owner: {userDummyData.username}</p>
            
            <p className='mb-2'>Contact no: {userDummyData.contactno}</p>
            <p>Email: {userDummyData.email}</p>

          </div>

          <div className="flex flex-col text-lg">
            <p>Rs:{space.price}/month</p>

          </div>

        </div>

        <div>
          <p className='text-lg mb-2'>Description:</p>
          <p className='text-lg'>Home is more than just a place—it's a feeling of comfort, safety, and belonging. It’s where we create memories with loved ones, unwind after a long day, and express ourselves freely. Whether big or small, cozy or spacious, home is the heart of our lives, filled with warmth and familiarity. It’s the one place in the world that truly feels like our own.</p>
        </div>
        

      </div>

      <div className='mt-20'>
        <Reviews />
      </div>

    </div>
    
  )
} 
export default SpaceDetails