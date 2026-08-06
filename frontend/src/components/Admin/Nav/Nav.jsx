import { assets } from "../../../assets/assets"
import { useNavigate } from "react-router-dom";
import { useAuth } from '../../../context/AuthContext.jsx';

const Nav = () => {
  const navigate = useNavigate();
  const { isAuthenticated, logout } = useAuth();

  const handleLogout = () => {
      logout();          //clear token from context and localStorage
      navigate('/login');
  };
  return(
    <nav className="fixed top-0 left-0 w-full flex items-center justify-between px-4 md:px-16 lg:px-24 xl:px-32 transition-all duration-500 bg-black/80">
      <div>
        <img src={assets.logo} alt="logo" className="h-25" />
      </div>

      <div>
          {isAuthenticated() ? (
            //Show Logout when logged in
            <button
                className={"px-8 py-2.5 rounded-full ml-4 transition-all duration-500 bg-white cursor-pointer"}
                onClick={handleLogout}
            >
                Logout
            </button>
        ) : (
            //Show Login when not logged in
            <button
                className={"px-8 py-2.5 rounded-full ml-4 transition-all duration-500 bg-white cursor-pointer"}
                onClick={() => navigate("/login")}
            >
                Login
            </button>
        )}

      </div>

    </nav>

  )
}
export default Nav