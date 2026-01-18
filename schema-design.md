## MySQL Database Design


### Table: Appointments
Column NameData TypeKey / Relationshipappointment_idUUID / INTPrimary Key (PK)patient_idUUID / INTForeign Key (FK) references Patients(patient_id)doctor_idUUID / INTForeign Key (FK) references 
Doctors(doctor_id)appointment_dtDATETIMEThe date and time of the visitreasonTEXTstatusVARCHAR(20)(e.g., 'Scheduled', 'Completed', 'No-show')

### Table: Patients
Column Name	Data Type	Key / Relationship
patient_id	UUID / INT	Primary Key (PK)
first_name	VARCHAR(50)	
last_name	VARCHAR(50)	
date_of_birth	DATE	
gender	VARCHAR(15)	
email	VARCHAR(100)	Unique constraint
phone_number	VARCHAR(20)	
emergency_contact	VARCHAR(100)

### Table: Doctors
Column NameData TypeKey / Relationshipdoctor_idUUID / INTPrimary Key (PK)first_nameVARCHAR(50)last_nameVARCHAR(50)specializationVARCHAR(100)license_numberVARCHAR(50)Unique 
constraintemailVARCHAR(100)hire_dateDATE

### Table: Admin
Column NameData TypeKey / Relationshipadmin_idUUID / INTPrimary Key (PK)usernameVARCHAR(50)Uniquepassword_hashVARCHAR(255)For secure authenticationroleVARCHAR(30)(e.g., 'Receptionist', 'Billing', 
'SuperAdmin')last_loginTIMESTAMP


## MongoDB Collections Design

### Collection: prescriptions
```json
{
  "patient_id": "xyz",
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "refillCount": 2,
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  }
}
