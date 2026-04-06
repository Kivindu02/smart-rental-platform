import { assets} from '../../assets/assets'

const Hero = () => {
  return(
    <div className="relative h-screen bg-no-repeat bg-cover bg-center"
    style={{ backgroundImage: `url("${assets.heroImage}")` }}>

      <div className="absolute right-0 top-1/3 transform -translate-y-1/4 px-6 md:px-16 lg:px-24 xl:px-32 text-white">
        <h1 className="text-5xl md:text-7xl  tracking-tight text-left">
          <span className="block mt-10 font-playfair font-bold md:font-extrabold">Find your</span>
          <span className="block mt-6 font-playfair font-bold md:font-extrabold">perfect place</span>
        </h1>

      </div>

    </div>

  )
}
export default Hero