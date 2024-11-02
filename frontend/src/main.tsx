import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import { createBrowserRouter, RouterProvider } from 'react-router-dom'
import HomePage from './components/page/HomePage.tsx'
import LoginForm from './components/LoginForm.tsx'
import App from './App.tsx'
import AboutPage from './components/page/AboutPage.tsx'
import { RegistrationForm } from './components/RegistrationForm.tsx'
import { AuthProvider } from './context/AuthContext.tsx'
import AuctionList from './components/AuctionList.tsx'
import AuctionDetail from './components/AuctionDetail.tsx'
import AuctionEvent from './components/AuctionEvent.tsx'
import LotDetail from './components/LotDetail.tsx'
import Staff from './components/Staff.tsx'
import { KoiForm } from './components/KoiForm.tsx'
import { KoiRequest } from './components/KoiRequest.tsx'
import TestCountdown from './components/TestCountdown.tsx'
import BillingList from './components/BillingList.tsx'
import KoiList from './components/KoiList.tsx'
import KoiDetail from './components/KoiDetail.tsx'
import BillingDetail from './components/BillingDetail.tsx'
import TransactionList from './components/TransactionList.tsx'
import Shipping from './components/Shipping.tsx'
import Privacy from './components/Privacy.tsx'
import TermsAndConditions from './components/TermAndConditions.tsx'
import NextEvent from './components/NextEvent.tsx'
import Wallet from './components/Wallet.tsx'


const route = createBrowserRouter([
  {
    path: '/',
    element: <App />,
    children: [
      { path: '/', element: <HomePage /> },
      { path: '/auction', element: <AuctionEvent /> },
      { path: '/auction/past', element: <AuctionList /> },
      { path: '/auction/:auctionId', element: <AuctionDetail /> },
      { path: '/auction/:auctionId/:lotId', element: <LotDetail /> },
      { path: '/about', element: <AboutPage /> },
      { path: '/signin', element: <LoginForm /> },
      { path: '/signup', element: <RegistrationForm /> },
      { path: '/breeder', element: <div>Breeder</div> },
      { path: '/koi', element: <KoiList /> },
      { path: '/koitable', element: <KoiRequest /> },
      { path: '/test', element: <TestCountdown /> },
      { path: '/billing', element: <BillingList /> },
      { path: '/invoice/:invoiceId', element: <BillingDetail /> },
      { path: '/staff', element: <Staff /> },
      { path: '/koi-review', element: <KoiRequest /> },
      { path: '/auction-setup', element: <NextEvent /> },
      { path: '/koi/new', element: <KoiForm /> },
      { path: '/koi/:koiId', element: <KoiDetail /> },
      { path: '/transaction', element: <TransactionList /> },
      { path: '/shipping', element: <Shipping /> },
      { path: '/privacy', element: <Privacy /> },
      { path: '/terms', element: <TermsAndConditions /> },
      { path: '/wallet', element: <Wallet /> },
    ]
  }
])

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <AuthProvider>
      <RouterProvider router={route} />
    </AuthProvider>
  </StrictMode>,
)
