User Stories


Admin:

As an Admin, I want to use the AdminDashboard to register new doctors into 
the MySQL Database, so that the system can maintain an accurate directory 
of active practitioners.

As an Admin, I want to view a unified list of all registered Patients and 
Admins (JPA Entities) to ensure data integrity across the healthcare 
network.

As an Admin, I want to manage login credentials stored in the MySQL 
Models, so that only authorized personnel can access sensitive patient 
data.

 Patient:

As a Patient, I want to use the PatientDashboard (via REST API) to 
retrieve my Prescription documents from MongoDB, so that I can access my 
treatment plan from any mobile device.

As a Patient, I want to schedule a visit through the Appointments module, 
which will create a new entry in the MySQL Appointment table, so that my 
spot is reserved in the doctor’s calendar.

As a Patient, I want to update my contact information via a JSON API, so 
that the Patient JPA Entity reflects my most current details.



Doctor:

As a Doctor, I want to create a Prescription in the DoctorDashboard that 
can include varying fields (like dosage, duration, and special notes) 
stored as a MongoDB Document, so that I am not restricted by a rigid table 
structure.

As a Doctor, I want to pull a list of today's Appointments from the MySQL 
Database, so that I can prepare for my upcoming consultations.

As a Doctor, I want the Service Layer to aggregate Patient data from MySQL 
with their Prescription history from MongoDB, providing a 360-degree view 
of the patient's health.



