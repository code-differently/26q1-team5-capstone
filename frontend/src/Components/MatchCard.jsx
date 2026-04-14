import React from 'react'
import { Link } from 'react-router-dom'


function MatchCard({ scholarship, matchScore }) { 
  const { scholarshipId, name, amount, deadline, fieldOfStudy } = scholarship
  // The MatchCard component takes in a scholarship object and an optional matchScore.
  //  It displays the scholarship's name, amount, deadline, and field of study. 
  // If a matchScore is provided, it shows the percentage match at the top of the card.
  //  The "View Details" button links to a detailed page for that specific scholarship using its ID.

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A'
    return new Date(dateString).toLocaleDateString()
    //This formats the Date and if no date inputed it will return N/A
  }

  return (
    <div className="card match-card">
      {matchScore && <div className="match-score">{matchScore}% Match</div>}
      <h3>{name}</h3>
      <p><strong>Amount:</strong> ${amount?.toLocaleString()}</p>
      <p><strong>Deadline:</strong> {formatDate(deadline)}</p>
      <p><strong>Field:</strong> {fieldOfStudy}</p> // Displays the scholarship's name, amount, deadline, and field of study.
      <Link to={`/scholarships/${scholarshipId}`}> // The "View Details" button links to a detailed page for that specific scholarship using its ID.
        <button>View Details</button>
      </Link>
    </div>
  )
}

export default MatchCard

