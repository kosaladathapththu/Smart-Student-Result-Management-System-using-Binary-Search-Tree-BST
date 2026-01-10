package model;

public class Student {
    private final String indexNo;   // BST key
    private String name;

    private double pdsa;
    private double se;
    private double dm2;

    public Student(String indexNo, String name, double pdsa, double se, double dm2) {
        this.indexNo = indexNo;
        this.name = name;
        this.pdsa = pdsa;
        this.se = se;
        this.dm2 = dm2;
    }

    public String getIndexNo() { return indexNo; }
    public String getName() { return name; }
    public double getPdsa() { return pdsa; }
    public double getSe() { return se; }
    public double getDm2() { return dm2; }

    public void setName(String name) { this.name = name; }
    public void setPdsa(double pdsa) { this.pdsa = pdsa; }
    public void setSe(double se) { this.se = se; }
    public void setDm2(double dm2) { this.dm2 = dm2; }

    public double getTotal() { return pdsa + se + dm2; }
    public double getAverage() { return getTotal() / 3.0; }

    public String getGrade() {
        double avg = getAverage();
        if (avg >= 75) return "A";
        if (avg >= 65) return "B";
        if (avg >= 55) return "C";
        if (avg >= 40) return "D";
        return "F";
    }

    public boolean isAtRisk() { return getAverage() < 40; }

    public boolean isScholarshipEligible() {
        return getAverage() >= 70 && pdsa >= 40 && se >= 40 && dm2 >= 40;
    }

    @Override
    public String toString() {
        return String.format(
            "Index: %s | Name: %s | PDSA: %.1f | SE: %.1f | DM2: %.1f | Avg: %.1f | Grade: %s",
            indexNo, name, pdsa, se, dm2, getAverage(), getGrade()
        );
    }
}
