package entity;

import javax.persistence.*;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    private long id;

    @Enumerated(EnumType.STRING)
    private RiskProfile riskProfile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RiskProfile getRiskProfile() {
        return riskProfile;
    }

    public void setRiskProfile(RiskProfile riskProfile) {
        this.riskProfile = riskProfile;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", riskProfile=" + riskProfile +
                '}';
    }
}
