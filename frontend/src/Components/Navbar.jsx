import { useState, useEffect, useRef } from 'react'
import { Link, useNavigate } from 'react-router-dom'
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
    // TODO: Call UserController logout + clear token
    logout()
    closeMenu()
    navigate('/login')
  }

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (navRef.current &&!navRef.current.contains(event.target)) {
        setIsOpen(false)
      }
    }
    if (isOpen) document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [isOpen])

  return (
    <nav className="navbar" ref={navRef}>
      <div className="nav-container">
        <Link to="/" className="nav-logo" onClick={closeMenu}>
          <img src={logoImg} alt="Scholarship Finder logo" className="nav-logo-img" />
          <h2>Scholarship Finder</h2>
        </Link>

        <div className="hamburger" onClick={toggleMenu}>
          <span className={isOpen? 'bar open' : 'bar'}></span>
          <span className={isOpen? 'bar open' : 'bar'}></span>
          <span className={isOpen? 'bar open' : 'bar'}></span>
        </div>

        <div className={isOpen? 'nav-links active' : 'nav-links'}>
          <Link to="/" onClick={closeMenu}>Home</Link>
          <Link to="/scholarships" onClick={closeMenu}>Scholarships</Link>
          <Link to="/matches" onClick={closeMenu}>Matches</Link>
          <Link to="/applications" onClick={closeMenu}>My Applications</Link>
          <Link to="/profile" onClick={closeMenu}>Profile</Link>
          {user ? (
            <>
              <span className="nav-user">{user.username}</span>
              <button onClick={handleLogout} className="nav-btn">Logout</button>
            </>
          ) : (
            <div className="auth-links">
              <Link to="/login" onClick={closeMenu}>Login</Link>
              <Link to="/register" onClick={closeMenu} className="register-link">Register</Link>
            </div>
          )}
        </div>
      </div>
      {isOpen && <div className="nav-overlay" onClick={closeMenu}></div>}
    </nav>
  )
}

export default Navbar