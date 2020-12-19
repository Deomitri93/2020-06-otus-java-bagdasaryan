package ru.otus.core.model;

import javax.persistence.*;

@Entity
@Table(name = "tAddresses")
public class AddressDataSet {
    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "street")
    private String street;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    public AddressDataSet() {
    }

    public AddressDataSet(User user, String street) {
        this.id = user.getId();
        this.user = user;
        this.street = street;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "AddressDataSet{" +
                "street='" + street + '\'' +
                '}';
    }
}
