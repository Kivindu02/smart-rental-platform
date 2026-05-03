import { Outlet } from "react-router-dom"
import Sidebar from "../../components/Admin/Sidebar/Sidebar"
import Nav from "../../components/Admin/Nav/Nav"


const Layout = () => {
  return(
    <div className='flex flex-col h-screen py-28 md:py-35 px-4 md:px-16 lg:px-24 xl:px-32'>
      <Nav />
      <div className='flex h-full'>
        <Sidebar />
        <div className='flex-1 p-4 pt-10 md:px-10 h-full overflow-auto'>
        <Outlet/>
        </div>

      </div>

    </div>
  )
}
export default Layout