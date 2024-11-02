import React from "react";

const TermsAndConditions: React.FC = () => {
  return (
    <main id="page-content" className="flex-auto relative">
      <section className="min-h-[calc(100vh-4rem)] px-4 py-8 flex justify-center">
        <div className="max-w-[1500px] w-full">
          <h1 className="mb-6 text-2xl font-bold">Terms of Service Agreement for KoiAuction.com</h1>
          <ol className="ml-4 list-inside list-decimal">
            <li className="mb-2 font-bold">Acceptance of Terms</li>
            <h3 className="ml-2 mb-6">
              By using KoiAuction.com ("the Site"), you agree to be bound by the following Terms of Service ("the Terms").
              These Terms apply to all users of the Site and govern your access and use of the Site and the services provided
              therein, including the koi auction services. If you do not agree to these Terms, you must discontinue your use
              of the Site and services.
            </h3>
            <li className="mb-2 font-bold">Registration</li>
            <h3 className="ml-2 mb-6">
              To access certain features of the Site and participate in koi auctions, you will need to register an account
              with KoiAuction.com. By registering, you warrant that you have provided true and accurate information and agree
              to keep your account information up-to-date.
            </h3>
            <li className="mb-2 font-bold">Compliance with Applicable Laws</li>
            <h3 className="ml-2 mb-6">
              As we offer our services to users across various states in the United States, it is your responsibility to ensure
              compliance with all applicable local, state, and federal laws and regulations when using our Site and services.
            </h3>
            <li className="mb-2 font-bold">Bidding and Auction Terms</li>
            <h3 className="ml-2 mb-6">
              All bids placed on our Site are final and binding. Upon winning a koi auction, you are obligated to make a payment
              for the koi won, according to the terms set out below.
            </h3>
            <li className="mb-2 font-bold">Payment Terms</li>
            <h3 className="ml-2 mb-6">
              Payments for koi won in auction must be made within 48 hours of the auction's completion. If payment is not received
              within 7 business days of the auction's completion, we reserve the right to cancel the transaction and the koi may
              not be guaranteed to ship.
            </h3>
            <li className="mb-2 font-bold">Shipping and Delivery</li>
            <h3 className="ml-2 mb-6">
              Koi purchased on our Site will be shipped and received in accordance with the associated Breeder's best practices
              and quarantine procedures. We will make every effort to ensure the safe and timely arrival of your koi, but we cannot
              guarantee their survival during shipping and receiving from Japan as well as shipping from our US facility to the winner.
            </h3>
            <li className="mb-2 font-bold">Credits and Guarantees</li>
            <h3 className="ml-2 mb-6">
              In the unfortunate event that your koi dies during shipping and receiving from Japan, you will be provided with a
              credit towards future purchases on our Site. Once the koi has been imported into the United States, we do not offer
              any further guarantees, refunds, or credits for the koi purchased.
            </h3>
            <li className="mb-2 font-bold">Indemnification</li>
            <h3 className="ml-2 mb-6">
              You agree to indemnify, defend, and hold harmless KoiAuction.com, its affiliates, and their respective officers,
              employees, and agents from any and all claims, losses, or damages arising out of your breach of these Terms or your
              use of the Site or services.
            </h3>
            <li className="mb-2 font-bold">Governing Law and Jurisdiction</li>
            <h3 className="ml-2 mb-6">
              These Terms shall be governed by and construed in accordance with the laws of the United States, without regard to
              principles of conflict of laws. Any disputes arising from or relating to these Terms, the Site, or services shall be
              resolved by a court of competent jurisdiction in the United States.
            </h3>
            <li className="mb-2 font-bold">Modifications to Terms</li>
            <h3 className="ml-2 mb-6">
              We reserve the right to modify these Terms at any time without prior notice. Your continued use of the Site and our
              services will signify your acceptance of the updated Terms.
            </h3>
            <li className="mb-2 font-bold">Contact Us</li>
            <h3 className="ml-2 mb-6">
              For questions or concerns regarding these Terms or our services, please contact us at:
            </h3>
            <ul className="ml-4 mb-6 leading-relaxed">
              <li>KoiAuction.com</li>
              <li>FPT University</li>
              <li>Nhà Văn Hóa Sinh Viên</li>
              <li><a className="hover:underline" href="tel:+1 (865)-437-9242">+84 904 231 543</a></li>
              <li><a className="hover:underline" href="mailto:contact@KoiAuction.com">KoiAuction@fpt.edu.vn</a></li>
            </ul>
            <h3 className="text-slate-900">Thank you for choosing KoiAuction.com for your koi needs!</h3>
          </ol>
        </div>
      </section>
    </main>
  );
};

export default TermsAndConditions;
