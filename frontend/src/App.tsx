import Header from "./components/Header"
import { Outlet } from 'react-router-dom';

import { Toaster } from "./components/ui/toaster";
import { ToastProvider } from "./components/ui/toast";
import Footer from "./components/footer";
function App() {

  return (
    <>
      {/* <div className="flex flex-col h-screen overflow-hidden">
        <nav className="bg-[#044f4a35] text-black p-4 sticky top-0">
          <Header />
        </nav>
        <div className="flex flex-col flex-grow overflow-y-scroll">
          <main className="min-h-[calc(100vh)] bg-white px-8 pt-10 pb-20 ">
            <ToastProvider>
              <Outlet />
              <Toaster />
            </ToastProvider>
          </main>
          <Footer />
        </div>
      </div> */}

      <div className="flex flex-col min-h-screen">
        <nav className="bg-[#044f4a35] text-black p-4">
          <Header />
        </nav>

        {/* Wrapping main and footer in a single scrollable container */}
        <div className="flex-grow overflow-y-auto flex flex-col">
          <main className="min-h-[calc(100vh)] flex-grow bg-white px-8 pt-10 pb-20">
            <ToastProvider>
              <Outlet />
              <Toaster />
            </ToastProvider>
          </main>

          {/* Footer at the end of the scrollable content */}
          <Footer />
        </div>
      </div>

    </>
  )
}

export default App
