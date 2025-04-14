'use client';
import { loginUser } from "@/app/api/UserApi";
import { useState } from "react";
import { useRouter } from 'next/navigation';
import Loading from "../general/Loading";

function LoginForm() {
    const [error, setError] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [loading, setloading] = useState(false)
    const router = useRouter();


    const handlesubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        try {
            setloading(true);
            const data = await loginUser(email, password);
            console.log('Login successful:', data);
            router.replace('/admin');
            setloading(false)
        } catch (err: any) {
            setError(err.message || 'An unexpected error occurred');
            setloading(false)
        }
    };

    const handleChange = () => {
        setError("");
    };

    return (
        <div>
            <section className="w-full h-screen md:h-auto">
                <div className="flex flex-col items-center justify-center px-6 py-8 h-full">
                    <div className="w-full sm:mt-6 lg:mt-0 rounded-lg shadow shadow-gray-300 sm:max-w-md xl:p-0 bg-amber-50">
                        <div className="p-6 space-y-8 sm:space-y-4 md:space-y-6 sm:p-8">
                            <h1 className="text-xl font-bold leading-tight tracking-tight text-[#0e0d0d] md:text-2xl ">
                                Sign in to your account
                            </h1>
                            <div className="space-y-4 md:space-y-6">
                                <div>
                                    <label htmlFor="email" className="block mb-2 text-sm font-medium text-gray-900">Your email</label>
                                    <input
                                        type="email"
                                        id="email"
                                        value={email}
                                        onChange={(e) => {
                                            setEmail(e.target.value);
                                            handleChange();
                                        }}
                                        className="bg-gray-50 border border-gray-300 text-gray-900 rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                                        placeholder="name@company.com"
                                        required
                                    />
                                </div>
                                <label htmlFor="password" className="block mb-2 text-sm font-medium text-gray-900">Password</label>
                                <input
                                    type="password"
                                    id="password"
                                    value={password}
                                    onChange={(e) => {
                                        setPassword(e.target.value);
                                        handleChange();
                                    }}
                                    placeholder="••••••••"
                                    className="bg-gray-50 border border-gray-300 text-gray-900 rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5"
                                    required
                                />
                            </div>
                            <div className="flex justify-end">
                                <a href="#" className="text-sm font-medium text-primary-600 hover:underline dark:text-primary-500">Forgot password?</a>
                            </div>
                            {error && <div className="text-red-600 p-0 m-0 pb-2">{error}</div>}
                            <button
                                type="submit"
                                className="w-full flex cursor-pointer justify-center text-white bg-primary-600 hover:bg-primary-700 focus:ring-4 focus:outline-none focus:ring-primary-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center bg-[#0e0d0d]"
                                onClick={handlesubmit}
                            >
                                {loading ? (<Loading />) : 'Sign in'}
                            </button>

                            <p className="text-sm font-light text-gray-500 dark:text-gray-400">
                                Welcome to our <a href="#" className="font-medium text-primary-600 hover:underline dark:text-primary-500">Program</a>
                            </p>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    );
}

export default LoginForm;
