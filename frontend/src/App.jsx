import { Route, Routes, useLocation } from "react-router-dom";
import Footer from "./components/Footer/Footer"
import Navbar from "./components/Navbar/Navbar"
import Home from "./pages/Home/Home"
import SpaceDetails from "./pages/SpaceDetails/SpaceDetails";

const App = () => {
  const isOwnerPath = useLocation().pathname.includes("owner");
  return (
    <div>
      {!isOwnerPath && <Navbar />}
      <div className='min-h-[70vh]'>
        <Routes>
          {/* Public Routes */}
          <Route path='/' element={<Home />} />

          {/* Private Routes */}
          <Route path='/rooms/:id' element={<SpaceDetails />} />

        </Routes>

      </div>
      {!isOwnerPath && <Footer />}

    </div>

  )
}
export default App
    
 
