import { assets } from '../../assets/assets'
import Title from '../../components/Title/Title'
const AddListing = () => {
  return(
    <form>
      <Title align='left' font='outfit' title='Add Listing' subTitle='Fill in the details carefully and accurate listing details, pricing, and small description, to enhance the user experience'/>

      {/* Upload Area For Image */}
      <p className='text-gray-800 mt-10'>Images</p>
      <div className='grid grid-cols-2 sm:flex gap-4 my-2 flex-wrap'>
        <label htmlFor="image" >
          <img src={assets.upload_area} alt="" />
        </label>
        <input type="file" id="image" hidden required />

      </div>

      <div className='p-1'>

        {/* title */}
        <div className="mb-4">
          <p className="block text-gray-800 font-medium mb-1">Title</p>
          <input className="border border-gray-300 rounded p-2 w-80 focus:ring-2 focus:ring-blue-500 focus:outline-none" type="text" name='title' placeholder='Type here' />
        </div>

        <div className='mb-4'>
          <p className='text-gray-800 mt-4'>Lising Type</p>
          <select name='listing_type' className='border opacity-70 border-gray-300 mt-1 rounded p-2 w-80'>
            <option value=''>Select</option>
            <option value="House">House</option>
            <option value="Room">Room</option>
            <option value="commercial Area">Commercal Area</option>
          </select>

        </div>

        <div className="mb-4">
          <p className="block text-gray-800 font-medium mb-1">Location</p>
          <input className="border border-gray-300 rounded p-2 w-80 focus:ring-2 focus:ring-blue-500 focus:outline-none" type="text" name='location' placeholder='Type here' />
        </div>

          {/* Price */}
        <div className="mb-4">
          <p className="block text-gray-800 font-medium mb-1">Price</p>
          <input className="border border-gray-300 rounded p-2 w-80 focus:ring-2 focus:ring-blue-500 focus:outline-none" type="number" name="price" placeholder='Type here' />
        </div>

        {/* Description */}
        <div className="mb-4">
          <p className="block text-gray-800 font-medium mb-1">Description</p>
          <textarea className="border border-gray-300 rounded p-2 w-80 focus:ring-2 focus:ring-blue-500 focus:outline-none" name="description" rows="6" placeholder='Write Content here'></textarea>
        </div>

        <div>
          <button className="bg-blue-600 text-white font-medium py-2 px-4 rounded hover:bg-blue-700 transition duration-200" type='submit'>Add</button>
        </div>

      </div>

    </form>

  )
}
export default AddListing