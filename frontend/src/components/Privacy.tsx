import React from "react";

const Privacy: React.FC = () => {
  return (
    <main id="page-content" className="flex-auto relative">
      <section className="min-h-[calc(100vh-4rem)] px-4 py-8 flex justify-center">
        <div className="max-w-[1500px] w-full">
          <h1 className="mb-8 text-2xl font-bold">Privacy Policy for KoiAuction.com</h1>

          <h3 className="ml-2 mb-6">
            KoiAuction.com ("us", "we", or "our") is a koi auction website owned and operated by
            Select Koi Inc., a US-based company. Our website,{" "}
            <a className="hover:underline" href="https://KoiAuction.com">
              KoiAuction.com
            </a>
            , is designed to provide a platform for customers to bid on and purchase koi fish from
            reputable Japanese breeders.
          </h3>

          <h3 className="ml-2 mb-6">
            Your privacy is our top priority. This Privacy Policy applies to all the services
            provided by KoiAuction.com, and describes the types of information we collect, how it is
            used, and your rights to update, modify or delete your personal information.
          </h3>

          <h3 className="ml-2 mb-6">
            By using our services, you consent to the collection, use, and disclosure of your
            information as outlined in this Privacy Policy.
          </h3>

          <ol className="ml-4 list-inside list-decimal">
            <li className="mb-2 font-bold">Information We Collect</li>
            <h3 className="ml-2 mb-2">
              When you register for an account on KoiAuction.com, we collect the following
              information to deliver our services to you:
            </h3>
            <ul className="ml-4 mb-6 list-inside list-disc">
              <li>Your name</li>
              <li>Mailing Address</li>
              <li>Email Address</li>
              <li>Contact phone number</li>
              <li>Billing and payment information</li>
            </ul>

            <li className="mb-2 font-bold">How We Use Your Information</li>
            <h3 className="ml-2 mb-2">We use your personal data in the following ways:</h3>
            <ul className="ml-4 mb-6 list-inside list-disc">
              <li>To manage and maintain your account</li>
              <li>To process and fulfill koi purchases, including payment processing and shipping</li>
              <li>To send transactional emails, such as order confirmations, shipping notifications, and payment-related information</li>
              <li>To send promotional and marketing emails, if you have opted into our email system</li>
              <li>To communicate any changes or updates to our services or policies</li>
              <li>To improve and analyze the performance of our website</li>
            </ul>

            <li className="mb-2 font-bold">Sharing your information</li>
            <h3 className="ml-2 mb-2">
              We do not sell or share your personal information with third parties, except when
              necessary to fulfill our services, as follows:
            </h3>
            <ul className="ml-4 mb-6 list-inside list-disc">
              <li>
                We share your payment information with our payment processor, solely for the purpose
                of processing your payment transactions.
              </li>
              <li>
                We share your shipping information with our chosen courier or postal services for
                delivering the Koi fish you have purchased.
              </li>
            </ul>

            <li className="mb-2 font-bold">Your Choices and Rights</li>
            <h3 className="ml-2 mb-2">
              You may update, modify, or delete your personal information or deactivate your
              KoiAuction.com account at any time by sending us a request to our customer support.
            </h3>
            <h3 className="ml-2 mb-6">
              You may choose to unsubscribe from our marketing emails by following the
              "unsubscribe" link or instructions provided in our promotional emails or by contacting
              our customer support.
            </h3>

            <li className="mb-2 font-bold">Data Security</li>
            <h3 className="ml-2 mb-6">
              We have implemented and maintain appropriate security measures to protect your
              personal data from unauthorized access, alteration, disclosure, or destruction.
              However, no method of data storage or transmission is 100% secure, thus we cannot
              guarantee absolute security.
            </h3>

            <li className="mb-2 font-bold">Children's Privacy</li>
            <h3 className="ml-2 mb-6">
              KoiAuction.com is not intended for use by individuals under the age of 18. We do not
              knowingly collect personal data from minors. If you become aware that a child has
              provided us with personal data, please contact us immediately, and we will take steps
              to delete such information from our system.
            </h3>

            <li className="mb-2 font-bold">Updates to this Privacy Policy</li>
            <h3 className="ml-2 mb-6">
              We may update the Privacy Policy from time to time, and any changes will be posted on
              this page. We encourage you to review this Privacy Policy periodically to stay
              informed about how we are protecting your information.
            </h3>

            <li className="mb-2 font-bold">Contact Us</li>
            <h3 className="ml-2 mb-2">
              If you have any questions or concerns regarding this Privacy Policy or our practices,
              please contact us at:
            </h3>
            <ul className="ml-4 mb-6 leading-relaxed">
            <li>KoiAuction.com</li>
              <li>FPT University</li>
              <li>Nhà Văn Hóa Sinh Viên</li>
              <li><a className="hover:underline" href="tel:+1 (865)-437-9242">+84 904 231 543</a></li>
              <li><a className="hover:underline" href="mailto:contact@KoiAuction.com">KoiAuction@fpt.edu.vn</a></li>
            </ul>
          </ol>

          <h3 className="ml-2">
            Thank you for choosing KoiAuction.com for your koi fish purchase. We take your privacy
            seriously and are committed to providing a secure and enjoyable experience.
          </h3>
        </div>
      </section>
    </main>
  );
};

export default Privacy;
