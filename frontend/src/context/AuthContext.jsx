import React, { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);

  useEffect(() => {
    try {
      const raw = localStorage.getItem('user');
      if (raw) setUser(JSON.parse(raw));
    } catch (e) {
      console.error('Failed to parse stored user', e);
    }
  }, []);

  const login = (userData) => {
    setUser(userData);
    try { localStorage.setItem('user', JSON.stringify(userData)); } catch (e) { console.error(e); }
  };

  const logout = () => {
    setUser(null);
    try { localStorage.removeItem('user'); } catch (e) { console.error(e); }
  };

  return (
    <AuthContext.Provider value={{ user, login, logout, isAuthenticated: !!user }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);

export default AuthContext;
