import { Route, Routes, useLocation } from "react-router-dom";
import Footer from "./components/Footer/Footer"
import Navbar from "./components/Navbar/Navbar"
import Home from "./pages/Home/Home"
import SpaceDetails from "./pages/SpaceDetails/SpaceDetails";
import Layout from "./pages/ListOwner/Layout";
import AddListing from "./pages/ListOwner/AddListing";
import AllListing from "./pages/ListOwner/AllListing";
import Login from "./pages/Auth/Login";
import Register from "./pages/Auth/Register";

const App = () => {
  const isOwnerPath = useLocation().pathname.includes("admin");
  const isLoginPath = useLocation().pathname.includes("login");
  const isRegisterPath = useLocation().pathname.includes("register");
  return (
    <div>
      {!isOwnerPath && !isLoginPath && !isRegisterPath && <Navbar />}
      <div className='min-h-[70vh]'>
        <Routes>
          {/* Public Routes */}
          
          <Route path='/' element={<Home />} />

          <Route path='/login' element={<Login />}/>
          <Route path='/register' element={<Register />}/>
          
          

          {/* Private Routes */}
          <Route path='/rooms/:id' element={<SpaceDetails />} />

          <Route path='/owner' element={<Layout />}>
            
            <Route index element={<AddListing />} />
            <Route path="list-room" element={<AllListing />} />

          </Route>

        </Routes>

      </div>
      {!isOwnerPath && !isLoginPath && !isRegisterPath && <Footer />}

    </div>

  )
}
export default App
    
 
