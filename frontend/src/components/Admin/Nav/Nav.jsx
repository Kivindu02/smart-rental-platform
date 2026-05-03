import { assets } from "../../../assets/assets"

const Nav = () => {
  return(
    <nav className="fixed top-0 left-0 w-full flex items-center justify-between px-4 md:px-16 lg:px-24 xl:px-32 transition-all duration-500 bg-black/80">
      <div>
        <img src={assets.logo} alt="logo" className="h-25" />
      </div>

      <div>
        <button className="px-8 py-2.5 rounded-full ml-4 transition-all duration-500 bg-white">
          Logout
        </button>
      </div>

    </nav>

  )
}
export default Nav