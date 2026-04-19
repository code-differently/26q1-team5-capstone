import React, { useState, useEffect } from 'react';
import axios from 'axios';
import ApplicationCard from '../Components/ApplicationCard';
import { useAuth } from '../context/AuthContext';
import PageTransition from '../Components/PageTransition';

const ApplicationPage = () => {
  const { user } = useAuth();
  const [applications, setApplications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('ALL');
  const API = import.meta.env.VITE_API_URL;

  useEffect(() => {
    const fetchApplications = async () => {
      if (!user?.userId) {
        setLoading(false);
        return;
      }
      try {
        const response = await axios.get(`${API}/api/applications/${user.userId}`);
        setApplications(response.data);
      } catch (error) {
        console.error('Error fetching applications:', error);
        setApplications([]);
      } finally {
        setLoading(false);
      }
    };

    fetchApplications();
  }, [user]);

  const handleDelete = async (applicationId) => {
    console.log("Delete application:", applicationId);
    if (window.confirm('Are you sure you want to delete this application?')) {
      try {
        await axios.delete(`${API}/api/applications/${applicationId}`);
        setApplications(applications.filter(app => app.applicationId !== applicationId));
      } catch (error) {
        console.error('Error deleting application:', error);
        alert('Failed to delete application');
      }
    }
  };

  const handleEditStatus = async (applicationId, newStatus) => {
    console.log("Update application status:", applicationId, newStatus);
    try {
      const response = await axios.put(`${API}/api/applications/${applicationId}/status`, {
        status: newStatus
      });
      // Update the application in the local state
      setApplications(applications.map(app =>
        app.applicationId === applicationId ? response.data : app
      ));
      alert('Application status updated successfully!');
    } catch (error) {
      console.error('Error updating application status:', error);
      alert('Failed to update application status. Please check if the status transition is valid.');
    }
  };

  const filteredApplications = applications.filter(app => {
    if (filter === 'ALL') return true;
    return app.status === filter;
  });

  const getStatusCounts = () => {
    const counts = { ALL: applications.length };
    applications.forEach(app => {
      counts[app.status] = (counts[app.status] || 0) + 1;
    });
    return counts;
  };

  const statusCounts = getStatusCounts();

  if (loading) {
    return (
      <PageTransition>
        <div className="page-container">
          <div className="loading">Loading applications...</div>
        </div>
      </PageTransition>
    );
  }

  return (
    <PageTransition>
      <div className="page-container">
        <h1>My Applications</h1>

        <div className="filter-tabs">
          {Object.entries(statusCounts).map(([status, count]) => (
            <button
              key={status}
              className={`filter-tab ${filter === status ? 'active' : ''}`}
              onClick={() => setFilter(status)}
            >
              {status === 'ALL' ? 'All' : status.replace('_', ' ')} ({count})
            </button>
          ))}
        </div>

        <div className="applications-grid">
          {filteredApplications.length > 0 ? (
            filteredApplications.map(application => (
              <ApplicationCard
                key={application.applicationId}
                application={application}
                onDelete={handleDelete}
                onEdit={handleEditStatus}
              />
            ))
          ) : (
            <div className="empty-state">
              <p>No applications found in this category.</p>
              <p>Start applying for scholarships to track your progress!</p>
            </div>
          )}
        </div>
      </div>
    </PageTransition>
  );
};

export default ApplicationPage;