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
@Table(name = "Project", schema = "public")
@XmlRootElement
public class Project implements java.io.Serializable{

    private static final long serialVersionUID = 5898770842921849170L;
    private int id;
    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private Date lastDemo;
    private String lastUpdate;
    private float percentage;
    private boolean onTrack;
    private boolean activate;
    private User user;
    
    public Project() {
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public int getId() {
        return id;
    }
    public void setId(int idProject) {
        this.id = idProject;
    }
    
    @Column(name = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    @Column(name = "description")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Column(name = "startDate")
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date start) {
        this.startDate = start;
    }
    
    @Column(name = "endDate")
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date end) {
        this.endDate = end;
    }
    
    @Column(name = "lastDemo")
    public Date getLastDemo() {
        return lastDemo;
    }
    public void setLastDemo(Date lastDemo) {
        this.lastDemo = lastDemo;
    }
    
    @Column(name = "lastUpdate")
    public String getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    @Column(name = "percentage")
    public float getPercentage() {
        return percentage;
    }
    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }
    
    @Column(name = "onTrack")
    public boolean getOnTrack() {
        return onTrack;
    }
    public void setOnTrack(boolean onTrack) {
        this.onTrack = onTrack;
    }
    
    @Column(name = "activate")
    public boolean getActivate() {
        return activate;
    }
    public void setActivate(boolean activate) {
        this.activate = activate;
    }
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Project project = (Project) o;
        if (id != project.id)
            return false;
        return true;
    }
}
