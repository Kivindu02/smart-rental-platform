import { Link, useLocation } from 'react-router-dom';
import { assets } from "../../assets/assets.js";
import { useNavigate } from "react-router-dom";
import { useEffect, useState } from 'react';

const Navbar = () => {

  const navigate = useNavigate();

  const navLinks = [
    { name: 'Home', path: '/' },
    { name: 'Browse Listings', path: '/rooms' },
    { name: 'Reviews', path: '/' },
    { name: 'Contact us', path: '/' },
  ];

   const [isScrolled, setIsScrolled] = useState(false);
   const location = useLocation();

  useEffect(() => {
    const handleScroll = () => {
      if (location.pathname !== '/') {
        setIsScrolled(true);
      } else {
        setIsScrolled(window.scrollY > 10);
      }
    };

    // Run once when route changes
    handleScroll();

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);

  }, [location.pathname]);

  return(
    <nav className={`fixed top-0 left-0  w-full flex items-center justify-between px-4 md:px-16 lg:px-24 xl:px-32 transition-all duration-500 z-50 ${isScrolled ? "bg-white/80 shadow-md text-gray-700 backdrop-blur-lg py-3 md:py-4" : "py-4 md:py-6"}`}>

      {/* Logo */}
      <Link to="/">
        <img src={assets.logo} alt="logo" 
          className={`h-25 ${isScrolled && "invert opacity-80"}`}/>    
      </Link>

      {/* Desktop Nav */}
      <div className={'hidden md:flex items-center gap-4 lg:gap-8'}>
        {navLinks.map((link, i) => (
          <a key={i} href={link.path} className={`group flex flex-col gap-0.5 ${isScrolled ? "text-gray-700" : "text-white"}`}>

            {link.name}
            <div className={`${isScrolled ? "bg-gray-700" : "bg-white"} h-0.5 w-0 group-hover:w-full transition-all duration-300`}/>

          </a>
        ))}
        <button className={`border px-4 py-1 text-sm font-light rounded-full cursor-pointer ${isScrolled ? 'text-black' : 'text-white'} transition-all`} onClick={() => navigate("/owner")}>
          Dashboard
        </button>
      </div>

      {/* Desktop Right */}
      <div>
        <button className={`px-8 py-2.5 rounded-full ml-4 transition-all duration-500 ${isScrolled ? "text-white bg-black" : "bg-white text-black"}`}>
          Login

        </button>

      </div>

      {/* Mobile Menu Button */}
      <div className={'flex items-center gap-3 md:hidden'}>
        <img src="" alt="" />

      </div>



    </nav>

  )
}

export default Navbar