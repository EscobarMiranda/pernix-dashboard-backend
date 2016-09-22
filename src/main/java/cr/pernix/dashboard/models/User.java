package cr.pernix.dashboard.models;

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
@Table(name = "User", schema = "public")
@XmlRootElement
public class User implements java.io.Serializable {

  private int id;
  private String email;
  private String lastname;
  private String name;
  private String password;
  private UserType usertype;

  public User(){

  }

  public User(String email, String lastname, String name, String password){
    this.email = email;
    this.lastname = lastname;
    this.name = name;
    this.password = password;
  }

  public User(String email, String lastname, String name, String password, UserType userType){
    this.email = email;
    this.lastname = lastname;
    this.name = name;
    this.password = password;
    this.usertype = userType;
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

  @Column(name = "name")

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "lastname")

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  @Column(name = "email")

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Column(name = "password")

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_type_id", nullable = false)
  public UserType getUserType() {
    return this.usertype;
  }

  public void setUserType(UserType userType) {
    this.usertype = userType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
	  return false;

    User user = (User) o;
    if (id != user.id)
      return false;
    return true;
  }

}
