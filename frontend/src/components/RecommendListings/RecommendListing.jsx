import { place_Dummy_list } from '../../assets/assets'
import PlaceCard from '../PlaceCard/PlaceCard'
import Title from '../Title/Title'

const RecommendListing = () => {
  return(
    <div className='flex flex-col items-center px-6 md:px-16 lg:px-24 bg-slate-50 py-20'>

      <Title title='Top Co-Living Choices' subTitle='Discover the best-reviewed rooms and homes perfect for co-living, saving money, and building community.'/>

      <div className='grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6 mt-20 w-full ml-20'>

        {place_Dummy_list.slice(0,8).map((place)=>(
          <PlaceCard key={place._id} place={place}/>
        ))}

      </div>

        <button className='my-16 px-4 py-2 text-sm font-medium border border-gray-300 rounded bg-white hover:bg-gray-50 transition-all cursor-pointer'>
          Explore All Rentals 
        </button>

          

    </div>
  )
}
export default RecommendListing