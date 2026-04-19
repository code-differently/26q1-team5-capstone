import React, { useState } from 'react'
import './ApplicationCard.css'

function ApplicationCard({ application, onDelete, onEdit }) {
  // onDelete is the function being called when "Delete" button is clicked.
  const { applicationId, scholarship, status, savedDate, submittedDate, deadlineAlert, notes } = application
  // Fields like scholarship name, status, saved date, submitted date, deadline alert, and notes are being extracted from the application object for display in the card.
  const [isEditing, setIsEditing] = useState(false)
  const [selectedStatus, setSelectedStatus] = useState(status)

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A'
     return new Date(dateString).toLocaleDateString()
     //This formats the Date and if no date inputed it will return N/A
  }

  const getStatusColor = (statusValue) => {
    switch (statusValue) {
      case 'SAVED': return '#64748b'
      case 'IN_PROGRESS': return '#3b82f6'
      case 'SUBMITTED': return '#22c55e'
      case 'AWARDED': return '#eab308'
      case 'REJECTED': return '#ef4444'
      default: return '#64748b'

      //The application status color changes based off of the status of the application
    }
  }

  const handleEditClick = () => {
    setIsEditing(true)
    setSelectedStatus(status)
  }

  const handleSaveStatus = () => {
    if (selectedStatus !== status) {
      onEdit(applicationId, selectedStatus)
    }
    setIsEditing(false)
  }

  const handleCancelEdit = () => {
    setSelectedStatus(status)
    setIsEditing(false)
  }

  const getValidStatuses = () => {
  switch (status) {
    case 'SAVED':
      return ['SAVED', 'IN_PROGRESS', 'SUBMITTED', 'AWARDED', 'REJECTED']
    case 'IN_PROGRESS':
      return ['IN_PROGRESS', 'SUBMITTED', 'AWARDED', 'REJECTED']
    case 'SUBMITTED':
      return ['SUBMITTED', 'AWARDED', 'REJECTED']
    case 'AWARDED':
      return ['AWARDED', 'REJECTED']
    case 'REJECTED':
      return ['REJECTED', 'IN_PROGRESS', 'SAVED']
    default:
      return ['SAVED', 'IN_PROGRESS', 'SUBMITTED', 'AWARDED', 'REJECTED']
  }
}

  if (!scholarship) return <div className="card error">Scholarship data missing</div>

  return (
    <div className="card">
      <h3>{scholarship.name}</h3>
      {isEditing ? (
        <div className="status-edit">
          <select
            value={selectedStatus}
            onChange={(e) => setSelectedStatus(e.target.value)}
            className="status-select"
          >
            {getValidStatuses().map(statusOption => (
              <option key={statusOption} value={statusOption}>
                {statusOption.replace('_', ' ')}
              </option>
            ))}
          </select>
          <div className="status-edit-actions">
            <button className="btn-save" onClick={handleSaveStatus}>Save</button>
            <button className="btn-cancel" onClick={handleCancelEdit}>Cancel</button>
          </div>
        </div>
      ) : (
        <span className="status-badge" style={{ backgroundColor: getStatusColor(status) }}>
          {status.replace('_', ' ')}
        </span>
      )}
      <div className="card-details">
        <p><strong>Saved:</strong> {formatDate(savedDate)}</p>
        {submittedDate && <p><strong>Submitted:</strong> {formatDate(submittedDate)}</p>}
        <p><strong>Deadline Alert:</strong> {formatDate(deadlineAlert)}</p>
        {notes && <p><strong>Notes:</strong> {notes}</p>}
      </div>
      <div className="card-actions">
        <button className="btn-edit" onClick={handleEditClick}>Edit Status</button>
        <button className="btn-delete" onClick={() => onDelete(applicationId)}>Delete</button>
      </div>
    </div>
  )
}

export default ApplicationCard
