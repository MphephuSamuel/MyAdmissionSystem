    /*
     * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
     * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
     */
    package za.ump.scms.bict.myadmissionsystem.model;

    import jakarta.persistence.CascadeType;
    import jakarta.persistence.Column;
    import jakarta.persistence.Entity;
    import jakarta.persistence.GeneratedValue;
    import jakarta.persistence.GenerationType;
    import jakarta.persistence.Id;
    import jakarta.persistence.OneToMany;
    import jakarta.persistence.OneToOne;
    import jakarta.persistence.Table;
    import jakarta.persistence.Temporal;
    import jakarta.persistence.TemporalType;
    import java.io.Serializable;
    import java.util.Date;
    import java.util.List;

    /**
     *
     * @author mphep
     */
    @Entity
    @Table(name="APPLICANT")
    public class Applicant implements Serializable {

        private static final long serialVersionUID = 1L;
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "APPLICANT_ID")
        private Long id;

        @Column(name = "ID_NUMBER", unique=true, nullable=false)
        private String idNumber;

        @Column(name="STUDENT_NUMBER", unique=true, nullable=false)
        private String studentNumber;

        @Column(name="PIN")
        private String pin;

        @Column(name="TITLE")
        private String title;

        @Column(name="DATE_OF_BIRTH")
        @Temporal(TemporalType.DATE)
        private Date dateOfBIrth;

        @Column(name="FIRST_NAME")
        private String firstName;

        @Column(name="MIDDLE_NAME")
        private String middleName;

        @Column(name="LAST_NAME")
        private String lastName;

        @Column(name="GENDER")
        private String gender;

        @Column(name="PHONE_NUMBER")
        private String phoneNumber;

        @Column(name="ALTERNATIVE_PHONE_NUMBER")
        private String alternativePhoneNumber;
        
        @Column(name="EMAIL", nullable=false)
        private String email;

        @Column(name="DISABILITY")
        private String disability;

        @Column(name="HOME_ADDRESS")
        private String homeAddress;

        @Column(name="POSTAL_ADDRESS")
        private String postalAddress;

        @Column(name="HOME_LANGAUGE")
        private String homeLanguage;

        @Column(name="ALTERNATIVE_LANGUAGE")
        private String alternativeLanguage;

        @Column(name="MARRIAGE_STATUS")
        private String marriageStatus;
        
        @Column(name="School")
        private String school;

        @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval=true)
        private List<Application> applications;

        @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval=true)
        private List<Mark> marks;

        @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval=true)
        private List<NextOfKin> nextOfKin;

        @OneToMany(mappedBy = "applicant", cascade = CascadeType.ALL, orphanRemoval=true)
        private List<Payment> payments;
        
        @OneToOne(mappedBy = "applicant",cascade = CascadeType.ALL, orphanRemoval=true)
        private Document documents;

        public Applicant() {
        }

        public Applicant(String idNumber, String studentNumber, String pin, String title, Date dateOfBIrth, 
                String firstName, String middleName, String lastName, String gender, String phoneNumber, 
                String alternativePhoneNumber, String disability, String homeAddress, String postalAddress, 
                String homeLanguage, String alternativeLanguage, String marriageStatus, String email, String school) {
            this.idNumber = idNumber;
            this.studentNumber = studentNumber;
            this.pin = pin;
            this.title = title;
            this.dateOfBIrth = dateOfBIrth;
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
            this.gender = gender;
            this.phoneNumber = phoneNumber;
            this.alternativePhoneNumber = alternativePhoneNumber;
            this.disability = disability;
            this.homeAddress = homeAddress;
            this.postalAddress = postalAddress;
            this.homeLanguage = homeLanguage;
            this.alternativeLanguage = alternativeLanguage;
            this.marriageStatus = marriageStatus;
            this.email=email;
            this.school = school;
        }

        public String getStudentNumber() {
            return studentNumber;
        }

        public void setStudentNumber(String studentNumber) {
            this.studentNumber = studentNumber;
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getPin() {
            return pin;
        }

        public void setPin(String pin) {
            this.pin = pin;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Date getDateOfBIrth() {
            return dateOfBIrth;
        }

        public void setDateOfBIrth(Date dateOfBIrth) {
            this.dateOfBIrth = dateOfBIrth;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAlternativePhoneNumber() {
            return alternativePhoneNumber;
        }

        public void setAlternativePhoneNumber(String alternativePhoneNumber) {
            this.alternativePhoneNumber = alternativePhoneNumber;
        }

        public String getDisability() {
            return disability;
        }

        public void setDisability(String disability) {
            this.disability = disability;
        }

        public String getHomeAddress() {
            return homeAddress;
        }

        public void setHomeAddress(String homeAddress) {
            this.homeAddress = homeAddress;
        }

        public String getPostalAddress() {
            return postalAddress;
        }

        public void setPostalAddress(String postalAddress) {
            this.postalAddress = postalAddress;
        }

        public String getHomeLanguage() {
            return homeLanguage;
        }

        public void setHomeLanguage(String homeLanguage) {
            this.homeLanguage = homeLanguage;
        }

        public String getAlternativeLanguage() {
            return alternativeLanguage;
        }

        public void setAlternativeLanguage(String alternativeLanguage) {
            this.alternativeLanguage = alternativeLanguage;
        }

        public String getMarriageStatus() {
            return marriageStatus;
        }

        public void setMarriageStatus(String marriageStatus) {
            this.marriageStatus = marriageStatus;
        }

        public List<Application> getApplications() {
            return applications;
        }

        public void setApplications(List<Application> applications) {
            this.applications = applications;
        }

        public List<Mark> getMarks() {
            return marks;
        }

        public void setMarks(List<Mark> marks) {
            this.marks = marks;
        }

        public List<NextOfKin> getNextOfKin() {
            return nextOfKin;
        }

        public void setNextOfKin(List<NextOfKin> nextOfKin) {
            this.nextOfKin = nextOfKin;
        }

        public List<Payment> getPayments() {
            return payments;
        }

        public void setPayments(List<Payment> payments) {
            this.payments = payments;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getSchool() {
            return school;
        }

        public void setSchool(String school) {
            this.school = school;
        }

    public Document getDocuments() {
        return documents;
    }

    public void setDocuments(Document documents) {
        this.documents = documents;
    }

        


        @Override
        public int hashCode() {
            int hash = 0;
            hash += (id != null ? id.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object object) {
            // TODO: Warning - this method won't work in the case the id fields are not set
            if (!(object instanceof Applicant)) {
                return false;
            }
            Applicant other = (Applicant) object;
            if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "za.ump.scms.bict.myadmissionsystem.model.Applicant[ id=" + id + " ]";
        }

    }
