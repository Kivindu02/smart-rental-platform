import PlaceCard from '../PlaceCard/PlaceCard'
import Title from '../Title/Title'
import { getAllProperties } from '../../services/propertyService'
import { useEffect, useState } from 'react'

const RecommendListing = () => {
    const [properties, setProperties] = useState([])
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        fetchProperties()
    }, [])

    const fetchProperties = async () => {
        try {
            const data = await getAllProperties()
            setProperties(data)
        } catch (err) {
            console.error(err.response?.data?.message || "Failed to load properties")
        } finally {
            setLoading(false)
        }
    }

    if (loading) return <p>Loading...</p>
  return(
    <div className='flex flex-col items-center px-6 md:px-16 lg:px-24 bg-slate-50 pt-20 pb-10'>

      <Title title='Top Co-Living Choices' subTitle='Discover the best-reviewed rooms and homes perfect for co-living, saving money, and building community.'/>

      <div className='grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 mt-20 w-full ml-20'>

        {properties.slice(0,8).map((place)=>(
          <PlaceCard key={place.id} place={place}/>
        ))}

      </div>

        <button className='my-16 px-4 py-2 text-sm font-medium border border-gray-300 rounded bg-white hover:bg-gray-50 transition-all cursor-pointer'>
          Explore All Rentals 
        </button>

          

    </div>
  )
}
export default RecommendListing