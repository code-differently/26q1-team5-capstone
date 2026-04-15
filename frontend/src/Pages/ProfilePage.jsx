import React, { useState, useEffect } from 'react';
import ProfileCard from '../Components/ProfileCard';

const ProfilePage = () => {
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isEditing, setIsEditing] = useState(false);
  const [editedProfile, setEditedProfile] = useState({});

  // Mock data - replace with API call to UserController
  const mockProfile = {
    userId: 1,
    name: 'Jordan Lee',
    email: 'jordan.lee@university.edu',
    gpa: 3.8,
    major: 'Computer Science',
    enrollmentStatus: 'Full-time',
    graduationYear: 2027,
    needsFinancialAid: true,
    state: 'CA',
    ethnicity: 'Asian',
    gender: 'Non-binary',
    careerGoals: 'Software engineering at a tech company',
    interests: 'AI, robotics, volunteer work',
    extracurriculars: 'Computer Science Club, Hackathon organizer',
    workExperience: 'Intern at TechCorp (Summer 2025)',
    awards: 'Deans List (2024, 2025)',
    challenges: 'First-generation college student'
  };

  useEffect(() => {
    // TODO: Replace with API call: axios.get('/api/users/profile')
    setTimeout(() => {
      setProfile(mockProfile);
      setEditedProfile(mockProfile);
      setLoading(false);
    }, 500);
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const handleEdit = () => {
    setIsEditing(true);
  };

  const handleSave = () => {
    // TODO: API call: axios.put('/api/users/profile', editedProfile)
    setProfile(editedProfile);
    setIsEditing(false);
    alert('Profile updated successfully!');
  };

  const handleCancel = () => {
    setEditedProfile(profile);
    setIsEditing(false);
  };

  const handleProfileChange = (field, value) => {
    setEditedProfile(prev => ({
      ...prev,
      [field]: value
    }));
  };

  if (loading) {
    return (
      <div className="page-container">
        <div className="loading">Loading profile...</div>
      </div>
    );
  }

  return (
    <div className="page-container">
      <h1>My Profile</h1>

      {isEditing ? (
        <div className="profile-edit-form">
          <h2>Edit Profile</h2>
          <div className="form-grid">
            <div className="form-group">
              <label>Name:</label>
              <input
                type="text"
                value={editedProfile.name || ''}
                onChange={(e) => handleProfileChange('name', e.target.value)}
              />
            </div>

            <div className="form-group">
              <label>Email:</label>
              <input
                type="email"
                value={editedProfile.email || ''}
                onChange={(e) => handleProfileChange('email', e.target.value)}
              />
            </div>

            <div className="form-group">
              <label>GPA:</label>
              <input
                type="number"
                step="0.1"
                min="0"
                max="4.0"
                value={editedProfile.gpa || ''}
                onChange={(e) => handleProfileChange('gpa', parseFloat(e.target.value))}
              />
            </div>

            <div className="form-group">
              <label>Major:</label>
              <input
                type="text"
                value={editedProfile.major || ''}
                onChange={(e) => handleProfileChange('major', e.target.value)}
              />
            </div>

            <div className="form-group">
              <label>Enrollment Status:</label>
              <select
                value={editedProfile.enrollmentStatus || ''}
                onChange={(e) => handleProfileChange('enrollmentStatus', e.target.value)}
              >
                <option value="Full-time">Full-time</option>
                <option value="Part-time">Part-time</option>
                <option value="Graduate">Graduate</option>
              </select>
            </div>

            <div className="form-group">
              <label>Graduation Year:</label>
              <input
                type="number"
                value={editedProfile.graduationYear || ''}
                onChange={(e) => handleProfileChange('graduationYear', parseInt(e.target.value))}
              />
            </div>

            <div className="form-group">
              <label>State:</label>
              <input
                type="text"
                value={editedProfile.state || ''}
                onChange={(e) => handleProfileChange('state', e.target.value)}
              />
            </div>

            <div className="form-group">
              <label>Career Goals:</label>
              <textarea
                value={editedProfile.careerGoals || ''}
                onChange={(e) => handleProfileChange('careerGoals', e.target.value)}
                rows="3"
              />
            </div>

            <div className="form-group">
              <label>Interests:</label>
              <textarea
                value={editedProfile.interests || ''}
                onChange={(e) => handleProfileChange('interests', e.target.value)}
                rows="2"
              />
            </div>

            <div className="form-group">
              <label>Extracurriculars:</label>
              <textarea
                value={editedProfile.extracurriculars || ''}
                onChange={(e) => handleProfileChange('extracurriculars', e.target.value)}
                rows="2"
              />
            </div>

            <div className="form-group">
              <label>Work Experience:</label>
              <textarea
                value={editedProfile.workExperience || ''}
                onChange={(e) => handleProfileChange('workExperience', e.target.value)}
                rows="2"
              />
            </div>

            <div className="form-group">
              <label>Awards:</label>
              <textarea
                value={editedProfile.awards || ''}
                onChange={(e) => handleProfileChange('awards', e.target.value)}
                rows="2"
              />
            </div>

            <div className="form-group">
              <label>Challenges:</label>
              <textarea
                value={editedProfile.challenges || ''}
                onChange={(e) => handleProfileChange('challenges', e.target.value)}
                rows="2"
              />
            </div>
          </div>

          <div className="form-actions">
            <button onClick={handleSave} className="save-btn">Save Changes</button>
            <button onClick={handleCancel} className="cancel-btn">Cancel</button>
          </div>
        </div>
      ) : (
        <ProfileCard
          profile={profile}
          isEditable={true}
          onEdit={handleEdit}
        />
      )}
    </div>
  );
};

export default ProfilePage;