import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'
import ApplicationCard from './Components/ApplicationCard'
import MatchCard from './Components/MatchCard'

function App() {
  const [count, setCount] = useState(0)

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

  const handleEdit = (applicationId) => {
    console.log("Edit application:", applicationId)
    // TODO: Open edit modal or navigate to /applications/${applicationId}/edit
    alert(`Editing application ${applicationId}`)
  }

  const handleDelete = (applicationId) => {
    console.log("Delete application:", applicationId)
    setApplications(applications.filter(app => app.applicationId!== applicationId))
  }

  return (
    <>
      <div>
        <a href="https://vitejs.dev" target="_blank">
          <img src={viteLogo} className="logo" alt="Vite logo" />
        </a>
        <a href="https://react.dev" target="_blank">
          <img src={reactLogo} className="logo react" alt="React logo" />
        </a>
      </div>

      <img src={heroImg} alt="Hero" className="hero" />
      <h1>Scholarship Tracker</h1>

      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.jsx</code> and save to test HMR
        </p>
      </div>

      <main className="dashboard">
        <section className="matches-section">
          <h2>Recommended For You</h2>
          <div className="card-grid">
            {matches.length > 0? (
              matches.map(match => (
                <MatchCard
                  key={match.scholarship.scholarshipId}
                  scholarship={match.scholarship}
                  matchScore={match.matchScore}
                />
              ))
            ) : (
              <p className="empty-state">No matches yet. Complete your profile to get recommendations.</p>
            )}
          </div>
        </section>

        <section className="applications-section">
          <h2>Your Applications</h2>
          <div className="card-grid">
            {applications.length > 0? (
              applications.map(app => (
                <ApplicationCard
                  key={app.applicationId}
                  application={app}
                  onEdit={handleEdit}
                  onDelete={handleDelete}
                />
              ))
            ) : (
              <p className="empty-state">You haven't saved any applications yet.</p>
            )}
          </div>
        </section>
      </main>
    </>
  )
}

export default App