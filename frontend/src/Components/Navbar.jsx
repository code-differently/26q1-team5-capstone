import { useState, useEffect, useRef } from 'react'
import { NavLink, useNavigate } from 'react-router-dom'
import logoImg from '../assets/logo.png'
import './Navbar.css'
import { useAuth } from '../context/AuthContext'

function Navbar() {
  const [isOpen, setIsOpen] = useState(false)
  const { user, logout } = useAuth()
  const navRef = useRef(null)
  const navigate = useNavigate()

  const toggleMenu = () => setIsOpen(!isOpen)
  const closeMenu = () => setIsOpen(false)

  const handleLogout = () => {
    logout()
    closeMenu()
    navigate('/login')
  }

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (navRef.current && !navRef.current.contains(event.target)) {
        setIsOpen(false)
      }
    }
    if (isOpen) document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [isOpen])

  return (
    <nav className="navbar" ref={navRef}>
      <div className="nav-container">
        <NavLink to="/" className="nav-logo" onClick={closeMenu}>
          <img src={logoImg} alt="Scholarship Finder logo" className="nav-logo-img" />
          <h2>Scholarship Finder</h2>
        </NavLink>

        <div className="hamburger" onClick={toggleMenu}>
          <span className={isOpen ? 'bar open' : 'bar'}></span>
          <span className={isOpen ? 'bar open' : 'bar'}></span>
          <span className={isOpen ? 'bar open' : 'bar'}></span>
        </div>

        <div className={isOpen ? 'nav-links active' : 'nav-links'}>
          <NavLink
            to="/"
            end
            onClick={closeMenu}
            className={({ isActive }) => isActive ? 'nav-active' : ''}
          >
            Home
          </NavLink>
          <NavLink
            to="/scholarships"
            onClick={closeMenu}
            className={({ isActive }) => isActive ? 'nav-active' : ''}
          >
            Scholarships
          </NavLink>
          <NavLink
            to="/applications"
            onClick={closeMenu}
            className={({ isActive }) => isActive ? 'nav-active' : ''}
          >
            My Applications
          </NavLink>
          <NavLink
            to="/profile"
            onClick={closeMenu}
            className={({ isActive }) => isActive ? 'nav-active' : ''}
          >
            Profile
          </NavLink>

          {user ? (
            <>
              <span className="nav-user">{user.username}</span>
              <button onClick={handleLogout} className="nav-btn">Logout</button>
            </>
          ) : (
            <div className="auth-links">
              <NavLink
                to="/login"
                onClick={closeMenu}
                className={({ isActive }) => isActive ? 'nav-active' : ''}
              >
                Login
              </NavLink>
              <NavLink
                to="/register"
                onClick={closeMenu}
                className={({ isActive }) =>
                  isActive ? 'register-link nav-active' : 'register-link'
                }
              >
                Register
              </NavLink>
            </div>
          )}
        </div>
      </div>
      {isOpen && <div className="nav-overlay" onClick={closeMenu}></div>}
    </nav>
  )
}

export default Navbar