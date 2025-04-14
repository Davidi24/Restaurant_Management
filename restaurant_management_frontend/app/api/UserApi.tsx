export async function loginUser(email: string, password: string) {
    const response = await fetch('http://localhost:10/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ email, password }),
      credentials: 'include', 
    });
  
    if (!response.ok) {
      const errorData = await response.json();
      console.log("ErrorData: ", errorData)
      throw new Error(errorData?.message || 'Login failed');
    }
    const data = await response.json();
    return data;
  }
  