package cr.pernix.dashboard.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "CustomerSatisfaction", schema = "public")
@XmlRootElement
public class CustomerSatisfaction implements java.io.Serializable {
    
    private int id;
    private int value;
    private Date timestamp;
    private Metric metric;
    private Manager manager;

    public CustomerSatisfaction() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "value")
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Column(name = "timestamp")
    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "metric_id", nullable = false)
    public Metric getMetric() {
        return this.metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manager_id", nullable = false)
    public Manager getManager() {
      return this.manager;
    }

    public void setManager(Manager manager) {
      this.manager = manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CustomerSatisfaction customerSatisfaction = (CustomerSatisfaction) o;
        if (id != customerSatisfaction.id)
            return false;
        return true;
    }

}
