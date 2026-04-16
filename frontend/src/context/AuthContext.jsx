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
    // Normalize common id fields to `userId` so frontend code can rely on it
    const normalized = { ...userData };
    if (!normalized.userId) {
      if (normalized.id) normalized.userId = normalized.id;
      else if (normalized.userID) normalized.userId = normalized.userID;
      else if (normalized.user && normalized.user.userId) normalized.userId = normalized.user.userId;
    }

    setUser(normalized);
    try { localStorage.setItem('user', JSON.stringify(normalized)); } catch (e) { console.error(e); }
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
