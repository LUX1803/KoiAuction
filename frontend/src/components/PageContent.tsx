import React from 'react';
import koiImage from "./image/1.jpg"
const PageContent: React.FC = () => {
    return (
        <main id="page-content" className="flex-auto relative">
            <section className="min-h-[calc(100vh-4rem)] px-4 py-8 flex justify-center" style={{}}>
                <div className="max-w-[1500px] w-full">
                    <div className="flex flex-col w-full items-center justify-center relative">
                        <div className="min-h-[85vh] grid place-items-center">
                            <div className="relative mb-20">
                                <div style={{}}>
                                    <div className="relative flex flex-col items-center mb-20 w-full">
                                        
                                        <img
                                            className="img-fluid max-w-[50rem] w-full rounded-3xl shadow-sm bg-gray-100"
                                            src={koiImage}
                                            alt="Koi Breeders"
                                        />
                                        <div className="absolute -bottom-4">
                                            <div className="flex items-end justify-center rounded-lg variant-glass-surface py-1 pl-4">
                                                <div className="w-8 sm:w-12 lg:w-24 mb-1">
                                                    <svg className="fill-primary-500" viewBox="50 145 790 790">
                                                        {/* SVG Path */}
                                                    </svg>
                                                </div>
                                                <h1 className="text-4xl sm:text-6xl lg:text-8xl font-medium font-['Righteous'] text-black">
                                                    KOIAUCTION
                                                </h1>
                                                <p className="text-[.6rem] sm:text-base lg:text-2xl leading-none -rotate-90 text-black lg:my-8 sm:my-4 my-3.5 lg:-translate-x-5 sm:-translate-x-3 -translate-x-2 tracking-wide font-medium">
                                                    .COM
                                                </p>
                                            </div>
                                        </div>
                                    </div>
                                    <h1 className="max-w-7xl text-[6vw] 2xl:text-7xl 2xl:leading-snug font-bold capitalize leading-snug text-center h1 mb-8">
                                        Experience the ultimate koi auction platform crafted by <span className="text-red-500">FPT</span>, connecting you with premier koi breeders."
                                    </h1>
                                    <div className="w-full grid place-items-center mb-10">
                                        <div className="grid grid-cols-2 max-w-md mt-10 gap-3 sm:gap-4">
                                            <a className="btn sm:btn-xl rounded-full bg-cyan-500 hover:bg-cyan-600 text-white font-semibold flex items-center justify-center h-12 w-[150px] text-lg" href="/auction">
                                                View Auctions
                                            </a>
                                            <a className="btn sm:btn-xl rounded-full bg-cyan-500 hover:bg-cyan-600 text-white font-semibold grid place-items-center h-12 w-[150px] text-lg" href="/about">
                                                Learn More
                                            </a>    
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </main >
    );
};

export default PageContent;
