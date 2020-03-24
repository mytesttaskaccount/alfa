package entity;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.EnumType;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                riskProfile == client.riskProfile;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, riskProfile);
    }
}
