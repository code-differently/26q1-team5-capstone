import React from 'react'
import './MatchCard.css'

function MatchCard({ scholarship, matchScore }) {
  const { name, amount, deadline, fieldOfStudy } = scholarship

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A'
    return new Date(dateString).toLocaleDateString()
  }

  return (
    <div className="card match-card">
      {matchScore && <div className="match-score">{matchScore}% Match</div>}
      <h3>{name}</h3>
      <p><strong>Amount:</strong> ${amount?.toLocaleString()}</p>
      <p><strong>Deadline:</strong> {formatDate(deadline)}</p>
      <p><strong>Field:</strong> {fieldOfStudy}</p>
      <button type="button">View Details</button>
    </div>
  )
}

export default MatchCard

