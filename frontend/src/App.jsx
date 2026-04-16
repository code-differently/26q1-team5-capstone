import { useState } from 'react'
import { Routes, Route } from 'react-router-dom'
import { AuthProvider } from './context/AuthContext'
import './App.css'
import ApplicationCard from './Components/ApplicationCard'
// import MatchCard from './Components/MatchCard'
import ProfileCard from './Components/ProfileCard'
import ScholarshipCard from './Components/ScholarshipCard'
import Navbar from './Components/Navbar'
import HomePage from './Pages/HomePage'
import LoginPage from './Pages/LoginPage'
import RegistrationPage from './Pages/RegistrationPage'
// import MatchesPage from './Pages/MatchesPage'
import ScholarshipPage from './Pages/ScholarshipPage'
import ApplicationPage from './Pages/ApplicationPage'
import ProfilePage from './Pages/ProfilePage'


function App() {

  

  // Mock data - replace with API calls to MatchingService & ApplicationService
  const [matches] = useState([
    {
      scholarship: {
        scholarshipId: 1,
        name: "STEM Excellence Award",
        amount: 5000,
        deadline: "2026-05-01",
        fieldOfStudy: "Computer Science"
      },
      matchScore: 94
    },
    {
      scholarship: {
        scholarshipId: 2,
        name: "Future Leaders Grant",
        amount: 2500,
        deadline: "2026-04-15",
        fieldOfStudy: "Business"
      },
      matchScore: 78
    },
    {
      scholarship: {
        scholarshipId: 3,
        name: "Arts & Humanities Fund",
        amount: 3000,
        deadline: "2026-06-30",
        fieldOfStudy: "Liberal Arts"
      },
      matchScore: 62
    }
  ])

  const [applications, setApplications] = useState([
    {
      applicationId: 101,
      scholarship: { name: "STEM Excellence Award" },
      status: "IN_PROGRESS",
      savedDate: "2026-03-10",
      submittedDate: null,
      deadlineAlert: "2026-05-01",
      notes: "Need recommendation letter from Prof. Smith"
    },
    {
      applicationId: 102,
      scholarship: { name: "Women in Tech Scholarship" },
      status: "SUBMITTED",
      savedDate: "2026-02-20",
      submittedDate: "2026-03-01",
      deadlineAlert: "2026-03-15",
      notes: null
    },
    {
      applicationId: 103,
      scholarship: { name: "Community Service Award" },
      status: "SAVED",
      savedDate: "2026-04-01",
      submittedDate: null,
      deadlineAlert: "2026-07-01",
      notes: "Gather volunteer hour logs"
    }
  ])

  const [profile] = useState({
    name: 'Jordan Lee',
    gpa: 3.8,
    major: 'Computer Science',
    enrollmentStatus: 'Full-time',
    needsFinancialAid: true,
    state: 'CA',
    ethnicity: 'Asian',
    careerGoals: 'Software engineering',
    interests: 'AI, robotics, volunteer work'
  })

  const handleDelete = (applicationId) => {
    console.log("Delete application:", applicationId)
    setApplications(applications.filter(app => app.applicationId!== applicationId))
  }

  return (
    <AuthProvider>
      <Navbar />
      <Routes>
        <Route path="/" element={
          <HomePage
            matches={matches}
            applications={applications}
            profile={profile}
            onDeleteApplication={handleDelete}
          />
        } />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegistrationPage />} />
        {/* <Route path="/matches" element={<MatchesPage />} /> */}
        <Route path="/scholarships" element={<ScholarshipPage />} />
        <Route path="/applications" element={<ApplicationPage />} />
        <Route path="/profile" element={<ProfilePage />} />
      </Routes>
    </AuthProvider>
  )
}

export default App