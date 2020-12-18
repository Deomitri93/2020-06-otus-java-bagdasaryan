package ru.otus.core.model;

import javax.persistence.*;

@Entity
@Table(name = "tPhones")
public class PhoneDataSet {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "phone")
    private String phone;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public PhoneDataSet(){

    }

    public PhoneDataSet(User user, String phone) {
        this.user = user;
        this.phone = phone;
    }

    public PhoneDataSet(Long id, User user, String phone) {
        this.id = id;
        this.user = user;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "PhoneDataSet{" +
                "phone='" + phone + '\'' +
                '}';
    }
}
