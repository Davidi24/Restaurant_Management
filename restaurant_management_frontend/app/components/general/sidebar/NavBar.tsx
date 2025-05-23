import React from 'react'

type NavBarProps = {
    setSidebarOpen: React.Dispatch<React.SetStateAction<boolean>>;
    setDropdownOpen: React.Dispatch<React.SetStateAction<boolean>>;
    sidebarOpen: boolean;
    dropdownOpen: boolean;
};

function NavBar({
    setSidebarOpen,
    setDropdownOpen,
    sidebarOpen,
    dropdownOpen,
}: NavBarProps) {
    return (
        <nav className="fixed top-0 z-50 w-full bg-white  border-gray-200 dark:bg-amber-50  ">
            <div
                className="absolute w-full h-full sm:ml-64"
                style={{ boxShadow: '0 3px 3px -2px rgba(0, 0, 0, 0.1)' }}
            ></div>
            <div className="px-3 py-3 lg:px-5 lg:pl-3">
                <div className="flex items-center justify-between">

                    <div className="flex items-center justify-start rtl:justify-end">
                        <button
                            onClick={() => setSidebarOpen(!sidebarOpen)}
                            type="button"
                            className="inline-flex items-center p-2 text-sm text-gray-500 rounded-lg sm:hidden hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-200 dark:text-gray-400 dark:hover:bg-gray-700 dark:focus:ring-gray-600"
                        >
                            <span className="sr-only">Open sidebar</span>
                            <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 20 20">
                                <path d="M2 4.75A.75.75 0 012.75 4h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 4.75zm0 10.5a.75.75 0 01.75-.75h7.5a.75.75 0 010 1.5h-7.5a.75.75 0 01-.75-.75zM2 10a.75.75 0 01.75-.75h14.5a.75.75 0 010 1.5H2.75A.75.75 0 012 10z" />
                            </svg>
                        </button>
                        <a href="https://flowbite.com" className="flex ms-2 md:me-24">
                            <img src="https://flowbite.com/docs/images/logo.svg" className="h-8 me-3" alt="FlowBite Logo" />
                            <span className="self-center text-xl font-semibold sm:text-2xl whitespace-nowrap text-[#0e0d0d]">Flowbite</span>
                        </a>
                    </div>


                    <div>dhhdhdsfufufufffffff</div>

                    <div>djdjhddhdhhdh</div>
                    <div className="flex items-center">
                        <div className="flex items-center ms-3 relative">
                            <button
                                type="button"
                                onClick={() => setDropdownOpen(!dropdownOpen)}
                                className="flex text-sm bg-gray-800 rounded-full focus:ring-4 focus:ring-gray-300 dark:focus:ring-gray-600"
                                data-user-button
                            >
                                <span className="sr-only text-[#0e0d0d]">Open user menu</span>
                                <img className="w-8 h-8 rounded-full" src="https://flowbite.com/docs/images/people/profile-picture-5.jpg" alt="user photo" />
                            </button>
                            <div
                                id="dropdown-user"
                                className={`absolute right-0 top-10 w-48 z-50 text-base list-none bg-white divide-y divide-gray-100 rounded-sm shadow-sm dark:bg-gray-700 dark:divide-gray-600 ${dropdownOpen ? '' : 'hidden'}`}
                            >
                                <div className="px-4 py-3" role="none">
                                    <p className="text-sm text-gray-900 dark:text-white">Neil Sims</p>
                                    <p className="text-sm font-medium text-gray-900 truncate dark:text-gray-300">neil.sims@flowbite.com</p>
                                </div>
                                <ul className="py-1" role="none">
                                    <li>
                                        <a href="#" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-600 dark:hover:text-white">Dashboard</a>
                                    </li>
                                    <li>
                                        <a href="#" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-600 dark:hover:text-white">Settings</a>
                                    </li>
                                    <li>
                                        <a href="#" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-600 dark:hover:text-white">Earnings</a>
                                    </li>
                                    <li>
                                        <a href="#" className="block px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-gray-600 dark:hover:text-white">Sign out</a>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    )
}

export default NavBar