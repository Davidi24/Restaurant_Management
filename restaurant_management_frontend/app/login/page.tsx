import React from 'react';
import LoginForm from "../components/Login/LoginForm";
import image_1 from "../assets/Images/image.png";
import Image from "next/image";
function Login() {
    return (
        <div
            className="lg:flex block h-screen lg:h-[100vh] lg:py-12 lg:px-16 shadow
         shadow-gray-300 rounded-lg bg-amber-50 bg-[url('/assets/Images/image.png')]
          bg-cover bg-center bg-no-repeat lg:bg-none my-auto"
        >
            <div className="md:w-1/2 mx-auto my-auto">
                <LoginForm />
            </div>
            <div className="hidden lg:block md:w-1/2 relative">
                <Image
                    src={image_1}
                    alt="A description of the image"
                    layout="intrinsic"
                    className="max-w-full min-h-full max-h-full"
                />
            </div>
        </div>
    );
}

export default Login;
